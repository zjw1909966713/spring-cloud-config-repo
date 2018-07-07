package com.highrock.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.highrock.mapper.CpUserMapper;
import com.highrock.util.BaseJsonRst;
import com.highrock.util.HRContants;
import com.highrock.util.StringUtil;
import com.highrock.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private TokenUtils tokenUtils;


    @Autowired
    private CpUserMapper cpUserMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
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
            return false;
        }

        boolean tokenExpired= tokenUtils.isTokenExpired(token);//检验token是否





        boolean flag=true;
        logger.info("======================"+tokenExpired);

        if ( !tokenExpired) {
            logger.info("==============="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tokenUtils.getExpirationDateFromToken(token)));
            logger.info("==============="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( tokenUtils.getCreatedDateFromToken(token)));

            Date lastOperateDate=tokenUtils.getCreatedDateFromToken(token);

            String username=tokenUtils.getUsernameFromToken(token);

            boolean isBeyondOneHour= lastOperateDate.before(new Date(System.currentTimeMillis()-60*60*1000));//判断是否超过一小时，true为超过，false为没有
            // boolean isBeyondOneHour= lastOperateDate.before(new Date(System.currentTimeMillis()-60*1000));//判断是否超过一小时，true为超过，false为没有


            if (isBeyondOneHour){
                //需要重新返回token

                BaseJsonRst ret = new BaseJsonRst();
                ret.setSuccess(false);
                ret.setMessage("token shoud be refresh");
                ret.setCode(HRContants.CODE600);

                JSONObject jsonObject=new JSONObject();
                String newToken=tokenUtils.generateToken(username);
                jsonObject.put("token",newToken);
                ret.setResult(jsonObject);
                response.getWriter().print(JSON.toJSON(ret));
            }

        } else {
            BaseJsonRst ret = new BaseJsonRst();
            ret.setSuccess(false);
            ret.setMessage("token error or expired");
            ret.setCode(HRContants.CODE500);
            response.getWriter().print(JSON.toJSON(ret));
            flag=false;
        }

        return flag;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        logger.info("===================拦截器post====================");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.info("===================拦截器end====================");
    }
}
