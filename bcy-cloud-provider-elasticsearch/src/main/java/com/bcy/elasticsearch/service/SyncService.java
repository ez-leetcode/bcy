package com.bcy.elasticsearch.service;

import com.bcy.elasticsearch.dto.CosPlay;
import com.bcy.elasticsearch.dto.Qa;
import com.bcy.elasticsearch.mapper.CosPlayMapper;
import com.bcy.elasticsearch.mapper.QaMapper;
import com.bcy.elasticsearch.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
//用于数据同步
public class SyncService {

    @Autowired
    private CosPlayMapper cosPlayMapper;

    @Autowired
    private QaMapper qaMapper;

    @Autowired
    private EsUtils esUtils;

    //1.cosplay 2.qa
    public void update(Long number,Integer type) throws IOException {
        if(type == 1){
            CosPlay cosPlay = cosPlayMapper.selectById(number);
            if(cosPlay != null){
                //考虑到消息的延迟，可能会被并发删除等操作
                esUtils.insertData(cosPlay,1,number);
            }
        }else if(type == 2){
            Qa qa = qaMapper.selectById(number);
            if(qa != null){
                esUtils.insertData(qa,2,number);
            }
        }
        log.info("更新es数据成功，类型：" + type + " 编号：" + number);
    }

    public void delete(Long number,Integer type) throws IOException{
        esUtils.deleteData(type,number);
        log.info("删除es数据成功，类型：" + type + " 编号：" + number);
    }

}
