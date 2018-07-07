package com.highrock.util;

/**
 *此类用于存放常量
 * @author zhangjinwen
 * @create 2017-11-01 10:56
 * @desc
 **/
public class HRContants {

    /**
     * 缺少请求的body
     */
    public static  final String MISSING_REQUEST_BODY="Missing request body.";

    /**
     * 缺少请求的字段
     */
    public static  final String MISSING="Missing";

    /**
     * 验证email正则表达式
     */
    public static final String  EMAILREGEX="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


    /**
     * 发送邮件的地址
     */
  //  public static final String EMAIL_URL="http://localhost:847/mails/send";
 //   public static final String EMAIL_URL="http://172.16.1.47:4321/mails/send";
    public static final String EMAIL_URL="mails/send";


    /**
     * 跳转重置密码的页面
     */
   // public static final String RESET_PASSWORD_URL="https://stage.hexaoutdoor.com/ecommerce/password-new/";
    //public static final String RESET_PASSWORD_URL="https://stage.hexaoutdoor.com/ecommerce/password-new/";
    public static final String RESET_PASSWORD_URL="http://172.16.1.47:9988/code_newPassword.html?resetToken=";


    /**
     * 重设密码的模板id
     */
    public static final String TEMPLATE_ID_PASSWORD_RESET = "2cb76fc2-2032-4f8b-9c76-40edf4dcd004";


    /**
     * 请求redis获得token
     */
    public static final String REDIS_USER_ID_TOKEN_URL="http://localhost:8082/redis/setLoginToken";


    /**
     * 验证redis验证login token
     */
    public static  final String REDIS_USER_ID_TOKEN_VERIFY_URL="http://localhost:8082/redis/verifyLoginToken";


    public static final Integer CODE200=200;//访问成功

    public static final Integer CODE404=404;//不存在

    public static final Integer CODE405=405;//缺少参数


    public static final  Integer CODE500=500;//证明有误
    public static final  Integer CODE501=501;//first name invalid format
    public static final  Integer CODE502=502;//last name invalid format
    public static final  Integer CODE503=503;//email invalid format
    public static final  Integer CODE504=504;//password invalid format
    public static final  Integer CODE505=505;//phone invalid format
    public static final  Integer CODE506=506;//email exists

    public static final  Integer CODE507=507;//messageSender exists
    public static final  Integer CODE508=508;//messageBody exists






    public static final  Integer CODE600=600;//token超过一小时没操作


    /**
     * 发送邮件的业务编号
     */
    public static final String EMAIL_SERVICE_TYPE_FORGET_PASSWORD="ForgetPassword";



}
