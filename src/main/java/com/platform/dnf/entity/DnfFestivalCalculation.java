package com.platform.dnf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;

@Data
public class DnfFestivalCalculation {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    // 节日套id
    private String festivalId;

    // 金币id
    private String goldId;

    // 购买数量
    private Integer buyNum;

    // 九折卷数量
    private Integer nineDiscountNum;

    // 是否持有拍卖行优惠卷
    private Integer isAuction;

    // 节日套价格
    @TableField(exist = false)
    private BigDecimal festivalPrice;

    // 金币比例
    @TableField(exist = false)
    private BigDecimal goldRatio;
}
