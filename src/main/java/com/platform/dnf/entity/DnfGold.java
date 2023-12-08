package com.platform.dnf.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DnfGold {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    // 金币比例
    private BigDecimal goldRatio;

    // 日期
    private Date dateTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private String createName;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
