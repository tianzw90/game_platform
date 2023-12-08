package com.platform.common.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.component.QueryCondition;
import com.platform.common.comstant.CommonConstant;
import com.platform.common.comstant.MessageConstant;
import com.platform.common.comstant.QueryConstant;
import com.platform.common.utils.CommonUtil;
import com.platform.common.utils.DateUtils;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * @author : wanghaotian
 * @date : 2019-12-20 18:38:40
 * 基础Service
 * 功能：重写mybatisplus中的ServiceImpl实现新增修改删除日志记录
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Resource
//    private BaseTypeService baseTypeService;
//    @Resource
//    private BaseSysLogService baseSysLogService;

    /**
     * 获取save和update等方法需要用到的syslog对象
     * @param entity
     * @param operatetype
     * @param operateresult
     * @return
     */
//    private SysLog getSysLog(T entity, String operatetype, String operateresult){
//        SysLog sysLog = LogUtil.getPartOfSysLog(operatetype, operateresult);
//        //javaBean与JSON格式字符串之间的转换要用到：JSON.toJSONString(obj);
//        //参考https://blog.csdn.net/a18827547638/article/details/80777366
//        sysLog.setLogcontent(JSON.toJSONString(entity));//存实体
//        String operateobject = entity.getClass().getName();
//        sysLog.setOperateobject(operateobject);//操作对象 存实体类名
//        return sysLog;
//    }

    /**
     * 新增时（对数据库进行insert操作时），记录日志
     * @param entity 实体
     * @return boolean
     */
    @Override
    public boolean save(T entity) {
        return super.save(entity);
    }

    /**
     * 根据多个id删除数据
     * @param ids id集合
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteListByIds(String[] ids){
        boolean bol=false;
        for (String id:ids){
            bol=this.deleteById(id);
        }
        return bol;
    }

    /**
     * 更新时（对数据库进行update操作时），记录日志
     * @param entity 实体
     * @return boolean
     */
    @Override
    public boolean updateById(T entity) {
        //记录更新日志
//        SysLog sysLog=getSysLog(entity,"UPDATE","Y");
//        baseSysLogService.insert(sysLog);
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        //先取实体，用于保存日志
        //调用ServiceImpl.class中的getById方法
        T entity = super.getById(id);
        boolean bool = false;
        if(entity!=null){
            //说明存在该对象
            bool = super.removeById(id);
//            if(bool){
                //删除成功，记录日志
//                SysLog sysLog=getSysLog(entity,"DELETE","Y");
//                baseSysLogService.insert(sysLog);
//            }
        }else{
            //删除时资源不存在，抛出异常
            throw new RuntimeException(MessageConstant.NO_RESOURCES+"，请刷新列表后重试。");
        }
        return bool;
    }

    /**
     * 根据id删除数据
     * @param id 主键
     * @return Bool
     */
    @Override
    public boolean deleteById(Serializable id) {
        return this.removeById(id);
    }

    /**
     * 根据查询条件
     * @param wrapper 查询条件
     * @return boolean
     */
    @Override
    public boolean remove(Wrapper<T> wrapper) {
        //TODO 批量删除 记录日志
        return super.remove(wrapper);
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        // TODO 记录日志
        return super.saveOrUpdate(entity);
    }


    /**
     * 根据前台条件查询出带有分页的列表
     * @param qw 查询条件
     * @param condition 分页组装器
     * @return IPage
     */
    @Override
    public IPage<T> pageList(QueryWrapper<T> qw, QueryCondition condition){
        //当前页码和一页需要多少数据
        Page<T> page =this.generatePage(condition);
        qw=this.generateWrapper(qw,condition);
        logger.debug("查看"+qw+condition);
        return super.page(page,qw);
    }

    /**
     * 生成页码和当前页数
     * @param condition 分页组装器
     * @return Page
     */
    @Override
    public Page<T> generatePage(QueryCondition condition){
        Integer current=1;
        Integer size=20;
        if (condition.getPage()!=null){
            current=condition.getPage();
        }
        if (condition.getPageSize()!=null){
            size=condition.getPageSize();
        }
        return  new Page<>(current,size);
    }

    /**
     * TODO 完善排序
     * 拼接查询条件
     * @param qw 查询条件
     * @param condition 分页组装其
     * @return query
     */
    @Override
    public QueryWrapper<T> generateWrapper(QueryWrapper<T> qw, QueryCondition condition){
        String filter = condition.getFilter();
        String sortBy = condition.getSorter();
        if (!ObjectUtils.isEmpty(sortBy)){
            String[] sortByArr = sortBy.split("=");
            if (sortByArr.length!=2){
                throw new RuntimeException("传入排序条件不正确，请联系管理员。");
            }
            String fieldName=sortByArr[0];
            fieldName= CommonUtil.humpToLine(fieldName);
            //校验是否有特殊字符 字段允许有下划线
            if (!CommonUtil.validateLegalString(fieldName,null)) {
                throw new RuntimeException("传入排序条件不正确，请联系管理员");
            }
            String sorter=sortByArr[1];
            if (CommonConstant.ORDER_DESC.equalsIgnoreCase(sorter)){
                qw.orderByDesc(fieldName);
            }else {
                qw.orderByAsc(fieldName);
            }
        }
        if (StringUtils.isEmpty(filter)){
           return qw;
        }
        String[] filterArr = filter.split("&");
        //TODO 解析各个类型的查询条件
        for (String field:filterArr ){
            if(field.equals("")){
                continue;
            }
            String[] fieldArr = field.split("=");
            String fieldName =fieldArr[0];
            //驼峰命名转下划线
            fieldName= CommonUtil.humpToLine(fieldName);
            if (fieldArr.length==1){
                // 只有字段没有值

                //某个字段为空
                if (fieldName.endsWith(QueryConstant.ISNULL)){
                    fieldName=fieldName.replace(QueryConstant.ISNULL,"").trim();
                    qw.isNull(fieldName);
                    //qw.or();
                    //qw.eq(fieldName,"");
                }
                //某个字段不为空
                else if(fieldName.endsWith(QueryConstant.ISNOTNULL)){
                    fieldName=fieldName.replace(QueryConstant.ISNOTNULL,"").trim();
                    qw.isNotNull(fieldName);
                }

            }
            else if (fieldArr.length==2){
                String fieldValue= fieldArr[1];
                //某个字段为空
                if (fieldName.endsWith(QueryConstant.BETWEEN_BEGIN)){
                    Object beginValue=null;
                    Object endValue = null;
                    fieldName=fieldName.replace(QueryConstant.BETWEEN_BEGIN,"").trim();
                    SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    int index = CommonUtil.getStrArrIndex(filterArr,fieldName+ QueryConstant.BETWEEN_END);
                    if (index>-1){
                        //有值
                        String endFiled = filterArr[index];
                        String[] endFiledArr = endFiled.split("=");
                        if (endFiledArr.length==2){
                            endValue=endFiledArr[1];
                            endValue= DateUtils.str2Date(endValue.toString(), datetimeFormat);
                        }

                    }//TODO 异常有begin没有end

                    if (fieldValue.contains(":")){//说明是时间范围
                        beginValue = DateUtils.str2Date(fieldValue, datetimeFormat);

                    }else{//说明是其他范围
                        beginValue=fieldValue;
                    }
                    qw.between(fieldName,beginValue,endValue);
                }
                else if (fieldName.endsWith(QueryConstant.EQ)){
                    fieldName=fieldName.replace(QueryConstant.EQ,"").trim();
                    qw.eq(fieldName,fieldValue);
                }
                else if (fieldName.endsWith(QueryConstant.NE)){
                    fieldName=fieldName.replace(QueryConstant.NE,"").trim();
                    qw.ne(fieldName,fieldValue);
                }
                else if (fieldName.endsWith(QueryConstant.IN)){
                    fieldName=fieldName.replace(QueryConstant.IN,"").trim();
                    qw.in(fieldName, (Object) fieldValue.split(","));
                }
                else if (fieldName.endsWith(QueryConstant.NOTIN)){
                    fieldName=fieldName.replace(QueryConstant.NOTIN,"").trim();
                    qw.notIn(fieldName, (Object) fieldValue.split(","));
                }else if (fieldName.endsWith(QueryConstant.BETWEEN_END)){
                }else if (fieldName.endsWith(QueryConstant.OR)){
                  // 或查询 主要是给手机APP查询用
                    fieldName=fieldName.replace(QueryConstant.OR,"").trim();
                    qw.or();
                    qw.like(fieldName,"%"+fieldValue+"%");
                }else if (fieldName.indexOf(QueryConstant.ANDOR)>-1){//入参格式是
//                    id_andor_name=abd_andor_lisi  QueryWrapper qw
                    String[] colName =fieldName.split("_andor_");
                    String[] colValue =fieldValue.split("_andor_");
                    //1个的时候 没必要用 andor 直接or 就行 ，这里最多是7个 这个必须要求 方法里有形参 QueryWrapper<T>
                   /* if(colName.length==2){
                        qw.and(wrapper -> wrapper.like(colName[0], colvalue[0]).or().like(colName[1], colvalue[1]));
                    }else if(colName.length==3){
                        qw.and(wrapper -> wrapper.like(colName[0], colvalue[0]).or().like(colName[1], colvalue[1]).or().like(colName[2], colvalue[2]));
                    }else if(colName.length==4){
                        qw.and(wrapper -> wrapper.like(colName[0], colvalue[0]).or().like(colName[1], colvalue[1])
                                .or().like(colName[2], colvalue[2]).or().like(colName[3], colvalue[3])
                        );
                    }else if(colName.length==5){
                        qw.and(wrapper -> wrapper.like(colName[0], colvalue[0]).or().like(colName[1], colvalue[1])
                                .or().like(colName[2], colvalue[2]).or().like(colName[3], colvalue[3])
                                .or().like(colName[4], colvalue[4])
                        );
                    }else if(colName.length==6){
                        qw.and(wrapper -> wrapper.like(colName[0], colvalue[0]).or().like(colName[1], colvalue[1])
                                .or().like(colName[2], colvalue[2]).or().like(colName[3], colvalue[3])
                                .or().like(colName[4], colvalue[4]).or().like(colName[5], colvalue[5])
                        );
                    }else if(colName.length>=7){
                        qw.and(wrapper -> wrapper.like(colName[0], colvalue[0]).or().like(colName[1], colvalue[1])
                                .or().like(colName[2], colvalue[2]).or().like(colName[3], colvalue[3])
                                .or().like(colName[4], colvalue[4]).or().like(colName[5], colvalue[5])
                                .or().like(colName[6], colvalue[6])
                        );
                    }*/
                    // 测试apply 写法   这个必须保证前台的字段和数据库字段是一致的才行
                   StringBuilder tempsql= new StringBuilder();
                   for(int i=0;i<colName.length;i++){
                       if(i==0){
                           tempsql = new StringBuilder(colName[i] + " like '%" + colValue[i] + "%'");
                       }else{
                           tempsql.append(" or ").append(colName[i]).append(" like '%").append(colValue[i]).append("%'");
                       }
                    }
                   if(tempsql.length()>0){
                       qw.apply("  ( "+tempsql+")");
                   }
                }
                else {

                    qw.like(fieldName,fieldValue);
                }
            }else{
                // TODO 抛出异常
                throw new RuntimeException("传入查询条件不正确");
            }
        }

        //组装查询条件
        return qw;
    }

//    /**
//     * 获取下拉枚举值
//     * @param kv 结构为 xx=XXX_XXX;xxx=X_
//     * @return List<Map<String,Object>>
//     */
//    @Override
//    public List<Map<String,Object>> getDicts(String kv){
//        return baseTypeService.getDicts(kv);
//    }

//    /**
//     * 传入下拉代码，获取下拉枚举值的map对象， key是枚举值的code，value是枚举汉字
//     *
//     * @param kv 属性名称=下拉代码;
//     * @return 返回多个 下拉的 下拉项map 减少使用时 for循环带来的效率低问题
//     */
//    @Override
//    public Map<String, Map<String, String>> getDictsMapByCode(String kv) {
//        return baseTypeService.getSysTypeList(kv, false);
//    }
//
//    /**
//     * 获取下拉枚举值 目前只支持枚举类型
//     * @param kv
//     * @param param  特殊参数 如 下拉代码做主键，附加sql做值
//     * @return
//     */
//    @Override
//    public List<Map<String,Object>> getDictsWithParam(String kv,Map<String,String> param){
//        return baseTypeService.getDictsWithParam(kv,param);
//    }

}
