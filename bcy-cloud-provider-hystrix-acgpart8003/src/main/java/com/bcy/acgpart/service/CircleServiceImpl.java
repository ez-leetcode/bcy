package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.acgpart.mapper.CircleFollowMapper;
import com.bcy.acgpart.mapper.CircleMapper;
import com.bcy.acgpart.pojo.Circle;
import com.bcy.acgpart.pojo.CircleFollow;
import com.bcy.acgpart.utils.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class CircleServiceImpl implements CircleService{

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CircleFollowMapper circleFollowMapper;

    @Override
    public String circlePhotoUpload(MultipartFile file, Long id) {
        return OssUtils.uploadPhoto(file,"circlePhoto");
    }

    @Override
    public String createCircle(String circleName, String description, String photo) {
        Circle circle = circleMapper.selectById(circleName);
        if(circle != null){
            log.error("创建圈子失败，圈子已存在");
            return "repeatWrong";
        }
        //创建圈子
        circleMapper.insert(new Circle(circleName,description,photo,0,0,null));
        return "success";
    }

    @Override
    public String followCircle(Long id, String circleName) {
        Circle circle = circleMapper.selectById(id);
        QueryWrapper<CircleFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("circle_name",circleName);
        CircleFollow circleFollow = circleFollowMapper.selectOne(wrapper);
        if(circleFollow != null){
            log.error("关注圈子失败，圈子已被关注");
            return "repeatWrong";
        }
        circle.setFollowCounts(circle.getFollowCounts() + 1);
        circleMapper.updateById(circle);
        circleFollowMapper.insert(new CircleFollow(null,circleName,id,null));
        log.info("关注圈子成功");
        return "success";
    }

    @Override
    public String disFollowCircle(Long id, String circleName) {
        Circle circle = circleMapper.selectById(id);
        QueryWrapper<CircleFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("circle_name",circleName);
        CircleFollow circleFollow = circleFollowMapper.selectOne(wrapper);
        if(circleFollow == null){
            log.error("取消关注圈子失败，圈子未被关注");
            return "repeatWrong";
        }
        circle.setFollowCounts(circle.getFollowCounts() - 1);
        circleMapper.updateById(circle);
        circleFollowMapper.delete(wrapper);
        log.info("取消关注圈子成功");
        return "success";
    }

    @Override
    public JSONObject getCircleInfo(String circleName) {
        Circle circle = circleMapper.selectById(circleName);
        if(circle == null){
            log.error("获取圈子基本信息失败，圈子不存在");
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("circleInfo",circle);
        log.info("获取圈子基本信息成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

}