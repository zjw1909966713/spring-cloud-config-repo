package com.highrock.controller;

import com.highrock.service.CpUserService;
import com.highrock.util.BaseJsonRst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CpUserService cpUserService;


    /**
     * 用户登录
     * @param data {"email":"fadsfa@qq.com","password":"123456"}
     * @return
     */
    @RequestMapping(value = "/sessions",method = RequestMethod.POST)
    public BaseJsonRst loginUser(@RequestBody String data){
        logger.info("==================请求登录接口 ============================");
        BaseJsonRst ret=cpUserService.loginUser(data);
       return ret;

    }




    /**
     * 注册用户
     * @param data
     * {"email":"fadsfa@qq.com","firstName":"aaa","lastName":"1546","phone":"1324567895","password":"123456"}
     * @return
     */
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public BaseJsonRst createUser(@RequestBody String data){
        BaseJsonRst ret=cpUserService.createUser(data);
        return  ret;
    }


    /**
     * 修改用户接口
     * @param id 用户id
     * @param data  {"email":"12341@qq.com","firstName":"zhang","lastName":"jin1","phone":"1562063557","password":"123456"}  password 是非必须的
     * @return
     */
    @RequestMapping(value = "/session/{id}",method = RequestMethod.PUT)
    public BaseJsonRst updateUser(@PathVariable Integer id, @RequestBody String data){
        BaseJsonRst ret=cpUserService.updateUser(id,data);
        return ret;
    }

    /**
     * 用户忘记密码，发送邮件接口
     * @param data  {"email":"jinwen_zhang@highrock.com.cn"}
     * @return
     */
    @RequestMapping(value = "/password",method = RequestMethod.POST)
    public BaseJsonRst forgetPassword(@RequestBody String data){
        BaseJsonRst ret=cpUserService.forgetPassword(data);
        return ret;
    }


    /**
     * 重设密码
     * @param data  {"password":"123456","token":"b6f82acfe49f8fce903947750c23b4c28c237039"}
     * @return
     */
    @PostMapping("/password/reset")
    public BaseJsonRst retPassword(@RequestBody String data){
        BaseJsonRst ret=cpUserService.retPassword(data);
        return  ret;
    }


    /**
     * 联系我们
     * @param data  {"messageEmail":"zjw1909966713@163.com","messageSender":"aaaaaaa","messageBody":"aaaaaa111111"}
     * @return
     */
    @PostMapping("/contact")
    public BaseJsonRst contactUS(@RequestBody String data){
        logger.info("==================请求联系我们接口==========================");
        BaseJsonRst ret=cpUserService.contactUS(data);
        return ret;
    }


    /**
     * 请求邮箱获取取用户信息
     * @param data  {"email":"13456@qq.com"}
     * @return
     */
    @PostMapping("/email")
    public BaseJsonRst getUserInfo(@RequestBody String data){
        logger.info("==================请求邮箱获取取用户信息========================");
        BaseJsonRst ret=cpUserService.getUserInfo(data);
        return ret;
    }


    /**
     * 获得所有用户
     * @return
     */
    @PostMapping("/alluser")
    public BaseJsonRst getAllUser(){
        logger.info("==================获得所有用户=======================");
        BaseJsonRst ret=cpUserService.getAllUser();
        return ret;

    }




}
