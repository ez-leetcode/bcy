package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface LikeService {

    String addLike(Long id,Long number);

    String deleteLike(Long id,Long number);

    JSONObject getLikeStatus(Long id, List<Long> number);

    JSONObject getLikeList(Long id,Long cnt,Long page);

}
