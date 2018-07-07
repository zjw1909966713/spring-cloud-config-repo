package com.highrock.mapper;

import com.highrock.model.CpUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户Mapper操作
 * @author zhangjinwen
 * @create 2017-11-21 14:05
 * @desc
 **/
@Repository
public interface CpUserMapper {


    /**
     * 保存用户
     * @param cpUser
     * @return
     */
    @Insert("insert into cp_user(email,password,first_name,last_name,phone,create_time,type,lock_time,error_times,status,store_no) values (#{email},#{password},#{firstName},#{lastName},#{phone},#{createTime},#{type},#{lockTime},#{errorTimes},#{status},#{storeNo})")
    @Options(useGeneratedKeys = true,keyColumn ="id",keyProperty = "id")
    boolean saveCpUser(CpUser cpUser);

    /**
     * 根据email找到用户
     * @param email
     * @return
     */
    @Select("select * from cp_user where email=#{email}")
    CpUser getCpUserByEmail(@Param("email") String email);

    /**
     * 通过id更新用户
     * @param cpUser
     */
    boolean updateCpUserById(CpUser cpUser);


    /**
     * 根据id找到用户
     * @param id
     * @return
     */
    @Select("select * from cp_user where id=#{id}")
    CpUser getCpUserById(@Param("id") Integer id);


    /**
     * 获得所有用户
     * @return
     */
    @Select("select * from cp_user ORDER BY id")
    List<CpUser> getAllUser();



}
