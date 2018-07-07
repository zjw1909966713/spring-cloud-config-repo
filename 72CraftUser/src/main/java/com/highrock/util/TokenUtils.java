package com.highrock.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0.0
 * @Description token 操作类

 */
@Component
public class TokenUtils {

    private  Logger logger= LoggerFactory.getLogger(TokenUtils.class);

    @Value("${token.secret}")
    private  String secret;

    @Value("${token.expiration}")
    private  Long expiration;

    /**
     * 根据 TokenDetail 生成 Token
     *
     * @param username
     * @return
     */
    public  String generateToken(String username) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", username);
        claims.put("created", generateCurrentDate());
        return this.generateToken(claims);
    }

    /**
     * 根据 claims 生成 Token
     *
     * @param claims
     * @return
     */
    public  String generateToken(Map<String, Object> claims) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate())
                    .signWith(SignatureAlgorithm.HS512,this.secret.getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException ex) {
            //didn't want to have this method throw the exception, would rather log it and sign the token like it was before
          //  logger.warn(ex.getMessage());
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate())
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        }
    }

    /**
     * token 过期时间
     *
     * @return
     */
    public    Date generateExpirationDate() {

        return new Date(System.currentTimeMillis() + this.expiration * 1000);
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    public    Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 从 token 中拿到 username
     *
     * @param token
     * @return
     */
    public    String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 解析 token 的主体 Claims
     *
     * @param token
     * @return
     */
    public   Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.secret.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 检查 token 是否处于有效期内
     * @param token
     * @param userDetails
     * @return
     */
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        UserDetailImpl user = (UserDetailImpl) userDetails;
//        final String username = this.getUsernameFromToken(token);
//        final Date created = this.getCreatedDateFromToken(token);
//        return (username.equals(user.getUsername()) && !(this.isTokenExpired(token)) && !(this.isCreatedBeforeLastPasswordReset(created, user.getLastPasswordReset())));
//    }

    /**
     * 获得我们封装在 token 中的 token 创建时间
     * @param token
     * @return
     */
    public  Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            created = new Date((Long) claims.get("created"));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 获得我们封装在 token 中的 token 过期时间
     * @param token
     * @return
     */
    public     Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 检查当前时间是否在封装在 token 中的过期时间之后，若是，则判定为 token 过期
     * @param token
     * @return
     */
    public   Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);

        if (expiration==null){//说明已经失效了
            return true;
        }

        return expiration.before(generateCurrentDate());
    }

    /**
     * 检查 token 是否是在最后一次修改密码之前创建的（账号修改密码之后之前生成的 token 即使没过期也判断为无效）
     * @param created
     * @param lastPasswordReset
     * @return
     */
    public  Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }


    public static void main(String[] args) {
        TokenUtils tokenUtils=new TokenUtils();
        System.out.println(tokenUtils.isTokenExpired("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjM0MUBxcS5jb20iLCJjcmVhdGVkIjoxNTMwMjQ2Mzc3NzEzLCJleHAiOjE1MzA4NTExNzd9.952GJKWXpAhMLB6JqfOKbYqPhqPvymsdtUIpzdEav2k50cF7tgJDWY-y-xdKA53bo_gAGjeJ3rYB4sfKFVWAfw"));
    }
}
