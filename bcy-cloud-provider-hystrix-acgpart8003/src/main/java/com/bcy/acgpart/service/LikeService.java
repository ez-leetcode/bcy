package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;

public interface LikeService {

    String addLike(Long id,Long number);

    String deleteLike(Long id,Long number);

    String getLikeStatus(Long id,Long number);

    JSONObject getLikeList(Long id,Long cnt,Long page);

}
