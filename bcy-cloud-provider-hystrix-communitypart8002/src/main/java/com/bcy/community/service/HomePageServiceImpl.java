package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.mapper.CircleCosMapper;
import com.bcy.community.mapper.CosPlayMapper;
import com.bcy.community.mapper.SearchHistoryMapper;
import com.bcy.community.mapper.UserMapper;
import com.bcy.community.pojo.CosPlay;
import com.bcy.community.pojo.SearchHistory;
import com.bcy.community.pojo.User;
import com.bcy.community.utils.RedisUtils;
import com.bcy.utils.PhotoUtils;
import com.bcy.vo.CosHomePageForList;
import com.bcy.vo.UserInfo;
import com.bcy.vo.UserInfoForSearchList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HomePageServiceImpl implements HomePageService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CosPlayMapper cosPlayMapper;

    @Autowired
    private CircleCosMapper circleCosMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SearchHistoryMapper searchHistoryMapper;

    @Override
    public JSONObject getOthersInfo(Long id) {
        User user = userMapper.selectById(id);
        if(user == null){
            log.error("获取他人信息失败，用户不存在或已被封禁");
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userInfo",new UserInfo(user.getId(),user.getUsername(),user.getPhoto(),user.getDescription(),user.getSex(),user.getFollowCounts(),user.getFansCounts()));
        log.info("获取他人用户信息成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject searchUser(Long id, Long page, Long cnt, String keyword) {
        JSONObject jsonObject = new JSONObject();
        Page<UserInfoForSearchList> page1 = new Page<>(page,cnt);
        List<UserInfoForSearchList> userInfoForSearchListList = userMapper.searchUser(id,keyword,page1);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        jsonObject.put("searchUserList",userInfoForSearchListList);
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
        log.info("搜索用户成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getUserCosList(Long userId, Long cnt, Long page) {
        JSONObject jsonObject = new JSONObject();
        Page<CosHomePageForList> page1 = new Page<>(page,cnt);
        List<CosHomePageForList> cosHomePageForListList = cosPlayMapper.getUserCosList(userId,page1);
        for(CosHomePageForList x:cosHomePageForListList){
            CosPlay cosPlay = cosPlayMapper.selectById(x.getNumber());
            if(cosPlay != null){
                x.setCosPhoto(PhotoUtils.photoStringToList(cosPlay.getPhoto()));
            }
            List<String> label = circleCosMapper.getAllCircleNameFromCosNumber(x.getNumber());
            x.setLabel(label);
        }
        jsonObject.put("counts",page1.getTotal());
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("cosUserList",cosHomePageForListList);
        log.info("获取用户个人cos列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

}
