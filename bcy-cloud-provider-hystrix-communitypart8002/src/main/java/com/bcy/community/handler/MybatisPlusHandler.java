package com.bcy.community.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MybatisPlusHandler implements MetaObjectHandler {

    //插入数据时间更新策略
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("正在插入数据并更新时间字段");
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("orderTime",new Date(),metaObject);
        log.info("插入数据并更新时间字段成功");
    }

    //更新数据时间更新策略
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("正在更新数据并更新时间字段");
        this.setFieldValByName("updateTime",new Date(),metaObject);
        log.info("更新数据并更新时间字段成功");
    }

}
