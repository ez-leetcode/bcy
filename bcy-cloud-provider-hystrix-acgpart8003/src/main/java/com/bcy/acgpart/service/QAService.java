package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QAService {

    String followQA(Long id,Long number);

    String disFollowQA(Long id,Long number);

    String photoUpload(MultipartFile file);

    String likeAnswer(Long id,Long number);

    String dislikeAnswer(Long id,Long number);

    String likeComment(Long id,Long number);

    String dislikeComment(Long id,Long number);

    String generateQA(Long id, String title, String description, List<String> photo);

    JSONObject getFollowQAList(Long id,Long number,String keyword,Long page,Long cnt);

    JSONObject getQATopic(Long id,Long number);

    JSONObject getQAAnswerList(Long id,Long number,Integer type,Long cnt,Long page);

    JSONObject getAnswerCommentList(Long id,Long answerNumber,Long page,Long cnt,Integer type);

    JSONObject getAnswerCommentCommentList(Long id,Long number,Long page,Long cnt,Integer type);
}
