package com.platform.common.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.platform.common.response.Result;
import com.platform.common.utils.UserUtils;
import com.platform.system.user.entity.SysUser;
import com.platform.system.user.service.SysUserService;
import jakarta.annotation.Resource;
import org.apache.catalina.User;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 结合MyBatisPlus实现新增时给某些字段赋值，修改时给某些字段赋值
 */
@Component
public class MetaHandler implements MetaObjectHandler {
    private final static Logger logger = LoggerFactory.getLogger(MetaHandler.class);

    private Map<String ,String> getAutoFiled(){
        Map<String ,String> map=new HashMap<>();
        SysUser user = UserUtils.getCurrUserInfo();
        String userName=null;
        String realName=null;
        if (user!=null){
            userName=user.getUserName();
            realName=user.getRealName();
        }
        map.put("userName",userName);
        map.put("realName",realName);
        return map;
    }
    @Override
    public void insertFill(MetaObject metaObject) {
        Map<String, String> map = getAutoFiled();
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy", map.get("userName"), metaObject);
        this.setFieldValByName("createName", map.get("realName"), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
    }
}
