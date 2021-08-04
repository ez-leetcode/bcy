package com.bcy.acgpart.service;

import com.bcy.acgpart.mapper.CosMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CosServiceImpl implements CosService {

    @Autowired
    private CosMapper cosMapper;

    @Override
    public String deleteCos(List<Long> numbers) {
        int result = cosMapper.deleteBatchIds(numbers);
        if(result == 0){
            log.error("删除cos失败，cos不存在");
            return "existWrong";
        }
        log.info("删除cos成功，共删除：" + result + "条");
        return "success";
    }

}
