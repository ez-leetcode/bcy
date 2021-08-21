package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.mapper.CircleFollowMapper;
import com.bcy.acgpart.mapper.CircleMapper;
import com.bcy.acgpart.mapper.SearchHistoryMapper;
import com.bcy.acgpart.pojo.Circle;
import com.bcy.acgpart.pojo.CircleFollow;
import com.bcy.acgpart.pojo.SearchHistory;
import com.bcy.acgpart.utils.OssUtils;
import com.bcy.acgpart.utils.RedisUtils;
import com.bcy.mq.EsMsgForCircle;
import com.bcy.vo.CircleInfoForSearchList;
import com.bcy.vo.JudgeCircleFollowForList;
import com.bcy.vo.PersonalCircleForList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class CircleServiceImpl implements CircleService{

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CircleFollowMapper circleFollowMapper;

    @Autowired
    private SearchHistoryMapper searchHistoryMapper;

    @Autowired
    private RabbitmqProducerService rabbitmqProducerService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String circlePhotoUpload(MultipartFile file, Long id) {
        return OssUtils.uploadPhoto(file,"circlePhoto");
    }

    @Override
    public String createCircle(String circleName, String description, String photo, String nickName) {
        Circle circle = circleMapper.selectById(circleName);
        if(circle != null){
            log.error("创建圈子失败，圈子已存在");
            return "repeatWrong";
        }
        //创建圈子
        circleMapper.insert(new Circle(circleName,description,photo,nickName,0,0,null));
        //es数据同步
        rabbitmqProducerService.sendEsCircleMessage(new EsMsgForCircle(circleName,1));
        return "success";
    }

    @Override
    public String followCircle(Long id, String circleName) {
        Circle circle = circleMapper.selectById(circleName);
        if(circle == null){
            log.error("关注圈子失败，圈子不存在");
            return "existWrong";
        }
        QueryWrapper<CircleFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("circle_name",circleName);
        CircleFollow circleFollow = circleFollowMapper.selectOne(wrapper);
        if(circleFollow != null){
            log.error("关注圈子失败，圈子已被关注");
            return "repeatWrong";
        }
        //这个比较少就不缓存了
        circle.setFollowCounts(circle.getFollowCounts() + 1);
        circleMapper.updateById(circle);
        circleFollowMapper.insert(new CircleFollow(null,circleName,id,null));
        rabbitmqProducerService.sendEsCircleMessage(new EsMsgForCircle(circleName,1));
        log.info("关注圈子成功");
        return "success";
    }

    @Override
    public String disFollowCircle(Long id, String circleName) {
        Circle circle = circleMapper.selectById(circleName);
        if(circle == null){
            log.error("取消关注失败，圈子不存在");
            return "existWrong";
        }
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
        rabbitmqProducerService.sendEsCircleMessage(new EsMsgForCircle(circleName,1));
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


    @Override
    public JSONObject getPersonalCircle(Long id, Long cnt, Long page) {
        JSONObject jsonObject = new JSONObject();
        Page<PersonalCircleForList> page1 = new Page<>(page,cnt);
        List<PersonalCircleForList> personalCircleForList = circleMapper.getPersonalCircleList(id,page1);
        jsonObject.put("personalCircleList",personalCircleForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取个人关注圈子列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject searchCircle(Long id, Long cnt, Long page, String keyword) {
        JSONObject jsonObject = new JSONObject();
        Page<CircleInfoForSearchList> page1 = new Page<>(page,cnt);
        List<CircleInfoForSearchList> circleInfoForSearchListList = circleMapper.searchCircle(keyword,page1);
        //存入搜索历史
        QueryWrapper<SearchHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("keyword",keyword);
        SearchHistory searchHistory = searchHistoryMapper.selectOne(wrapper);
        if(searchHistory != null){
            searchHistory.setDeleted(0);
            searchHistory.setReClick(searchHistory.getReClick() + 1);
            searchHistoryMapper.updateById(searchHistory);
        }else{
            //插入历史
            searchHistoryMapper.insert(new SearchHistory(null,id,keyword,0,0,null));
            //维护redis
            Page<String> page2 = new Page<>(1,20);
            List<String> historyList = searchHistoryMapper.getHistoryKeywordList(id,page2);
            redisUtils.saveByHoursTime("keyword_" + id,historyList.toString(),48);
        }
        jsonObject.put("searchCircleList",circleInfoForSearchListList);
        jsonObject.put("counts",page1.getTotal());
        jsonObject.put("pages",page1.getPages());
        log.info("搜索圈子成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject judgeCircleList(Long id, List<String> circleNames) {
        JSONObject jsonObject = new JSONObject();
        List<JudgeCircleFollowForList> judgeCircleFollowForListList = new LinkedList<>();
        for(String x:circleNames){
            QueryWrapper<CircleFollow> wrapper = new QueryWrapper<>();
            wrapper.eq("id",id)
                    .eq("circle_name",x);
            CircleFollow circleFollow = circleFollowMapper.selectOne(wrapper);
            if(circleFollow == null){
                judgeCircleFollowForListList.add(new JudgeCircleFollowForList(x,0));
            }else{
                judgeCircleFollowForListList.add(new JudgeCircleFollowForList(x,1));
            }
        }
        jsonObject.put("judgeCircleList",judgeCircleFollowForListList);
        log.info("判断圈子是否关注成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


}