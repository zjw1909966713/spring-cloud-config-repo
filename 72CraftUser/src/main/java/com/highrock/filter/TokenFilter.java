package com.highrock.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.highrock.util.BaseJsonRst;
import com.highrock.util.HRContants;
import com.highrock.util.StringUtil;
import com.highrock.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

//@WebFilter(filterName = "tokenFilter", urlPatterns = {"/password/reset", "/user/session/*"})
public class TokenFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        System.out.println("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE,PATCH");
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Headers","Content-Type,Access-Control-Allow-Headers,Authorization,X-Requested-With,token");
        response.setCharacterEncoding("UTF-8");

        logger.info(path);

        String token = request.getHeader("token");
        if (StringUtil.isBlank(token)){
            //没有token
            BaseJsonRst ret = new BaseJsonRst();
            ret.setSuccess(false);
            ret.setMessage("token error or expired");
            ret.setCode(HRContants.CODE500);
            response.getWriter().print(JSON.toJSON(ret));
            return;
        }


       boolean tokenExpired= tokenUtils.isTokenExpired(token);


       logger.info("======================"+tokenExpired);

        if ( !tokenExpired) {
            logger.info("==============="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tokenUtils.getExpirationDateFromToken(token)));
            logger.info("==============="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( tokenUtils.getCreatedDateFromToken(token)));

            Date lastOperateDate=tokenUtils.getCreatedDateFromToken(token);

            String username=tokenUtils.getUsernameFromToken(token);

           boolean isBeyondOneHour= lastOperateDate.before(new Date(System.currentTimeMillis()-60*60*1000));//判断是否超过一小时，true为超过，false为没有
          // boolean isBeyondOneHour= lastOperateDate.before(new Date(System.currentTimeMillis()-60*1000));//判断是否超过一小时，true为超过，false为没有


           if (isBeyondOneHour){//需要重新返回token

               BaseJsonRst ret = new BaseJsonRst();
               ret.setSuccess(false);
               ret.setMessage("token shoud be refresh");
               ret.setCode(HRContants.CODE600);

               JSONObject jsonObject=new JSONObject();
               String newToken=tokenUtils.generateToken(username);
               jsonObject.put("token",newToken);
               ret.setResult(jsonObject);
               response.getWriter().print(JSON.toJSON(ret));

           }else {

               filterChain.doFilter(servletRequest, servletResponse);
           }


        } else {

            BaseJsonRst ret = new BaseJsonRst();
            ret.setSuccess(false);
            ret.setMessage("token error or expired");
            ret.setCode(HRContants.CODE500);
            response.getWriter().print(JSON.toJSON(ret));
        }


    }

    @Override
    public void destroy() {
        System.out.println("过滤器初始化2");
    }
}
