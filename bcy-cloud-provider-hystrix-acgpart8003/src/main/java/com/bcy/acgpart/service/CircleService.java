package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface CircleService {

    String circlePhotoUpload(MultipartFile file,Long id);

    String createCircle(String circleName,String description,String photo,String nickName);

    String followCircle(Long id,String circleName);

    String disFollowCircle(Long id,String circleName);

    JSONObject getCircleInfo(String circleName);

    JSONObject getPersonalCircle(Long id,Long cnt,Long page);

    JSONObject searchCircle(Long id,Long cnt,Long page,String keyword);
}
