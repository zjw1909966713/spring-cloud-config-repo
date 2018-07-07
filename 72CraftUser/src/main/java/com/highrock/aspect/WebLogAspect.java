package com.highrock.aspect;

import com.highrock.util.TimeUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

/**
 * 定义一个切面，统一处理web请求的日志
 */
@Aspect
@Component
public class WebLogAspect {
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.highrock.controller.*.*(..))")
     public void WebLog() {
    }


    @Before("WebLog()")
    public void doBefore(JoinPoint joinPoint){

        logger.info("");
        // 接收到请求，记录请求内容

        logger.info("*********************************************request start:"+ TimeUtil.Date2DateStr(new Date(),TimeUtil.DATE_DEFAULT_FORMAT)+"*************************************************************");


        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();


        // 记录下请求内容

        logger.info("URL : " + request.getRequestURL().toString());

        logger.info("HTTP_METHOD : " + request.getMethod());

        logger.info("IP : " + request.getRemoteAddr());

        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature
                ().getName());

        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        //获取所有参数方法一：

        Enumeration<String> enu = request.getParameterNames();

        while (enu.hasMoreElements()) {

            String paraName = enu.nextElement();

           logger.info(paraName + ": " + request.getParameter(paraName));

        }

    }



    @AfterReturning(pointcut = "WebLog()",returning ="retVal" )
    public void doAfterReturning(JoinPoint joinPoint,Object retVal) {
        // 处理完请求，返回内容
        logger.info("*********************************************request end:"+ TimeUtil.Date2DateStr(new Date(),TimeUtil.DATE_DEFAULT_FORMAT)+"***************************************************************");

        logger.info("");
    }





}
