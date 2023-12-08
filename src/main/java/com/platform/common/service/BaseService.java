package com.platform.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.common.component.QueryCondition;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: wanghaotian
 * Date: 2019/11/15
 * Time: 3:15 PM
 */
public interface BaseService<T> extends IService<T>{
//    /**
//     * 获取下拉枚举值 支持枚举和 自定义sql
//     * @param kv
//     * @return
//     */
//    public List<Map<String,Object>> getDicts(String kv);
//
//    /**
//     *  传入下拉代码，获取下拉枚举值的map对象， key是枚举值的code，value是枚举汉字
//     * @param kv  属性名称=下拉代码;
//     * @return 返回多个 下拉的 下拉项map 减少使用时 for循环带来的效率低问题
//     */
//    public Map<String,Map<String,String>> getDictsMapByCode(String kv);
//
//    /**
//     * 获取下拉枚举值 目前只支持枚举类型
//     * @param kv
//     * @param param  特殊参数 如 下拉代码做主键，附加sql做值 sql 不需要加 and 前缀关系
//     *              如  param.put("SYS_MODULE"," typecode='SYS'");
//     * @return
//     */
//    public List<Map<String,Object>> getDictsWithParam(String kv,Map<String,String> param);

    /**
     * 根据多个id删除数据
     * @param ids id集合
     * @return boolean
     */
    public boolean deleteListByIds(String[] ids);
    /**
     * 根据id删除数据
     * @param id 主键
     * @return Bool
     */
    public boolean deleteById(Serializable id);

    public IPage<T> pageList(QueryWrapper<T> qw, QueryCondition condition);
    /**
     * 生成页码和当前页数
     * @param condition 分页组装器
     * @return Page
     */
    public Page<T> generatePage(QueryCondition condition);
    /**
     * TODO 完善排序
     * 拼接查询条件
     * @param qw 查询条件
     * @param condition 分页组装器
     * @return query
     */
    public QueryWrapper<T> generateWrapper(QueryWrapper<T> qw,QueryCondition condition);
}
