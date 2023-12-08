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
public class DnfFestival {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    // 节日套名称
    private String festivalName;

    // 节日套开始时间
    private Date festivalStartDate;

    // 节日套结束时间
    private Date festivalEndDate;

    // 节日套价格
    private BigDecimal festivalPrice;

    // 节日套年份
    private String festivalYear;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private String createName;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



}
