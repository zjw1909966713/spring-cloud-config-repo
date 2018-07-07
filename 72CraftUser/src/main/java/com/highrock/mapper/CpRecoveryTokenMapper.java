package com.highrock.mapper;

import com.highrock.model.CpRecoveryToken;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author zhangjinwen
 * @create 2017-11-23 09:12
 * @desc
 **/
@Repository
public interface CpRecoveryTokenMapper {

    /**
     * 保存token
     * @param cpRecoveryToken
     * @return
     */
    @Insert("insert into cp_recovery_token(user_id,token,expiration,status) values (#{userId},#{token},#{expiration},#{status})")
    boolean saveCpRecoveryToken(CpRecoveryToken cpRecoveryToken);


    /**
     * 通过token找到
     * @param token
     * @return
     */
    @Select("select * from cp_recovery_token where token=#{token}")
    CpRecoveryToken getCpRecoveryTokenByToken(@Param("token") String token);


    /**
     * 根据token更新status
     * @param token
     * @param status
     * @return
     */
    @Update("update cp_recovery_token set status=#{status} where token=#{token}")
    boolean UpdateCpRecoveryTokenStausByToken(@Param("token") String token, @Param("status") Integer status);
}
