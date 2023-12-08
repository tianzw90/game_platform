package com.platform.dnf.service;

import com.platform.common.response.Result;
import com.platform.common.service.BaseService;
import com.platform.dnf.entity.DnfFestivalCalculation;

public interface DnfFestivalCalculationService extends BaseService<DnfFestivalCalculation> {
    /**
     * 计算节日套
     * @param dnfFestivalCalculation 入参实体
     * */
    Result calculation(DnfFestivalCalculation dnfFestivalCalculation);
}
