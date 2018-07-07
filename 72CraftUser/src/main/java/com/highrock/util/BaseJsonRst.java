package com.highrock.util;

/**
 *此类用于返回结构
 * @author zhangjinwen
 * @create 2017-11-01 11:05
 * @desc
 **/
public class BaseJsonRst<T> {
    private boolean success;	//成功或失败的标志
    private String message;	//成功或失败的信息
    private T result;	//结果集

    private Integer code;//返回代码

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
