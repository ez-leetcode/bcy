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
    @DeleteMapping("/acg/discuss")
    Result<JSONObject> deleteDiscuss(@RequestParam("numbers") List<Long> numbers);

    @PostMapping("/acg/discuss")
    Result<JSONObject> createDiscuss(@RequestParam("id") Long id, @RequestParam("title") String title,
                                     @RequestParam("description") String description);

    @GetMapping("/acg/judgeLikes")
    Result<JSONObject> judgeLikes(@RequestParam("id") Long id, @RequestParam("numbers") List<Long> numbers);

    @GetMapping("/acg/likeList")
    Result<JSONObject> getLikeList(@RequestParam("id") Long id, @RequestParam("cnt") Long cnt,
                                   @RequestParam("page") Long page, @RequestParam("keyword") String keyword);

    @PostMapping("/acg/like")
    Result<JSONObject> likeDiscuss(@RequestParam("id") Long id, @RequestParam("number") Long number);

    @PostMapping("/acg/like")
    Result<JSONObject> dislikeDiscuss(@RequestParam("id") Long id, @RequestParam("number") Long number);

}
