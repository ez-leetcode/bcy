package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.ecs.model.v20140526.DescribeRecommendInstanceTypeResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.elasticsearch.mapper.UserMapper;
import com.bcy.elasticsearch.utils.RedisUtils;
import com.bcy.vo.RecommendUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    //因为是热点高频数据，这里优化了一下，用了分页缓存
    @Override
    public JSONObject getRecommendUser(Long page,Long cnt) {
        JSONObject jsonObject = new JSONObject();
        String ck = redisUtils.getValue("recommendUser_" + page + cnt);
        if(ck != null){
            jsonObject.put("recommendUserList",ck);
        }else{
            //不在缓存里
            Page<RecommendUser> page1 = new Page<>(page,cnt);
            List<RecommendUser> recommendUserList = userMapper.getRecommendUserList(page1);
            redisUtils.saveByHoursTime("recommendUserList",recommendUserList.toString(),12);
            jsonObject.put("recommendUserList",recommendUserList.toString());
        }
        log.info("获取推荐用户成功");
        return jsonObject;
    }

}
