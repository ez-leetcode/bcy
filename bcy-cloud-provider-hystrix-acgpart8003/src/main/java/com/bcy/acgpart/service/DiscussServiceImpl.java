package com.bcy.acgpart.service;

import com.bcy.acgpart.mapper.DiscussMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DiscussServiceImpl implements DiscussService{

    @Autowired
    private DiscussMapper discussMapper;

    @Override
    public String deleteDiscuss(List<Long> numbers) {
        int result = discussMapper.deleteBatchIds(numbers);
        if(result == 0){
            log.error("删除讨论失败，讨论不存在");
            return "existWrong";
        }
        log.info("删除讨论成功，共删除：" + result + "条");
        return "success";
    }

}
