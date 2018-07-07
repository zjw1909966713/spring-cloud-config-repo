package com.highrock.service;

import com.highrock.util.BaseJsonRst;

/**
 * @author zhangjinwen
 * @create 2017-11-21 14:36
 * @desc
 **/
public interface CpUserService {


    /**
     * 注册用户
     *
     * @param data
     * @return
     */
    BaseJsonRst createUser(String data);


    /**
     * 用户登录
     *
     * @param data
     * @return
     */
    BaseJsonRst loginUser(String data);


    /**
     * 根据
     *
     * @param id
     * @return
     */
    BaseJsonRst updateUser(Integer id, String data);


    /**
     * 用户忘记密码，发送邮箱
     *
     * @param data
     * @return
     */
    BaseJsonRst forgetPassword(String data);


    /**
     * 重设密码
     *
     * @param data
     * @return
     */
    BaseJsonRst retPassword(String data);


    /**
     * 联系我们
     *
     * @param data
     * @return
     */
    BaseJsonRst contactUS(String data);

    /**
     * 请求邮箱获取取用户信息
     * @param data
     * @return
     */
    BaseJsonRst getUserInfo(String data);

    /**
     * 获得所有用户
     * @return
     */
    BaseJsonRst getAllUser();
}
