package com.bcy.elasticsearch.service;

import com.bcy.elasticsearch.dto.CosPlay;
import com.bcy.elasticsearch.dto.CosPlayForEs;
import com.bcy.elasticsearch.dto.Qa;
import com.bcy.elasticsearch.dto.QaForEs;
import com.bcy.elasticsearch.mapper.CircleCosMapper;
import com.bcy.elasticsearch.mapper.CosPlayMapper;
import com.bcy.elasticsearch.mapper.QaLabelMapper;
import com.bcy.elasticsearch.mapper.QaMapper;
import com.bcy.elasticsearch.utils.EsUtils;
import com.bcy.utils.PhotoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
//用于数据同步
public class SyncService {

    @Autowired
    private CosPlayMapper cosPlayMapper;

    @Autowired
    private CircleCosMapper circleCosMapper;

    @Autowired
    private QaMapper qaMapper;

    @Autowired
    private QaLabelMapper qaLabelMapper;

    @Autowired
    private EsUtils esUtils;

    //1.cosplay 2.qa
    public void update(Long number,Integer type) throws IOException {
        if(type == 1){
            CosPlay cosPlay = cosPlayMapper.selectById(number);
            List<String> label = circleCosMapper.getAllCircleNameFromCosNumber(number);
            //公开的cos才能搜到
            if(cosPlay != null && cosPlay.getPermission() == 1){
                //考虑到消息的延迟，可能会被并发删除等操作
                CosPlayForEs cosPlayForEs = new CosPlayForEs(cosPlay.getNumber(),cosPlay.getId(),cosPlay.getDescription(), PhotoUtils.photoStringToList(cosPlay.getPhoto()).toString()
                        ,label.toString(),cosPlay.getCreateTime(),cosPlay.getUpdateTime());
                log.info(cosPlayForEs.toString());
                esUtils.insertData(cosPlayForEs,1,number);
            }
        }else if(type == 2){
            Qa qa = qaMapper.selectById(number);
            List<String> label = qaLabelMapper.getAllCircleNameFromQaNumber(number);
            if(qa != null){
                //待修改
                QaForEs qaForEs = new QaForEs(qa.getNumber(),qa.getId(),qa.getTitle(),qa.getDescription(),label.toString(),
                        PhotoUtils.photoStringToList(qa.getPhoto()).toString(),qa.getCreateTime());
                log.info(qaForEs.toString());
                esUtils.insertData(qaForEs,2,number);
            }
        }
        log.info("更新es数据成功，类型：" + type + " 编号：" + number);
    }

    public void delete(Long number,Integer type) throws IOException{
        esUtils.deleteData(type,number);
        log.info("删除es数据成功，类型：" + type + " 编号：" + number);
    }

}
