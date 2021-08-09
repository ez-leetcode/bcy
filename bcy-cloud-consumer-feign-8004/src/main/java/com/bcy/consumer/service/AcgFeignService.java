package com.bcy.consumer.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@FeignClient(value = "BCY-CLOUD-HYSTRIX-ACGPART")
public interface AcgFeignService {

    @PostMapping("/acg/circlePhoto")
    Result<JSONObject> circlePhotoUpload(@RequestParam("photo") MultipartFile file, @RequestParam("id") Long id);

    @PostMapping("/acg/circle")
    Result<JSONObject> createCircle(@RequestParam("id") Long id, @RequestParam("circleName") String circleName,
                                    @RequestParam("description") String description,
                                    @RequestParam("photo") String photo);
    @GetMapping("/acg/circle")
    Result<JSONObject> getCircleInfo(@RequestParam("circleName") String circleName);

    @PostMapping("/acg/followCircle")
    Result<JSONObject> followCircle(@RequestParam("id") Long id, @RequestParam("circleName") String circleName);

    @DeleteMapping("/acg/followCircle")
    Result<JSONObject> disFollowCircle(@RequestParam("id") Long id, @RequestParam("circleName") String circleName);

    @GetMapping("/acg/personalCircle")
    Result<JSONObject> getPersonalCircle(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                                @RequestParam("page") Long page);
    @DeleteMapping("/acg/cos")
    Result<JSONObject> deleteCos(@RequestParam("numbers")List<Long> numbers);

    @GetMapping("/acg/cosCountsList")
    Result<JSONObject> getCosCountsList(@RequestParam(value = "id",required = false) Long id,@RequestParam("number") List<Long> number);

    @GetMapping("/acg/cosCommentCountsList")
    Result<JSONObject> getCosCommentCountsList(@RequestParam(value = "id",required = false) Long id,
                                                      @RequestParam("number") List<Long> number);


    @GetMapping("/acg/judgeLikes")
    Result<JSONObject> judgeLikes(@RequestParam("id") Long id, @RequestParam("numbers") List<Long> numbers);

    @PostMapping("/acg/cos")
    Result<JSONObject> createCos(@RequestParam("id") Long id, @RequestParam("description") String description,
                                        @RequestParam("photo") List<String> photo,@RequestParam("label") List<String> label);
    @GetMapping("/acg/cos")
    Result<JSONObject> getCosTopic(@RequestParam(value = "id",required = false) Long id,@RequestParam("number") Long number);

    @GetMapping("/acg/cosComment")
    Result<JSONObject> getCosComment(@RequestParam(value = "id",required = false) Long id,
                                            @RequestParam("number") Long number, @RequestParam("page") Long page,
                                            @RequestParam("cnt") Long cnt,@RequestParam("type") Integer type);

    @PostMapping("/acg/cosComment")
    Result<JSONObject> addCosComment(@RequestParam("id") Long id,@RequestParam("cosNumber") Long cosNumber,
                                            @RequestParam("description") String description,
                                            @RequestParam(value = "fatherNumber",required = false) Long fatherNumber,
                                            @RequestParam(value = "toId",required = false) Long toId,
                                            @RequestParam(value = "reply",required = false) Long replyNumber);

    @GetMapping("/acg/cosCommentComment")
    Result<JSONObject> getCosCommentComment(@RequestParam(value = "id",required = false) Long id,
                                                   @RequestParam("number") Long number,@RequestParam("cnt") Long cnt,
                                                   @RequestParam("page") Long page,@RequestParam("type") Integer type);

    @PostMapping("/acg/likeCosComment")
    Result<JSONObject> likeCosComment(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @DeleteMapping("/acg/likeCosComment")
    Result<JSONObject> dislikeCosComment(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @PostMapping("/acg/like")
    Result<JSONObject> likeDiscuss(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @PostMapping("/acg/dislike")
    Result<JSONObject> dislikeDiscuss(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @GetMapping("/acg/followQAList")
    Result<JSONObject> getFollowQAList(@RequestParam("id") Long id,@RequestParam("number") Long number,
                                              @RequestParam("cnt") Long cnt,@RequestParam("page") Long page,
                                              @RequestParam("keyword") String keyword);

    @DeleteMapping("/acg/dislikeComment")
    Result<JSONObject> dislikeComment(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @PostMapping("/acg/likeComment")
    Result<JSONObject> likeComment(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @PostMapping("/acg/cosPhotoUpload")
    Result<JSONObject> photoUpload(@RequestParam("photo") MultipartFile file);

    @GetMapping("/acg/likeList")
    Result<JSONObject> getLikeList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                          @RequestParam("page") Long page);

    @PostMapping("/acg/likeCos")
    Result<JSONObject> likeCos(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @DeleteMapping("/acg/likeCos")
    Result<JSONObject> deleteLikeCos(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @GetMapping("/acg/favorList")
    Result<JSONObject> getFavorList(@RequestParam("id") Long id,
                                           @RequestParam("page") Long page,@RequestParam("cnt") Long cnt);

    @DeleteMapping("/acg/dislikeAnswer")
    Result<JSONObject> dislikeAnswer(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @DeleteMapping("/acg/followQA")
    Result<JSONObject> disFollowQA(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @PostMapping("/acg/followQA")
    Result<JSONObject> followQA(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @GetMapping("/acg/qaTopic")
    Result<JSONObject> getQaTopic(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @PostMapping("/acg/QA")
    Result<JSONObject> generateQA(@RequestParam("id") Long id, @RequestParam("title") String title,
                                  @RequestParam("description") String description, @RequestParam("photo")List<String> photo);

    @PostMapping("/acg/favorCos")
    Result<JSONObject> addFavorCos(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @DeleteMapping("/acg/favorCos")
    Result<JSONObject> deleteFavorCos(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @PostMapping("/acg/judgeFavor")
    Result<JSONObject> judgeFavor(@RequestParam("id") Long id, @RequestParam("number")List<Long> number);


    @PostMapping("/acg/likeAnswer")
    Result<JSONObject> likeAnswer(@RequestParam("id") Long id,@RequestParam("number") Long number);

    @GetMapping("/acg/answerList")
    Result<JSONObject> getAnswerList(@RequestParam(value = "id",required = false) Long id,
                                            @RequestParam("number") Long number,@RequestParam("type") Integer type,
                                            @RequestParam("cnt") Long cnt,@RequestParam("page") Long page);

    @GetMapping("/acg/answerCommentList")
    Result<JSONObject> getAnswerCommentList(@RequestParam(value = "id",required = false) Long id,
                                                   @RequestParam("answerNumber") Long answerNumber,
                                                   @RequestParam("cnt") Long cnt ,@RequestParam("page") Long page,
                                                   @RequestParam("type") Integer type);

    @GetMapping("/acg/answerCommentCommentList")
    Result<JSONObject> getAnswerCommentCommentList(@RequestParam(value = "id",required = false) Long id,
                                                          @RequestParam("number") Long number,
                                                          @RequestParam("cnt") Long cnt,@RequestParam("page") Long page,
                                                          @RequestParam("type") Integer type);

}
