package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.acgpart.mapper.DiscussMapper;
import com.bcy.acgpart.mapper.LikesMapper;
import com.bcy.acgpart.pojo.Likes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LikeServiceImpl implements LikeService{

    //待会再说，暂时不清楚逻辑

    @Autowired
    private LikesMapper likesMapper;

    @Autowired
    private DiscussMapper discussMapper;

    @Override
    public String addLike(Long id, Long number) {
        QueryWrapper<Likes> wrapper = new QueryWrapper<>();
        wrapper.eq("discuss_number",number)
                .eq("id",id);
        Likes likes = likesMapper.selectOne(wrapper);
        if(likes != null){
            log.error("添加喜欢失败，已添加喜欢");
            return "existWrong";
        }
        likesMapper.insert(new Likes(null,number,id,null));

        return "success";
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
