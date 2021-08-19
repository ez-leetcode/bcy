package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CosService {

    String deleteCos(List<Long> numbers);

    String deleteCosByOwner(Long id,Long number);

    JSONObject getCosCountsList(Long id,List<Long> number);

    String cosPhotoUpload(MultipartFile file);

    String generateCos(Long id,String description,List<String> photo,List<String> label,Integer permission);

    JSONObject getCosTopic(Long id,Long number);

    JSONObject getCosComment(Long id,Long number,Long page,Long cnt,Integer type);

    String likeCosComment(Long id,Long number);

    String dislikeCosComment(Long id,Long number);

    JSONObject getCosCommentCountsList(Long id,List<Long> number);

    JSONObject getCosCommentList(Long id,Long number,Long cnt,Long page,Integer type);

    String addComment(Long id,Long cosNumber,String description,Long fatherNumber,Long toId,Long replyNumber);

    JSONObject getRecommendLabelList();

    JSONObject getCosDayHotList(String time);

    JSONObject getCosMonthHotList(String time);

    JSONObject getFollowList(Long id,Long page,Long cnt);

    JSONObject getFollowNoRead(Long id);

    String patchCos(Long id,Long number,String description,List<String> cosPhoto,Integer permission);

}
