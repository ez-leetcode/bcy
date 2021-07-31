package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;

public interface FavorService {

    JSONObject getFavorList(Long id, String keyword, Long page, Long cnt);

}
