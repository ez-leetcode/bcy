package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.mapper.*;
import com.bcy.acgpart.pojo.CosPlay;
import com.bcy.acgpart.pojo.CosCounts;
import com.bcy.acgpart.pojo.Favor;
import com.bcy.acgpart.pojo.Qa;
import com.bcy.acgpart.utils.RedisUtils;
import com.bcy.utils.PhotoUtils;
import com.bcy.vo.CosForFavor;
import com.bcy.vo.CosJudgeFavorForList;
import com.bcy.vo.QaFollowForList;
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

    @Autowired
    private CircleCosMapper circleCosMapper;

    @Autowired
    private QaFollowMapper qaFollowMapper;

    @Autowired
    private QaLabelMapper qaLabelMapper;

    @Autowired
    private QaMapper qaMapper;

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
                x.setCreateTime(cosPlay.getCreateTime());
            }
        }
        jsonObject.put("favorList",cosForFavorList);
        jsonObject.put("counts",page1.getTotal());
        jsonObject.put("pages",page1.getPages());
        log.info("??????cos??????????????????");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public String addFavor(Long id, Long number) {
        CosCounts cosCounts = cosCountsMapper.selectById(number);
        if(cosCounts == null){
            log.error("?????????????????????cos?????????");
            return "existWrong";
        }
        QueryWrapper<Favor> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("cos_number",number);
        Favor favor = favorMapper.selectOne(wrapper);
        if(favor != null){
            log.error("?????????????????????cos????????????");
            return "repeatWrong";
        }
        //??????????????????
        favorMapper.insert(new Favor(null,number,id,null));
        //???????????????redis
        String ck = redisUtils.getValue("cosFavorCounts_" + number);
        if(ck == null){
            //redis??????????????????
            redisUtils.saveByHoursTime("cosFavorCounts_" + number,cosCounts.getFavorCounts().toString(),48);
        }
        redisUtils.addKeyByTime("cosFavorCounts_" + number,12);
        //????????????
        Long cosExist = circleCosMapper.judgeCircleCosExist("cos",number);
        Long drawExist = circleCosMapper.judgeCircleCosExist("??????",number);
        Long writeExist = circleCosMapper.judgeCircleCosExist("??????",number);
        if(cosExist != null){
            redisUtils.addKeyByTime("cosHotFavorCounts1_" + number,48);
            redisUtils.addKeyByTime("cosHotFavorWeekCounts1_" + number,24 * 8);
        }
        if(drawExist != null){
            redisUtils.addKeyByTime("cosHotFavorCounts2_" + number,48);
            redisUtils.addKeyByTime("cosHotFavorWeekCounts2_" + number,24 * 8);
        }
        if(writeExist != null){
            redisUtils.addKeyByTime("cosHotFavorCounts3_" + number,48);
            redisUtils.addKeyByTime("cosHotFavorWeekCounts3_" + number,24 * 8);
        }
        log.info("??????????????????");
        return "success";
    }

    @Override
    public String deleteFavor(Long id, Long number) {
        CosCounts cosCounts = cosCountsMapper.selectById(number);
        if(cosCounts == null){
            log.error("?????????????????????cos?????????");
            return "existWrong";
        }
        QueryWrapper<Favor> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("cos_number",number);
        Favor favor = favorMapper.selectOne(wrapper);
        if(favor == null) {
            log.error("?????????????????????cos????????????");
            return "repeatWrong";
        }
        //??????????????????
        favorMapper.deleteById(favor.getNumber());
        //???????????????redis
        String ck = redisUtils.getValue("cosFavorCounts_" + number);
        if(ck == null){
            //redis???????????????
            redisUtils.saveByHoursTime("cosFavorCounts_" + number,cosCounts.getFavorCounts().toString(),48);
        }
        redisUtils.subKeyByTime("cosFavorCounts_" + number,12);
        Long cosExist = circleCosMapper.judgeCircleCosExist("cos",number);
        Long drawExist = circleCosMapper.judgeCircleCosExist("??????",number);
        Long writeExist = circleCosMapper.judgeCircleCosExist("??????",number);
        if(cosExist != null){
            redisUtils.subKeyByTime("cosHotFavorCounts1_" + number,48);
            redisUtils.subKeyByTime("cosHotFavorWeekCounts1_" + number,24 * 8);
        }
        if(drawExist != null){
            redisUtils.subKeyByTime("cosHotFavorCounts2_" + number,48);
            redisUtils.subKeyByTime("cosHotFavorWeekCounts2_" + number,24 * 8);
        }
        if(writeExist != null){
            redisUtils.subKeyByTime("cosHotFavorCounts3_" + number,48);
            redisUtils.subKeyByTime("cosHotFavorWeekCounts3_" + number,24 * 8);
        }
        log.info("??????????????????");
        return "success";
    }

    @Override
    public JSONObject judgeFavor(Long id, List<Long> number) {
        //??????sql?????????
        JSONObject jsonObject = new JSONObject();
        List<CosJudgeFavorForList> judgeFavorList = new LinkedList<>();
        for(Long x:number){
            QueryWrapper<Favor> wrapper = new QueryWrapper<>();
            wrapper.eq("cos_number",x)
                    .eq("id",id);
            Favor favor = favorMapper.selectOne(wrapper);
            if(favor == null){
                //?????????
                judgeFavorList.add(new CosJudgeFavorForList(x,0));
            }else{
                judgeFavorList.add(new CosJudgeFavorForList(x,1));
            }
        }
        jsonObject.put("judgeFavorList",judgeFavorList);
        log.info("????????????????????????");
        log.info(judgeFavorList.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getFavorQaList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        Page<QaFollowForList> page1 = new Page<>(page,cnt);
        List<QaFollowForList> qaFollowForListList = qaFollowMapper.getQaFollowList(id,page1);
        for(QaFollowForList x:qaFollowForListList){
            List<String> qaList = qaLabelMapper.getAllCircleNameFromQaNumber(x.getQaNumber());
            if(qaList != null){
                x.setLabel(qaList);
            }
            Qa qa = qaMapper.selectById(x.getQaNumber());
            if(qa != null){
                x.setTitle(qa.getTitle());
                x.setDescription(qa.getDescription());
            }
        }
        jsonObject.put("qaFollowList",qaFollowForListList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("??????????????????????????????");
        log.info(jsonObject.toString());
        return jsonObject;
    }

}
