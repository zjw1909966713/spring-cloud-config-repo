package com.highrock.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    private static Logger logger = LoggerFactory.getLogger(TokenUtil.class);


    // 创建token
    public static String creatToken(String username) throws IllegalArgumentException, UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        map.put("operateLastTime", System.currentTimeMillis());
        String token = JWT.create().withHeader(map).withClaim("username", username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)).sign(algorithm);//设置过期时间为一个星期
        return token;
    }

    // 验证jwt
    public static DecodedJWT verifyJwt(String token) {
        DecodedJWT decodedJWT = null;
        if (token == null) {
            return decodedJWT;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            decodedJWT = jwtVerifier.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return decodedJWT;
    }


    public static DecodedJWT parseJWT(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        JWTParser jwtParser = new JWTParser();
        return decodedJWT;
    }


    public static void main(String[] args) {
        DecodedJWT decodedJWT = verifyJwt("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjM0MUBxcS5jb20iLCJjcmVhdGVkIjoxNTMwMjQ1NTA1MTg4LCJleHAiOjE1MzA4NTAzMDV9.SzYbAHs4l8N3Mu1rHsPibWi8aLhkJYfTjzDraR1X0u5q234JMToz1uY0wjQxHWVp4W6N2m2jDDctSbzs-e2G1Q");
        System.out.println(decodedJWT.getClaims().get("username").toString());
        System.out.println(decodedJWT.getHeader());
    }


}
