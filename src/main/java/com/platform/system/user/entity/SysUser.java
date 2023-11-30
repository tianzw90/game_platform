package com.platform.system.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
@Data
public class SysUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    //登录名（英文数字组合）
    private String userName;

    //大小写字母、数字、特殊字符
    private String passWord;

    //真实姓名
    private String realName;

    //性别 1男 0女
    private Integer sex;

    //所属单位组织机构ID
    private String orgId;

    //用户图像
    private String userAvatar;

    //手机号，外部用户必填
    private String mobile;

    //邮箱
    private String email;

    //身份证
    private String cardNum;

    //排序

    private Integer sort;

    //最后一次登陆时间

    private Date lastLoginTime;

    //最后一次登陆IP

    private String lastLoginIp;


    //状态 1激活，2休眠，3注销，4冻结
    private Integer status;

    //加密盐
    private String salt;


    //省份
    private String province;

    //市
    private String city;

    //区
    private String county;


    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private String createName;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE)
    private String updateName;

    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    //删除标记  1逻辑删除，0或NULL 未删除或启用
    private Integer isDelete;

    //删除人登录名
    private String deleteBy;

    //删除人
    private String deleteName;

    //删除时间
    private Date deleteTime;

}
