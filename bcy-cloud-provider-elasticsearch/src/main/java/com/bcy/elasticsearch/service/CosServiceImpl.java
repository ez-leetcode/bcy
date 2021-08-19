package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.dto.Ck;
import com.bcy.elasticsearch.dto.CosPlay;
import com.bcy.elasticsearch.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class CosServiceImpl implements CosService{

    @Autowired
    private EsUtils esUtils;

    @Override
    public String test(String indexName) throws IOException {
        /*
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CosPlay cosPlay = null;
        try {
            cosPlay = new CosPlay(1424245114351009794L,1422567470053179394L,"小姐姐真好看哈哈","http://rat-bcy.oss-cn-shenzhen.aliyuncs.com/cosPhoto/ccffd637-b5bc-4d9a-89b7-23e0c7065d2f.jpg#http://rat-bcy.oss-cn-shenzhen.aliyuncs.com/cosPhoto/213bcc62-5b50-49a3-879e-0238f80b0407.jpg#",
                    1,format.parse("2021-08-08 13:44:31"),format.parse("2021-08-08 13:44:31"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

         */
        return "success";
    }

    @Override
    public JSONObject searchCos(String keyword, Integer cnt, Integer page) throws IOException {
        return esUtils.CosSearch(keyword,cnt,page);
    }

}
