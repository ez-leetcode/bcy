package com.bcy.consumer.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.consumer.service.AcgFeignService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
public class AcgController {

    @Autowired
    private AcgFeignService acgFeignService;

    @PostMapping("/acg/circlePhoto")
    public Result<JSONObject> circlePhotoUpload(@RequestParam("photo") MultipartFile file, @RequestParam("id") String id){
        return acgFeignService.circlePhotoUpload(file,id);
    }

    @PostMapping("/acg/circle")
    public Result<JSONObject> createCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName,
                                           @RequestParam("description") String description,
                                           @RequestParam("photo") String photo,@RequestParam("nickName") String nickName){
        return acgFeignService.createCircle(id, circleName, description, photo, nickName);
    }

    @GetMapping("/acg/circle")
    public Result<JSONObject> getCircleInfo(@RequestParam("circleName") String circleName){
        return acgFeignService.getCircleInfo(circleName);
    }

    @PostMapping("/acg/followCircle")
    public Result<JSONObject> followCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName){
        return acgFeignService.followCircle(id, circleName);
    }

    @DeleteMapping("/acg/followCircle")
    public Result<JSONObject> disFollowCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName){
        return acgFeignService.disFollowCircle(id, circleName);
    }

    @GetMapping("/acg/personalCircle")
    public Result<JSONObject> getPersonalCircle(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                                @RequestParam("page") Long page){
        return acgFeignService.getPersonalCircle(id, cnt, page);
    }

    @DeleteMapping("/acg/cos")
    public Result<JSONObject> deleteCos(@RequestParam("numbers")List<Long> numbers){
        return acgFeignService.deleteCos(numbers);
    }

    @GetMapping("/acg/cosCountsList")
    public Result<JSONObject> getCosCountsList(@RequestParam(value = "id",required = false) Long id,@RequestParam("number") List<Long> number){
        return acgFeignService.getCosCountsList(id,number);
    }

    @GetMapping("/acg/cosCommentCountsList")
    public Result<JSONObject> getCosCommentCountsList(@RequestParam(value = "id",required = false) Long id,
                                                      @RequestParam("number") List<Long> number){
        return acgFeignService.getCosCommentCountsList(id, number);
    }

    @PostMapping("/acg/cos")
    public Result<JSONObject> createCos(@RequestParam("id") Long id, @RequestParam("description") String description,
                                        @RequestParam("photo") List<String> photo,@RequestParam("label") List<String> label,
                                        @RequestParam("permission") Integer permission){
        return acgFeignService.createCos(id, description, photo, label, permission);
    }

    @GetMapping("/acg/cos")
    public Result<JSONObject> getCosTopic(@RequestParam(value = "id",required = false) Long id,@RequestParam("number") Long number){
        return acgFeignService.getCosTopic(id, number);
    }

    @GetMapping("/acg/cosComment")
    public Result<JSONObject> getCosComment(@RequestParam(value = "id",required = false) Long id,
                                            @RequestParam("number") Long number, @RequestParam("page") Long page,
                                            @RequestParam("cnt") Long cnt,@RequestParam("type") Integer type){
        return acgFeignService.getCosComment(id, number, page, cnt, type);
    }

    @PostMapping("/acg/cosComment")
    public Result<JSONObject> addCosComment(@RequestParam("id") Long id,@RequestParam("cosNumber") Long cosNumber,
                                            @RequestParam("description") String description,
                                            @RequestParam(value = "fatherNumber",required = false) Long fatherNumber,
                                            @RequestParam(value = "toId",required = false) Long toId,
                                            @RequestParam(value = "replyNumber",required = false) Long replyNumber){
        return acgFeignService.addCosComment(id, cosNumber, description, fatherNumber, toId, replyNumber);
    }

    @GetMapping("/acg/cosCommentComment")
    public Result<JSONObject> getCosCommentComment(@RequestParam(value = "id",required = false) Long id,
                                                   @RequestParam("number") Long number,@RequestParam("cnt") Long cnt,
                                                   @RequestParam("page") Long page,@RequestParam("type") Integer type){
        return acgFeignService.getCosCommentComment(id, number, cnt, page, type);
    }

    @PostMapping("/acg/likeCosComment")
    public Result<JSONObject> likeCosComment(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.likeCosComment(id, number);
    }

    @DeleteMapping("/acg/likeCosComment")
    public Result<JSONObject> dislikeCosComment(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.dislikeCosComment(id, number);
    }


    @GetMapping("/acg/judgeLikes")
    public Result<JSONObject> judgeLikes(@RequestParam("id") Long id, @RequestParam("numbers")List<Long> numbers){
        return acgFeignService.judgeLikes(id, numbers);
    }

    @GetMapping("/acg/likeList")
    public Result<JSONObject> getLikeList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                          @RequestParam("page") Long page){
        return acgFeignService.getLikeList(id, cnt, page);
    }

    @PostMapping("/acg/like")
    public Result<JSONObject> likeDiscuss(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.likeDiscuss(id,number);
    }

    @PostMapping("/acg/dislike")
    public Result<JSONObject> dislikeDiscuss(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.dislikeDiscuss(id, number);
    }

    @GetMapping("/acg/followQAList")
    public Result<JSONObject> getFollowQAList(@RequestParam("id") Long id,@RequestParam("number") Long number,
                                              @RequestParam("cnt") Long cnt,@RequestParam("page") Long page,
                                              @RequestParam("keyword") String keyword) {
        return acgFeignService.getFollowQAList(id, number, cnt, page, keyword);
    }

    @DeleteMapping("/acg/dislikeComment")
    public Result<JSONObject> dislikeComment(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.dislikeComment(id, number);
    }

    @PostMapping("/acg/likeComment")
    public Result<JSONObject> likeComment(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.likeComment(id,number);
    }

    @PostMapping("/acg/cosPhotoUpload")
    public Result<JSONObject> photoUpload(@RequestParam("photo") MultipartFile file){
        return acgFeignService.photoUpload(file);
    }

    @GetMapping("/acg/favorList")
    public Result<JSONObject> getFavorList(@RequestParam("id") Long id,
                                           @RequestParam("page") Long page,@RequestParam("cnt") Long cnt){
        return acgFeignService.getFavorList(id, page, cnt);
    }

    @PostMapping("/acg/likeCos")
    public Result<JSONObject> likeCos(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.likeCos(id, number);
    }

    @DeleteMapping("/acg/likeCos")
    public Result<JSONObject> deleteLikeCos(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.deleteLikeCos(id, number);
    }

    @DeleteMapping("/acg/dislikeAnswer")
    public Result<JSONObject> dislikeAnswer(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.dislikeAnswer(id, number);
    }

    @DeleteMapping("/acg/followQA")
    public Result<JSONObject> disFollowQA(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.disFollowQA(id,number);
    }

    @PostMapping("/acg/followQA")
    public Result<JSONObject> followQA(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.followQA(id,number);
    }

    @GetMapping("/acg/qaTopic")
    public Result<JSONObject> getQaTopic(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.getQaTopic(id, number);
    }

    @PostMapping("/acg/QA")
    public Result<JSONObject> generateQA(@RequestParam("id") Long id, @RequestParam("title") String title,
                                         @RequestParam("description") String description, @RequestParam("photo")List<String> photo,
                                         @RequestParam("label") List<String> label){
        return acgFeignService.generateQA(id, title, description, photo, label);
    }

    @PostMapping("/acg/favorCos")
    public Result<JSONObject> addFavorCos(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.addFavorCos(id, number);
    }

    @DeleteMapping("/acg/favorCos")
    public Result<JSONObject> deleteFavorCos(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.deleteFavorCos(id, number);
    }

    @PostMapping("/acg/judgeFavor")
    public Result<JSONObject> judgeFavor(@RequestParam("id") Long id, @RequestParam("number")List<Long> number){
        return acgFeignService.judgeFavor(id, number);
    }

    @PostMapping("/acg/likeAnswer")
    public Result<JSONObject> likeAnswer(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.likeAnswer(id, number);
    }

    @GetMapping("/acg/answerList")
    public Result<JSONObject> getAnswerList(@RequestParam(value = "id",required = false) Long id,
                                            @RequestParam("number") Long number,@RequestParam("type") Integer type,
                                            @RequestParam("cnt") Long cnt,@RequestParam("page") Long page){
        return acgFeignService.getAnswerList(id, number, type, cnt, page);
    }

    @GetMapping("/acg/answerCommentList")
    public Result<JSONObject> getAnswerCommentList(@RequestParam(value = "id",required = false) Long id,
                                                   @RequestParam("answerNumber") Long answerNumber,
                                                   @RequestParam("cnt") Long cnt ,@RequestParam("page") Long page,
                                                   @RequestParam("type") Integer type){
        return acgFeignService.getAnswerCommentList(id, answerNumber, cnt, page, type);
    }

    @GetMapping("/acg/answerCommentCommentList")
    public Result<JSONObject> getAnswerCommentCommentList(@RequestParam(value = "id",required = false) Long id,
                                                          @RequestParam("number") Long number,
                                                          @RequestParam("cnt") Long cnt,@RequestParam("page") Long page,
                                                          @RequestParam("type") Integer type){
        return acgFeignService.getAnswerCommentCommentList(id, number, cnt, page, type);
    }

    @GetMapping("/acg/recommendList")
    public Result<JSONObject> getRecommendList(){
        return acgFeignService.getRecommendList();
    }

    @GetMapping("/acg/hotDayCos")
    public Result<JSONObject> getHotDayCos(@RequestParam("time") String time,@RequestParam("type") Integer type){
        return acgFeignService.getHotDayCos(time,type);
    }

    @GetMapping("/acg/hotWeekCos")
    public Result<JSONObject> getHotMonthCos(@RequestParam("time") String time,@RequestParam("type") Integer type){
        return acgFeignService.getHotMonthCos(time,type);
    }

    @GetMapping("/acg/followCos")
    public Result<JSONObject> getFollowList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                            @RequestParam("page") Long page){
        return acgFeignService.getFollowList(id, cnt, page);
    }

    @GetMapping("/acg/followNoRead")
    public Result<JSONObject> getFollowNoRead(@RequestParam("id") Long id){
        return acgFeignService.getFollowNoRead(id);
    }

    @PutMapping("/acg/cos")
    public Result<JSONObject> patchCos(@RequestParam("id") Long id,@RequestParam("number") Long number,
                                       @RequestParam(value = "description",required = false) String description,
                                       @RequestParam(value = "cosPhoto",required = false) List<String> cosPhoto,
                                       @RequestParam(value = "permission",required = false) Integer permission){
        return acgFeignService.patchCos(id, number, description, cosPhoto, permission);
    }

    @GetMapping("/acg/searchCircle")
    public Result<JSONObject> searchCircle(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                           @RequestParam("page") Long page,@RequestParam("keyword") String keyword){
        return acgFeignService.searchCircle(id, cnt, page, keyword);
    }

    @PostMapping("/acg/answerComment")
    public Result<JSONObject> addAnswerComment(@RequestParam("id") Long id,@RequestParam("description") String description,
                                               @RequestParam("answerNumber") Long answerNumber,@RequestParam(value = "fatherNumber",required = false) Long fatherNumber,
                                               @RequestParam(value = "toId",required = false) Long toId,
                                               @RequestParam(value = "replyNumber",required = false) Long replyNumber){
        return acgFeignService.addAnswerComment(id, description, answerNumber, fatherNumber, toId, replyNumber);
    }

    @PostMapping("/acg/addAnswer")
    public Result<JSONObject> addAnswer(@RequestParam("id") Long id,@RequestParam("description") String description,
                                        @RequestParam("photo") List<String> photo,@RequestParam("number") Long number){
        return acgFeignService.addAnswer(id, description, photo, number);
    }

    @GetMapping("/acg/qaCountsList")
    public Result<JSONObject> getQaCountsList(@RequestParam(value = "id",required = false) Long id,
                                              @RequestParam("numbers") List<Long> numbers){
        return acgFeignService.getQaCountsList(id,numbers);
    }

    @GetMapping("/acg/qaCommentCountsList")
    public Result<JSONObject> getCommentCountsList(@RequestParam(value = "id",required = false) Long id,
                                                   @RequestParam("numbers") List<Long> numbers){
        return acgFeignService.getCommentCountsList(id, numbers);
    }

    @GetMapping("/acg/qaAnswerCountsList")
    public Result<JSONObject> getQaAnswerCountsList(@RequestParam(value = "id",required = false) Long id,
                                                    @RequestParam("numbers") List<Long> numbers){
        return acgFeignService.getQaAnswerCountsList(id, numbers);
    }

    @DeleteMapping("/acg/cosOwner")
    public Result<JSONObject> deleteCosByOwner(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return acgFeignService.deleteCosByOwner(id, number);
    }

    @GetMapping("/acg/favorQaList")
    public Result<JSONObject> getFavorQaList(@RequestParam("id") Long id,@RequestParam("page") Long page,
                                             @RequestParam("cnt") Long cnt){
        return acgFeignService.getFavorQaList(id, page, cnt);
    }

    @GetMapping("/acg/judgeCircle")
    public Result<JSONObject> judgeCircle(@RequestParam("id") Long id, @RequestParam("circleNames")List<String> circleNames){
        return acgFeignService.judgeCircle(id, circleNames);
    }

    @GetMapping("/acg/judgeQa")
    public Result<JSONObject> judgeQaFollow(@RequestParam("id") Long id,@RequestParam("numbers") List<Long> numbers){
        return acgFeignService.judgeQaFollow(id, numbers);
    }

    @GetMapping("/acg/circleCosList")
    public Result<JSONObject> getCircleCosList(@RequestParam("circleName") String circleName,@RequestParam("type") Integer type,
                                               @RequestParam("cnt") Long cnt,@RequestParam("page") Long page){
        return acgFeignService.getCircleCosList(circleName, type, cnt, page);
    }

    @GetMapping("/acg/judgeQaAnswer")
    public Result<JSONObject> judgeQaAnswer(@RequestParam("id") Long id,@RequestParam("numbers") List<Long> numbers){
        return acgFeignService.judgeQaAnswer(id, numbers);
    }

    @GetMapping("/acg/judgeQaComment")
    public Result<JSONObject> judgeQaComment(@RequestParam("id") Long id,@RequestParam("numbers") List<Long> numbers){
        return acgFeignService.judgeQaComment(id, numbers);
    }
}