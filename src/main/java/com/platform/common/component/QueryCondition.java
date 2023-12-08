package com.platform.common.component;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Data
public class QueryCondition {
    private Integer page;
    private Integer pageSize;
    private String sorter;
    private String filter;
    /**
     * 附加信息 这些信息不直接参与查询条件，需要转换处理
     * 多个字段可以属性名做key  值做value 转成json 格式
     * 前台示例：
     *  var otherFilter={'curTodoTaskId':_self.taskId,'busiId':_self.busiId,'nextTacheId':_self.nextTacheId}//
     *       var jsonStr = JSON.stringify(otherFilter);
     *       _self.defaultSearchSysUser.params.otherFilter=jsonStr;
     * 单个字段值 可以直接传 根据需要进行转换
     */
    private String otherFilter;

    private static final Logger logger= LoggerFactory.getLogger(QueryCondition.class);
    /**
     * 是否已经解码了，只能解码一次，如果查询条件中包含% 多次解码会报错 #1993
     */
    private boolean isDecode;

    /**
     * 前台调用的是这个构造器 默认都是未解码的，
     * 后台手动新增的QueryCondition或者网站前台发过来的请求 @RequestBody QueryCondition  都要单独补充代码设置为true 避免解码;
     */
    public QueryCondition(){
        this.isDecode=false;
    }

    /**
     * 将json格式的字符转换成map
     * @return
     */
    public Map getOtherFilterJson(){
        if(!StringUtils.isEmpty(this.otherFilter)){
            return  (Map) JSONObject.parse(this.otherFilter);
        }else{
            return null;
        }
    }

    /**
     * 重写getFilter 处理filter中的 特殊字符 避免乱码 #1993
     * @return
     */
    public String getFilter(){
        String filterNew="";
        if(!StringUtils.isEmpty(this.filter)){
            if(isDecode) {//已经经过解码 直接使用
                return this.filter;
            }else{//没有经过解码
                try {
                    filterNew = this.filter.replaceAll("%(?![0-9a-fA-F]{2})", "%25");//避免 输入的过滤条件中有 %的情况 正是因为单个% 导致无法多次解码
                    filterNew = URLDecoder.decode(this.filter, "UTF-8");
                    this.filter = filterNew;
                    this.isDecode=true;
                } catch (UnsupportedEncodingException e) {
                    logger.error("前端传递条件解码出现问题");
                    e.printStackTrace();
                }
            }
        }
        return  filterNew;
    }
}
