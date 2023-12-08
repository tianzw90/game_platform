package com.platform.dnf.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.common.component.QueryCondition;
import com.platform.common.comstant.MessageConstant;
import com.platform.common.response.Result;
import com.platform.common.response.ResultUtils;
import com.platform.dnf.entity.DnfFestival;
import com.platform.dnf.service.DnfFestivalService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dnfFestival")
public class DnfFestivalController {

    @Resource
    private DnfFestivalService dnfFestivalService;

    @Operation(summary = "查询单条")
    @GetMapping(value = {"/{id}"})
    public Result get(@PathVariable String id) {
        return ResultUtils.success(dnfFestivalService.getById(id));
    }

    @Operation(summary = "list列表")
    @GetMapping
    public Result list (QueryCondition queryCondition) {
        Map<String, Object> res = new HashMap<>();
        QueryWrapper<DnfFestival> query = new QueryWrapper<>();
        IPage<DnfFestival> IPage = dnfFestivalService.pageList(query, queryCondition);
        res.put("records", IPage.getRecords());
        res.put("current", IPage.getCurrent());
        res.put("total", IPage.getTotal());
        return ResultUtils.success(res);
    }

    @Operation(summary = "保存")
    @PostMapping
    public Result save(@RequestBody DnfFestival dnfFestival) {
        dnfFestivalService.saveOrUpdate(dnfFestival);
        return ResultUtils.success(MessageConstant.SAVE_SUCCESS, dnfFestivalService.getById(dnfFestival.getId()));
    }


    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody String[] ids) {
        dnfFestivalService.deleteListByIds(ids);
        return ResultUtils.success(MessageConstant.DELETE_SUCCESS);
    }
}
