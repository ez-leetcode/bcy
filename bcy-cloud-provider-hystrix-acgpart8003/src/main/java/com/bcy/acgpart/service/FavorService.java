package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface FavorService {

    JSONObject getFavorList(Long id, Long page, Long cnt);

    JSONObject getFavorQaList(Long id,Long page,Long cnt);

    String addFavor(Long id,Long number);

    String deleteFavor(Long id,Long number);

    JSONObject judgeFavor(Long id, List<Long> number);
}
