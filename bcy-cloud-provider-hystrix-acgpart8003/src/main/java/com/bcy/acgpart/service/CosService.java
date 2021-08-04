package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CosService {

    String deleteCos(List<Long> numbers);

    JSONObject getCosCountsList(Long id,List<Long> number);

    String cosPhotoUpload(MultipartFile file);

    String generateCos(Long id,String description,List<String> photo,List<String> label);

    JSONObject getCosTopic(Long id,Long number);

    JSONObject getCosComment(Long id,Long number,Long page,Long cnt,Integer type);

    String likeCosComment(Long id,Long number);

    String dislikeCosComment(Long id,Long number);

    JSONObject getCosCommentCountsList(Long id,List<Long> number);
}
