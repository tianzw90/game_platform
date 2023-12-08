package com.platform.dnf.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.common.component.QueryCondition;
import com.platform.common.comstant.MessageConstant;
import com.platform.common.response.Result;
import com.platform.common.response.ResultUtils;
import com.platform.dnf.entity.DnfFestivalCalculation;
import com.platform.dnf.service.DnfFestivalCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/dnfFestivalCalculation")
public class DnfFestivalCalculationController {
    @Resource
    private DnfFestivalCalculationService dnfFestivalCalculationService;

    @Operation(summary = "查询单条")
    @GetMapping(value = {"/{id}"})
    public Result get(@PathVariable String id) {
        return ResultUtils.success(dnfFestivalCalculationService.getById(id));
    }

    @Operation(summary = "list列表")
    @GetMapping
    public Result list (QueryCondition queryCondition) {
        Map<String, Object> res = new HashMap<>();
        QueryWrapper<DnfFestivalCalculation> query = new QueryWrapper<>();
        IPage<DnfFestivalCalculation> IPage = dnfFestivalCalculationService.pageList(query, queryCondition);
        res.put("records", IPage.getRecords());
        res.put("current", IPage.getCurrent());
        res.put("total", IPage.getTotal());
        return ResultUtils.success(res);
    }

    @Operation(summary = "保存")
    @PostMapping
    public Result save(@RequestBody DnfFestivalCalculation dnfFestivalCalculation) {
        dnfFestivalCalculationService.saveOrUpdate(dnfFestivalCalculation);
        return ResultUtils.success(MessageConstant.SAVE_SUCCESS, dnfFestivalCalculationService.getById(dnfFestivalCalculation.getId()));
    }


    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody String[] ids) {
        dnfFestivalCalculationService.deleteListByIds(ids);
        return ResultUtils.success(MessageConstant.DELETE_SUCCESS);
    }

    @Operation(summary = "计算节日套")
    @PostMapping("/calculation")
    public Result calculation(@RequestBody DnfFestivalCalculation dnfFestivalCalculation) {
        return dnfFestivalCalculationService.calculation(dnfFestivalCalculation);
    }
}
