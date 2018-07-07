package com.highrock.model;

import java.io.Serializable;
import java.util.Date;

/**
 * token 存储
 *
 * @author zhangjinwen
 * @create 2017-11-23 09:02
 * @desc
 **/
public class CpRecoveryToken implements Serializable {
    private Integer id;
    private Integer userId;
    private String token;
    private Date expiration;
    private Integer status;//0.used 1.active

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
