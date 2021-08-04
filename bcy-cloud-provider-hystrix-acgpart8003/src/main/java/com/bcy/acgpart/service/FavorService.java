package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;

public interface FavorService {

    JSONObject getFavorList(Long id, Long page, Long cnt);

}
