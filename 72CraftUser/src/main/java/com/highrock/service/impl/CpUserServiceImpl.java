package com.highrock.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.highrock.mapper.CpRecoveryTokenMapper;
import com.highrock.mapper.CpUserMapper;
import com.highrock.model.CpRecoveryToken;
import com.highrock.model.CpUser;
import com.highrock.service.CpUserService;
import com.highrock.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author zhangjinwen
 * @create 2017-11-21 14:37
 * @desc
 **/
@Service
public class CpUserServiceImpl implements CpUserService {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //  @Autowired
    //  private CpUserMapper cpUserMapper;//用户mapper

    @Autowired
    private CpUserMapper cpUserMapper;

    @Autowired
    private CpRecoveryTokenMapper cpRecoveryTokenMapper;//tokenmapper

    @Value("${serverAddress}")
    private String serverAddress;


    @Value("${forgetPasswordEmailUrl}")
    private String forgetPasswordEmailUrl;


    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration}")
    private Long expiration;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    @Transactional
    public BaseJsonRst createUser(String data) {

        BaseJsonRst ret = new BaseJsonRst();

        if (StringUtil.isBlank(data)) {//如果为空
            ret.setSuccess(false);
            ret.setMessage(HRContants.MISSING_REQUEST_BODY);
            ret.setCode(HRContants.CODE405);
            return ret;
        }
        Map<String, Object> dataMap = JSONHelper.convertJsonStrToMap(data);//把数据转换为map;

        String[] requiredFields = new String[]{"password", "firstName", "lastName", "email", "phone"};
        String missingRequired = ValidationUtil.checkRequiredFields(requiredFields, dataMap);//检验字段是否存在
        if (missingRequired.length() > 0) {
            ret.setSuccess(false);
            ret.setMessage(missingRequired);
            return ret;
        }

        if (StringUtil.isBlank(dataMap.get("firstName").toString())) {
            ret.setSuccess(false);
            ret.setMessage("firstName invalid format");
            ret.setCode(HRContants.CODE501);
            return ret;
        }

        if (StringUtil.isBlank(dataMap.get("lastName").toString())) {
            ret.setSuccess(false);
            ret.setMessage("lastName invalid format");
            ret.setCode(HRContants.CODE502);
            return ret;
        }


        if (!ValidationUtil.matcher(HRContants.EMAILREGEX, dataMap.get("email").toString())) {
            ret.setSuccess(false);
            ret.setMessage("email invalid format");
            ret.setCode(HRContants.CODE503);
            return ret;
        }

        if (dataMap.get("password").toString().length() < 6) {
            ret.setSuccess(false);
            ret.setMessage("password invalid format");
            ret.setCode(HRContants.CODE504);
            return ret;
        }


        if (dataMap.get("phone").toString().length() != 10) {
            ret.setSuccess(false);
            ret.setMessage("phone invalid format");
            ret.setCode(HRContants.CODE505);
            return ret;
        }


        CpUser existingUser = cpUserMapper.getCpUserByEmail(dataMap.get("email").toString());

        if (existingUser != null) {
            ret.setSuccess(false);
            ret.setMessage("email exists");
            ret.setCode(HRContants.CODE506);
            return ret;
        }


        CpUser cpUser = new CpUser();
        cpUser.setEmail(dataMap.get("email").toString());
        cpUser.setFirstName(dataMap.get("firstName").toString());
        cpUser.setLastName(dataMap.get("lastName").toString());
        //cpUser.setPassword(BCrypt.hashpw(dataMap.get("password").toString(), BCrypt.gensalt(10)));
        cpUser.setPassword(dataMap.get("password").toString());
        cpUser.setCreateTime(new Date());
        cpUser.setPhone(dataMap.get("phone").toString());
        cpUser.setType(1);//// this is a regular user (customer)
        cpUser.setStatus(1);//they are active
        boolean flag = cpUserMapper.saveCpUser(cpUser);

        ret.setSuccess(flag);
        if (!flag) {

            ret.setMessage("save error");
            return ret;

        }

        ret.setMessage("save success");
        cpUser.setPassword("");//把密码清空，返回前台，避免泄露

        JSONObject jsonResult = new JSONObject();
        jsonResult.put("user", cpUser);
        String token = tokenUtils.generateToken(cpUser.getEmail());
        jsonResult.put("token", token);

        ret.setResult(jsonResult);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("subject", "register success");
        // jsonObject.put("template_id",HRContants.TEMPLATE_ID_PASSWORD_RESET);
        jsonObject.put("to_mail", cpUser.getEmail());
        jsonObject.put("to_name", cpUser.getFirstName() + " " + cpUser.getLastName());
        jsonObject.put("content", "Congratulations on your registration");

        logger.info("===================" + jsonObject.toJSONString());


        try {
            HttpHelper.sendPOST(serverAddress + HRContants.EMAIL_URL, jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }


        return ret;
    }

    @Override
    public BaseJsonRst loginUser(String data) {
        BaseJsonRst ret = new BaseJsonRst();

        if (StringUtil.isBlank(data)) {//如果为空
            ret.setSuccess(false);
            ret.setMessage(HRContants.MISSING_REQUEST_BODY);
            return ret;
        }
        Map<String, Object> dataMap = JSONHelper.convertJsonStrToMap(data);//把数据转换为map;

        String[] requiredFields = new String[]{"email", "password"};
        String missingRequired = ValidationUtil.checkRequiredFields(requiredFields, dataMap);//检验字段是否存在
        if (missingRequired.length() > 0) {
            ret.setSuccess(false);
            ret.setMessage(missingRequired);
            return ret;
        }

        CpUser existingUser = cpUserMapper.getCpUserByEmail(dataMap.get("email").toString());

      /*  if (existingUser == null || !BCrypt.checkpw(dataMap.get("password").toString(), existingUser.getPassword())) {
            ret.setSuccess(false);
            ret.setMessage("Log in failed, please try again.");
            return ret;
        }*/

        if (existingUser == null || !dataMap.get("password").toString().equals(existingUser.getPassword())) {
            ret.setSuccess(false);
            ret.setMessage("Log in failed, please try again.");
            return ret;
        }




        ret.setSuccess(true);
        ret.setMessage("login success");
        existingUser.setPassword(null);//把密码清空，返回前台，避免泄露

        JSONObject jsonResult = new JSONObject();
        jsonResult.put("user", existingUser);


        String token = tokenUtils.generateToken(existingUser.getEmail());
        jsonResult.put("token", token);


        ret.setResult(jsonResult);
        return ret;
    }

    @Override
    @Transactional
    public BaseJsonRst updateUser(Integer id, String data) {
        BaseJsonRst ret = new BaseJsonRst();

        if (id == null) {
            ret.setSuccess(false);
            ret.setMessage("missing userId");
            return ret;
        }

        if (StringUtil.isBlank(data)) {//如果为空
            ret.setSuccess(false);
            ret.setMessage(HRContants.MISSING_REQUEST_BODY);
            return ret;
        }
        Map<String, Object> dataMap = JSONHelper.convertJsonStrToMap(data);//把数据转换为map;

        String[] requiredFields = new String[]{"phone", "firstName", "lastName", "email"};
        String missingRequired = ValidationUtil.checkRequiredFields(requiredFields, dataMap);//检验字段是否存在
        if (missingRequired.length() > 0) {
            ret.setSuccess(false);
            ret.setMessage(missingRequired);
            return ret;
        }

        if (StringUtil.isBlank(dataMap.get("firstName").toString())) {
            ret.setMessage("firstName can not be blank");
            ret.setSuccess(false);
            ret.setCode(HRContants.CODE501);
            return ret;
        }

        if (StringUtil.isBlank(dataMap.get("lastName").toString())) {
            ret.setSuccess(false);
            ret.setCode(HRContants.CODE502);
            ret.setMessage("lastName can not be blank");
            return ret;
        }

        if (!ValidationUtil.matcher(HRContants.EMAILREGEX, dataMap.get("email").toString())) {
            ret.setSuccess(false);
            ret.setCode(HRContants.CODE503);
            ret.setMessage("email invalid format");
            return ret;
        }


        CpUser existingUser = cpUserMapper.getCpUserByEmail(dataMap.get("email").toString());


        if (null != existingUser && existingUser.getId().intValue() != id.intValue()) {
            ret.setMessage("email exists");
            ret.setSuccess(false);
            ret.setCode(HRContants.CODE506);
            return ret;
        }


        CpUser cpUser = new CpUser();
        cpUser.setId(id);
        cpUser.setPhone(dataMap.get("phone").toString());
        cpUser.setFirstName(dataMap.get("firstName").toString());
        cpUser.setLastName(dataMap.get("lastName").toString());
        cpUser.setEmail(dataMap.get("email").toString());
        if (dataMap.containsKey("password")) {


            if (dataMap.get("password").toString().length() < 6) {
                ret.setSuccess(false);
                ret.setCode(HRContants.CODE504);
                ret.setMessage("password invalid format");
                return ret;
            } else {

                //cpUser.setPassword(BCrypt.hashpw(dataMap.get("password").toString(), BCrypt.gensalt(10)));
                cpUser.setPassword(dataMap.get("password").toString());
            }

        }
        boolean flag = cpUserMapper.updateCpUserById(cpUser);

        ret.setSuccess(flag);

        if (!flag) {
            ret.setMessage("update error");
            return ret;
        }

        CpUser cpUser1 = cpUserMapper.getCpUserById(id);
        cpUser1.setPassword("");
        ret.setResult(cpUser1);


        return ret;
    }

    @Override
    @Transactional
    public BaseJsonRst forgetPassword(String data) {
        BaseJsonRst ret = new BaseJsonRst();
        logger.info(data);
        if (StringUtil.isBlank(data)) {//如果为空
            ret.setSuccess(false);
            ret.setCode(HRContants.CODE405);
            ret.setMessage(HRContants.MISSING_REQUEST_BODY);
            return ret;
        }
        Map<String, Object> dataMap = JSONHelper.convertJsonStrToMap(data);//把数据转换为map;

        String[] requiredFields = new String[]{"email"};

        String missingRequired = ValidationUtil.checkRequiredFields(requiredFields, dataMap);//检验字段是否存在
        if (missingRequired.length() > 0) {
            ret.setCode(HRContants.CODE405);
            ret.setSuccess(false);
            ret.setMessage(missingRequired);
            return ret;
        }


        CpUser existingUser = cpUserMapper.getCpUserByEmail(dataMap.get("email").toString());

        if (existingUser == null) {
            ret.setSuccess(false);
            ret.setMessage("Email address not found.");
            return ret;
        }

        /**********************保存token到数据库*START********************/
        String resetToken = UUID.randomUUID().toString().replace("-", "");
        CpRecoveryToken cpRecoveryToken = new CpRecoveryToken();
        cpRecoveryToken.setToken(resetToken);
        cpRecoveryToken.setStatus(1);//可以使用的 active
        cpRecoveryToken.setUserId(existingUser.getId());

        Date now = new Date();
        //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, 2);//时间持续2个小时有效

        cpRecoveryToken.setExpiration(cal.getTime());

        cpRecoveryTokenMapper.saveCpRecoveryToken(cpRecoveryToken);
        /**********************保存token到数据库*END********************/


        /**********************发送邮件*START********************/
       // String body = HRContants.RESET_PASSWORD_URL + resetToken;
        String body = forgetPasswordEmailUrl + resetToken;


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serviceType", HRContants.EMAIL_SERVICE_TYPE_FORGET_PASSWORD);
        jsonObject.put("to_mail", existingUser.getEmail());
        jsonObject.put("to_name", existingUser.getFirstName() + " " + existingUser.getLastName());
        jsonObject.put("substitutes_val", body);


        String resStr = null;

        try {
            resStr = HttpHelper.sendPOST(serverAddress + HRContants.EMAIL_URL, jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        /**********************发送邮件*END********************/


        if (!"SUCCESS".equalsIgnoreCase(resStr)) {
            ret.setSuccess(false);
            ret.setCode(HRContants.CODE500);
            ret.setMessage("Failed to send password reset email");
            return ret;
        }

        ret.setSuccess(true);
        ret.setCode(HRContants.CODE200);
        ret.setMessage("success");

        return ret;
    }

    @Override
    @Transactional
    public BaseJsonRst retPassword(String data) {

        BaseJsonRst ret = new BaseJsonRst();

        if (StringUtil.isBlank(data)) {
            ret.setSuccess(false);
            ret.setMessage(HRContants.MISSING_REQUEST_BODY);
            ret.setCode(HRContants.CODE405);
            return ret;
        }
        Map<String, Object> dataMap = JSONHelper.convertJsonStrToMap(data);//把数据转换为map;

        String[] requiredFields = new String[]{"token", "password"};

        String missingRequired = ValidationUtil.checkRequiredFields(requiredFields, dataMap);//检验字段是否存在
        if (missingRequired.length() > 0) {
            ret.setSuccess(false);
            ret.setCode(HRContants.CODE405);
            ret.setMessage(missingRequired);
            return ret;
        }

        CpRecoveryToken cpRecoveryToken = cpRecoveryTokenMapper.getCpRecoveryTokenByToken(dataMap.get("token").toString());

        if (cpRecoveryToken == null) {
            ret.setSuccess(false);
            ret.setMessage("Token does not exist.");
            return ret;
        }

        if (cpRecoveryToken.getStatus() != 1) {
            ret.setSuccess(false);
            ret.setMessage("Token has already been used.");
            return ret;
        }

        Date now = new Date();

        if (cpRecoveryToken.getExpiration().before(now)) {
            ret.setSuccess(false);
            ret.setMessage("Token has expired.");
            return ret;
        }

        /*****************更新密码*START*************************/
        CpUser cpUser = new CpUser();
        cpUser.setId(cpRecoveryToken.getUserId());
        //cpUser.setPassword(BCrypt.hashpw(dataMap.get("password").toString(), BCrypt.gensalt(10)));
        cpUser.setPassword(dataMap.get("password").toString());
        cpUserMapper.updateCpUserById(cpUser);
        /*****************更新密码*END*************************/


        boolean flag = cpRecoveryTokenMapper.UpdateCpRecoveryTokenStausByToken(dataMap.get("token").toString(), 0);

        ret.setSuccess(flag);

        if (!flag) {
            ret.setCode(HRContants.CODE500);
            ret.setMessage("Unable to deactivate token " + dataMap.get("token").toString() + ".");
            return ret;
        } else {
            ret.setCode(HRContants.CODE200);
            ret.setMessage("succeed,please login");
        }

        return ret;
    }

    @Override
    public BaseJsonRst contactUS(String data) {

        BaseJsonRst ret = new BaseJsonRst();

        if (StringUtil.isBlank(data)) {//如果为空
            ret.setSuccess(false);
            ret.setMessage(HRContants.MISSING_REQUEST_BODY);
            return ret;
        }
        Map<String, Object> dataMap = JSONHelper.convertJsonStrToMap(data);//把数据转换为map;

        String[] requiredFields = new String[]{"messageEmail", "messageSender", "messageBody"};

        String missingRequired = ValidationUtil.checkRequiredFields(requiredFields, dataMap);//检验字段是否存在
        if (missingRequired.length() > 0) {
            ret.setSuccess(false);
            ret.setMessage(missingRequired);
            ret.setCode(HRContants.CODE405);
            return ret;
        }


        if (StringUtil.isBlank(dataMap.get("messageEmail").toString())) {
            ret.setSuccess(false);
            ret.setMessage("email invalid format");
            ret.setCode(HRContants.CODE503);
            return ret;
        }

        if (!ValidationUtil.matcher(HRContants.EMAILREGEX, dataMap.get("messageEmail").toString())) {
            ret.setMessage("email invalid format");
            ret.setSuccess(false);
            ret.setCode(HRContants.CODE503);
            return ret;
        }

        if (StringUtil.isBlank(dataMap.get("messageSender").toString())) {
            ret.setSuccess(false);
            ret.setMessage("messageSender  blank");
            ret.setCode(HRContants.CODE507);
            return ret;
        }
        if (StringUtil.isBlank(dataMap.get("messageBody").toString())) {
            ret.setSuccess(false);
            ret.setMessage("messageBody  blank");
            ret.setCode(HRContants.CODE508);
            return ret;
        }

        //发送邮件

        JSONObject jsonObject = new JSONObject();
       // jsonObject.put("subject", "Inquiry from customer " + dataMap.get("messageSender").toString() + "via Contact Us page");

        jsonObject.put("serviceType","ContactUs");
        jsonObject.put("to_mail", "service@hexaoutdoor.com");
        jsonObject.put("to_name", "Hexa Outdoor Customer Support");

        jsonObject.put("reply_mail",dataMap.get("messageEmail").toString());
        jsonObject.put("reply_name",dataMap.get("messageSender").toString());
       // jsonObject.put("content", dataMap.get("messageBody").toString());
        jsonObject.put("substitutes_val", dataMap.get("messageBody").toString());

        logger.info("===================" + jsonObject.toJSONString());

        String sendStr = "";

        try {
            sendStr = HttpHelper.sendPOST(serverAddress + HRContants.EMAIL_URL, jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }


        if (!sendStr.equalsIgnoreCase("SUCCESS")) {

            ret.setSuccess(false);
            ret.setCode(HRContants.CODE500);
            ret.setMessage("send email error");
            return ret;
        }


        ret.setSuccess(true);
        ret.setCode(HRContants.CODE200);
        ret.setMessage("succeed");
        return ret;


    }

    @Override
    public BaseJsonRst getUserInfo(String data) {

        BaseJsonRst ret = new BaseJsonRst();

        if (StringUtil.isBlank(data)) {//如果为空
            ret.setSuccess(false);
            ret.setMessage(HRContants.MISSING_REQUEST_BODY);
            return ret;
        }


        Map<String, Object> dataMap = JSONHelper.convertJsonStrToMap(data);//把数据转换为map;




        if (!dataMap.containsKey("email")||StringUtil.isBlank(data)) {//如果为空
            ret.setSuccess(false);
            ret.setMessage("email is blank");
            return ret;
        }

        String email=dataMap.get("email").toString();

        CpUser existingUser = cpUserMapper.getCpUserByEmail(email);

        if (existingUser==null){
            ret.setSuccess(false);
            ret.setMessage("email not exist");
            return ret;
        }


        existingUser.setPassword(null);

        ret.setSuccess(true);
        ret.setMessage("success");
        ret.setResult(existingUser);
        return ret;
    }

    @Override
    public BaseJsonRst getAllUser() {
        BaseJsonRst ret = new BaseJsonRst();
        ret.setSuccess(true);
        ret.setCode(200);
        ret.setMessage("success");
        List<CpUser> userList=cpUserMapper.getAllUser();

        for (CpUser user:userList) {
            user.setPassword(null);
        }

        ret.setResult(userList);

        return ret;
    }

}
