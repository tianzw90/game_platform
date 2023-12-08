package com.platform.dnf.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class DnfFestivalCalculationVo {

    // 原价（总价）
    private BigDecimal totalPrice;

    // 九折卷优惠的价格
    private BigDecimal nineDiscountPrice;

    // 七折卷优惠的价格
    private BigDecimal sevenDiscountPrice;

    // 九折卷总优惠价格
    private BigDecimal nineTotalDiscountPrice;

    // 总优惠价格
    private BigDecimal totalDiscountPrice;

    // 优惠后的总价（需要花费的价格）
    private BigDecimal buyPrice;

    // 所需要的金币
    private BigDecimal buyGold;

}
