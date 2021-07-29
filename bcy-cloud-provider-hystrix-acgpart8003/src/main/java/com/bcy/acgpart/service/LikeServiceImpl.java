package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.acgpart.mapper.LikesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LikeServiceImpl implements LikeService{

    @Autowired
    private LikesMapper likesMapper;

    @Override
    public String addLike(Long id, Long number) {
        likesMapper.s
        return null;
    }

    @Override
    public String deleteLike(Long id, Long number) {
        return null;
    }

    @Override
    public String getLikeStatus(Long id, Long number) {
        return null;
    }

    @Override
    public JSONObject getLikeList(Long id, Long cnt, Long page) {
        return null;
    }
}
