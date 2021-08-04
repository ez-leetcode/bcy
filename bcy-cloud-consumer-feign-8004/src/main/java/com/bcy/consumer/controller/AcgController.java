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
    public Result<JSONObject> circlePhotoUpload(@RequestParam("photo") MultipartFile file, @RequestParam("id") Long id){
        return acgFeignService.circlePhotoUpload(file,id);
    }

    @PostMapping("/acg/circle")
    public Result<JSONObject> createCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName,
                                           @RequestParam("description") String description,
                                           @RequestParam("photo") String photo){
        return acgFeignService.createCircle(id, circleName, description, photo);
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

    @DeleteMapping("/acg/discuss")
    public Result<JSONObject> deleteDiscuss(@RequestParam("numbers") List<Long> numbers){
        return acgFeignService.deleteDiscuss(numbers);
    }

    @PostMapping("/acg/discuss")
    public Result<JSONObject> createDiscuss(@RequestParam("id") Long id,@RequestParam("title") String title,
                                            @RequestParam("description") String description){
        return acgFeignService.createDiscuss(id,title,description);
    }

    @GetMapping("/acg/judgeLikes")
    public Result<JSONObject> judgeLikes(@RequestParam("id") Long id, @RequestParam("numbers")List<Long> numbers){
        return acgFeignService.judgeLikes(id, numbers);
    }

    @GetMapping("/acg/likeList")
    public Result<JSONObject> getLikeList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                          @RequestParam("page") Long page,@RequestParam("keyword") String keyword){
        return acgFeignService.getLikeList(id, cnt, page, keyword);
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
                                         @RequestParam("description") String description, @RequestParam("photo")List<String> photo){
        return acgFeignService.generateQA(id, title, description, photo);
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

    @PostMapping("/acg/cosPhotoUpload")
    public Result<JSONObject> photoUpload(@RequestParam("photo") MultipartFile file){
        return acgFeignService.photoUpload(file);
    }

    @PostMapping("/acg/cos")
    public Result<JSONObject> createCos(@RequestParam("id") Long id, @RequestParam("description") String description,
                                        @RequestParam("photo") List<String> photo,@RequestParam("label") List<String> label){
        return acgFeignService.createCos(id, description, photo, label);
    }

}