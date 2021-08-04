package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.acgpart.mapper.FavorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FavorServiceImpl implements FavorService{

    @Autowired
    private FavorMapper favorMapper;

    @Override
    public JSONObject getFavorList(Long id, Long page, Long cnt) {
        return null;
    }


}
