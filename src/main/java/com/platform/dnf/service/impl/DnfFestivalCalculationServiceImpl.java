package com.platform.dnf.service.impl;

import com.platform.common.response.Result;
import com.platform.common.response.ResultUtils;
import com.platform.common.service.BaseServiceImpl;
import com.platform.dnf.entity.DnfFestivalCalculation;
import com.platform.dnf.mapper.DnfFestivalCalculationMapper;
import com.platform.dnf.service.DnfFestivalCalculationService;
import com.platform.dnf.vo.DnfFestivalCalculationVo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

@Service
public class DnfFestivalCalculationServiceImpl extends BaseServiceImpl<DnfFestivalCalculationMapper, DnfFestivalCalculation> implements DnfFestivalCalculationService {
    /**
     * 计算节日套
     * @param dnfFestivalCalculation 入参实体
     * */
    @Override
    public Result calculation(DnfFestivalCalculation dnfFestivalCalculation) {
        DnfFestivalCalculationVo vo = new DnfFestivalCalculationVo();
        BigDecimal festivalPrice = dnfFestivalCalculation.getFestivalPrice();
        Integer buyNum = dnfFestivalCalculation.getBuyNum();
        Integer nineDiscountNum = dnfFestivalCalculation.getNineDiscountNum();
        BigDecimal goldRatio = dnfFestivalCalculation.getGoldRatio();
        Integer isAuction = dnfFestivalCalculation.getIsAuction();
        if (ObjectUtils.isEmpty(festivalPrice) || festivalPrice.compareTo(new BigDecimal(0)) < 0) {
            return ResultUtils.fail("节日套价格异常，无法计算。");
        }
        if (ObjectUtils.isEmpty(buyNum) || buyNum < 0) {
            return ResultUtils.fail("购买数量异常，无法计算。");
        }
        if (ObjectUtils.isEmpty(nineDiscountNum) || nineDiscountNum < 0) {
            return ResultUtils.fail("九折卷数量异常，无法计算。");
        }
        // TODO: 人民币计算
        // 总价
        BigDecimal totalPrice = festivalPrice.multiply(BigDecimal.valueOf(buyNum));
        vo.setTotalPrice(totalPrice);
        // 九折优惠的价格
        BigDecimal nineDiscountPrice = festivalPrice.multiply(BigDecimal.valueOf(0.1));
        vo.setNineDiscountPrice(nineDiscountPrice);
        // 最后花费的总价钱
        BigDecimal buyPrice = new BigDecimal(0);
        // 九折卷总优惠
        BigDecimal nineTotalDiscountPrice = festivalPrice.multiply(BigDecimal.valueOf(0.1 * nineDiscountNum));
        vo.setNineTotalDiscountPrice(nineTotalDiscountPrice);
        // 如果买的大于1 才有七折卷
        if (buyNum > 1) {
            // 九折券足够的时候
            if (nineDiscountNum >= buyNum) {
                // 九折购买价格
                BigDecimal nineMoney = festivalPrice.multiply(BigDecimal.valueOf(buyNum - 1)).multiply(BigDecimal.valueOf(0.9));
                // 七折优惠价
                BigDecimal sevenDiscountPrice = festivalPrice.multiply(BigDecimal.valueOf(0.3));
                vo.setSevenDiscountPrice(sevenDiscountPrice);
                // 最后总消费价格
                buyPrice = nineMoney.add(festivalPrice.multiply(BigDecimal.valueOf(0.7)));
            } else {
                // 九折券不够的时候
                BigDecimal nineMoney = nineDiscountPrice.multiply(BigDecimal.valueOf(nineDiscountNum)).multiply(BigDecimal.valueOf(0.9));
                // 七折优惠价
                BigDecimal sevenDiscountPrice = festivalPrice.multiply(BigDecimal.valueOf(0.3));
                // 剩余的节日套数量(没优惠卷的)
                int remainderNum = buyNum - nineDiscountNum - 1;
                BigDecimal remainderMoney = festivalPrice.multiply(BigDecimal.valueOf(remainderNum));
                buyPrice = nineMoney.add(sevenDiscountPrice).add(remainderMoney);
            }
            // 总优惠
            vo.setTotalDiscountPrice(totalPrice.subtract(buyPrice));
            // 购买价格
            vo.setBuyPrice(buyPrice);
            // 转换成金币
            BigDecimal commission = new BigDecimal("1.05");
            if (isAuction == 1) {
                commission = new BigDecimal("1.03");
            }
            BigDecimal goldEvey = goldRatio.multiply(commission);
            BigDecimal totalGold = buyPrice.multiply(goldEvey);
            vo.setBuyGold(totalGold);
        }
        return ResultUtils.success("计算成功。", vo);
    }
}
