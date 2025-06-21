package com.health.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.health.domain.entity.User;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Date;

//自动填充
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private HttpSession session;

    //插入操作，自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", new Date());
        metaObject.setValue("updateTime", new Date());

        //从session中获取当前登录用户的信息
        User employee = (User) session.getAttribute("SESSION_EMPLOYEE");
        if (employee != null){
            metaObject.setValue("createUser", employee.getId());
            metaObject.setValue("updateUser", employee.getId());
        }
    }

    //更新操作，自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", new Date());

        //从session中获取当前登录用户的信息
        User employee = (User) session.getAttribute("SESSION_EMPLOYEE");
        if (employee != null){
            metaObject.setValue("updateUser", employee.getId());
        }
    }
}