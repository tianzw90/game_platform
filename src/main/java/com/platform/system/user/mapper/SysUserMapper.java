package com.platform.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.system.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查找用户
     * @param userName 用户名
     * */
    @Select("SELECT *  " +
            "FROM SYS_USER " +
            "WHERE USER_NAME = #{userName}")
    public SysUser getByUserName(@Param("userName") String userName);

    /**
     * 根据手机号查找用户
     * @param mobile 手机号
     * */
    @Select("SELECT *  " +
            "FROM SYS_USER " +
            "WHERE MOBILE = #{mobile}")
    public SysUser getByMobile(@Param("mobile") String mobile);

    /**
     * 根据身份证查找用户
     * @param cardNum 身份证号
     * */
    @Select("SELECT *  " +
            "FROM SYS_USER " +
            "WHERE CARD_NUM = #{cardNum}")
    public SysUser getByCardNum(@Param("cardNum") String cardNum);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * */
    @Select("SELECT *  " +
            "FROM SYS_USER " +
            "WHERE EMAIL = #{email}")
    public SysUser getByEmail(@Param("email") String email);

}
