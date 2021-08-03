package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface QAService {

    String followQA(Long id,Long number);

    String disFollowQA(Long id,Long number);

    String photoUpload(MultipartFile file);

    String likeAnswer(Long id,Long number);

    String dislikeAnswer(Long id,Long number);

    String likeComment(Long id,Long number);

    String dislikeComment(Long id,Long number);

    JSONObject getFollowQAList(Long id,Long number,String keyword,Long page,Long cnt);

    JSONObject getQATopic(Long id,Long number);
}
