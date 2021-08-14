package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.mapper.CosCountsMapper;
import com.bcy.acgpart.mapper.CosPlayMapper;
import com.bcy.acgpart.mapper.FavorMapper;
import com.bcy.acgpart.pojo.CosPlay;
import com.bcy.acgpart.pojo.CosCounts;
import com.bcy.acgpart.pojo.Favor;
import com.bcy.acgpart.utils.RedisUtils;
import com.bcy.utils.PhotoUtils;
import com.bcy.vo.CosForFavor;
import com.bcy.vo.CosJudgeFavorForList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class FavorServiceImpl implements FavorService{

    @Autowired
    private CosPlayMapper cosPlayMapper;

    @Autowired
    private FavorMapper favorMapper;

    @Autowired
    private CosCountsMapper cosCountsMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public JSONObject getFavorList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        Page<CosForFavor> page1 = new Page<>(page,cnt);
        List<CosForFavor> cosForFavorList = favorMapper.getCosForFavor(id,page1);
        for(CosForFavor x:cosForFavorList){
            CosPlay cosPlay = cosPlayMapper.selectById(x.getCosNumber());
            if(cosPlay != null){
                x.setCosPhoto(PhotoUtils.photoStringToList(cosPlay.getPhoto()));
                x.setDescription(cosPlay.getDescription());
            }
        }
        jsonObject.put("favorList",cosForFavorList);
        jsonObject.put("counts",page1.getTotal());
        jsonObject.put("pages",page1.getPages());
        log.info("获取cos收藏列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public String addFavor(Long id, Long number) {
        CosCounts cosCounts = cosCountsMapper.selectById(number);
        if(cosCounts == null){
            log.error("添加收藏失败，cos不存在");
            return "existWrong";
        }
        QueryWrapper<Favor> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("cos_number",number);
        Favor favor = favorMapper.selectOne(wrapper);
        if(favor != null){
            log.error("添加收藏失败，cos已被收藏");
            return "repeatWrong";
        }
        //存入收藏记录
        favorMapper.insert(new Favor(null,number,id,null));
        //收藏数扔给redis
        String ck = redisUtils.getValue("cosFavorCounts_" + number);
        String ck1 = redisUtils.getValue("cosHotFavorCounts_" + number);
        String ck2 = redisUtils.getValue("cosHotFavorWeekCounts_" + number);
        if(ck == null){
            //redis里没有，存入
            redisUtils.saveByHoursTime("cosFavorCounts_" + number,cosCounts.getFavorCounts().toString(),48);
        }
        if(ck1 == null){
            redisUtils.saveByHoursTime("cosHotFavorCounts_" + number,"0",12);
        }
        if(ck2 == null){
            redisUtils.saveByHoursTime("cosHotFavorWeekCounts_" + number, "0",24 * 8);
        }
        redisUtils.addKeyByTime("cosHotFavorCounts_" + number,48);
        redisUtils.addKeyByTime("cosFavorCounts_" + number,12);
        redisUtils.addKeyByTime("cosHotFavorWeekCounts_" + number,24 * 8);
        log.info("添加收藏成功");
        return "success";
    }

    @Override
    public String deleteFavor(Long id, Long number) {
        CosCounts cosCounts = cosCountsMapper.selectById(number);
        if(cosCounts == null){
            log.error("取消收藏失败，cos不存在");
            return "existWrong";
        }
        QueryWrapper<Favor> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("cos_number",number);
        Favor favor = favorMapper.selectOne(wrapper);
        if(favor == null) {
            log.error("取消收藏失败，cos未被收藏");
            return "repeatWrong";
        }
        //清除收藏记录
        favorMapper.deleteById(favor.getId());
        //收藏数扔给redis
        String ck1 = redisUtils.getValue("cosHotFavorCounts_" + number);
        String ck2 = redisUtils.getValue("cosHotFavorWeekCounts_" + number);
        String ck = redisUtils.getValue("cosFavorCounts_" + number);
        if(ck == null){
            //redis没有，存入
            redisUtils.saveByHoursTime("cosFavorCounts_" + number,cosCounts.getFavorCounts().toString(),48);
        }
        if(ck2 == null){
            redisUtils.saveByHoursTime("cosHotFavorWeekCounts_" + number,"0",24 * 8);
        }
        if(ck1 == null){
            redisUtils.saveByHoursTime("cosHotFavorCounts_" + number,"0",12);
        }
        redisUtils.subKeyByTime("cosHotFavorCounts_" + number,48);
        redisUtils.subKeyByTime("cosFavorCounts_" + number,12);
        redisUtils.subKeyByTime("cosHotFavorWeekCounts_" + number,24 * 8);
        log.info("取消收藏成功");
        return "success";
    }

    @Override
    public JSONObject judgeFavor(Long id, List<Long> number) {
        //这里sql待调优
        JSONObject jsonObject = new JSONObject();
        List<CosJudgeFavorForList> judgeFavorList = new LinkedList<>();
        for(Long x:number){
            QueryWrapper<Favor> wrapper = new QueryWrapper<>();
            wrapper.eq("cos_number",x)
                    .eq("id",id);
            Favor favor = favorMapper.selectOne(wrapper);
            if(favor == null){
                //未收藏
                judgeFavorList.add(new CosJudgeFavorForList(x,0));
            }else{
                judgeFavorList.add(new CosJudgeFavorForList(x,1));
            }
        }
        jsonObject.put("judgeFavorList",judgeFavorList);
        log.info("获取收藏情况成功");
        log.info(judgeFavorList.toString());
        return jsonObject;
    }

}
