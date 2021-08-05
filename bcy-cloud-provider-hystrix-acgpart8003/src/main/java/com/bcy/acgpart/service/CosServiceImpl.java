package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.mapper.*;
import com.bcy.acgpart.pojo.*;
import com.bcy.acgpart.utils.OssUtils;
import com.bcy.acgpart.utils.RedisUtils;
import com.bcy.utils.PhotoUtils;
import com.bcy.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class CosServiceImpl implements CosService {

    @Autowired
    private CosMapper cosMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CosCountsMapper cosCountsMapper;

    @Autowired
    private CircleCosMapper circleCosMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HistoryMapper historyMapper;

    @Autowired
    private CosCommentMapper cosCommentMapper;

    @Autowired
    private CosCommentLikeMapper cosCommentLikeMapper;

    @Override
    public String deleteCos(List<Long> numbers) {
        //先删动态
        for(Long x:numbers){
            Cos cos = cosMapper.selectById(x);
            if(cos != null){
                User user = userMapper.selectById(cos.getId());
                if(user != null){
                    user.setMomentCounts(user.getMomentCounts() - 1);
                    userMapper.updateById(user);
                }
            }
        }
        //删除cos
        int result = cosMapper.deleteBatchIds(numbers);
        //删除cosCounts
        cosCountsMapper.deleteBatchIds(numbers);
        if(result == 0){
            log.error("删除cos失败，cos不存在");
            return "existWrong";
        }
        //通知用户待完成
        log.info("删除cos成功，共删除：" + result + "条");
        return "success";
    }

    @Override
    public JSONObject getCosCountsList(Long id, List<Long> number) {
        JSONObject jsonObject = new JSONObject();
        List<CosCountsForList> cosCountsForList = new LinkedList<>();
        for(Long x:number){
            CosCounts cosCounts = new CosCounts();
            CosCountsForList cosCountsForList1 = new CosCountsForList();
            String favorCounts = redisUtils.getValue("cosFavorCounts_" + x);
            String likeCounts = redisUtils.getValue("cosLikeCounts_" + x);
            String commentCounts = redisUtils.getValue("cosCommentCounts_" + x);
            String shareCounts = redisUtils.getValue("cosShareCounts_" + x);
            if(favorCounts == null || likeCounts == null || commentCounts == null || shareCounts == null){
                cosCounts = cosCountsMapper.selectById(x);
            }
            if(favorCounts != null){
                cosCountsForList1.setFavorCounts(Integer.parseInt(favorCounts));
            }else{
                cosCountsForList1.setFavorCounts(cosCounts.getFavorCounts());
                //redis插入
                redisUtils.saveByHoursTime("cosFavorCounts_" + x,cosCounts.getFavorCounts().toString(),12);
            }
            if(likeCounts != null){
                cosCountsForList1.setLikeCounts(Integer.parseInt(likeCounts));
            }else{
                cosCountsForList1.setLikeCounts(cosCounts.getLikeCounts());
                //redis插入
                redisUtils.saveByHoursTime("cosLikeCounts_" + x,cosCounts.getLikeCounts().toString(),12);
            }
            if(commentCounts != null){
                cosCountsForList1.setCommentCounts(Integer.parseInt(commentCounts));
            }else{
                cosCountsForList1.setCommentCounts(cosCounts.getCommentCounts());
                //redis插入
                redisUtils.saveByHoursTime("cosCommentCounts_" + x,cosCounts.getCommentCounts().toString(),12);
            }
            if(shareCounts != null){
                cosCountsForList1.setShareCounts(Integer.parseInt(shareCounts));
            }else{
                cosCountsForList1.setShareCounts(cosCounts.getShareCounts());
                //redis插入
                redisUtils.saveByHoursTime("cosCommentCounts_" + x,cosCounts.getCommentCounts().toString(),12);
            }
            cosCountsForList.add(cosCountsForList1);
        }
        jsonObject.put("cosCountsList",cosCountsForList);
        log.info("获取cos计数信息成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getCosCommentCountsList(Long id, List<Long> number) {
        JSONObject jsonObject = new JSONObject();
        List<CosCommentCountsForList> cosCommentCountsForList = new LinkedList<>();
        for(Long x:number){
            CosComment cosComment = new CosComment();
            CosCommentCountsForList cosCommentCountsForList1 = new CosCommentCountsForList(x,null,null);
            String likeCounts = redisUtils.getValue("cosCommentLikeCounts_" + x);
            String commentCounts = redisUtils.getValue("cosCommentCommentCounts_" + x);
            if(likeCounts == null || commentCounts == null){
                cosComment = cosCommentMapper.selectById(x);
            }
            if(likeCounts != null){
                cosCommentCountsForList1.setLikeCounts(Integer.parseInt(likeCounts));
            }else{
                cosCommentCountsForList1.setCommentCounts(cosComment.getCommentCounts());
                redisUtils.saveByHoursTime("cosCommentLikeCounts_" + x,cosComment.getLikeCounts().toString(),12);
            }
            if(commentCounts != null){
                cosCommentCountsForList1.setCommentCounts(Integer.parseInt(commentCounts));
            }else{
                cosCommentCountsForList1.setCommentCounts(cosCommentCountsForList1.getCommentCounts());
                redisUtils.saveByHoursTime("cosCommentCommentCounts_" + x,cosComment.getCommentCounts().toString(),12);
            }
            cosCommentCountsForList.add(cosCommentCountsForList1);
        }
        jsonObject.put("cosCommentCountsList",cosCommentCountsForList);
        log.info("获取cos下评论列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    //内容检测待完成
    @Override
    public String generateCos(Long id, String description, List<String> photo, List<String> label) {
        String ck = redisUtils.getValue("generateCos_" + id);
        int cosCounts = 0;
        if(ck != null){
            cosCounts = Integer.parseInt(ck);
        }
        if(cosCounts >= 15){
            log.warn("生成cos失败，短期内次数过多");
            return "repeatWrong";
        }
        //加一
        redisUtils.saveByHoursTime("generateCos_" + id,String.valueOf(cosCounts + 1),24);
        //个人动态加1
        User user = userMapper.selectById(id);
        user.setMomentCounts(user.getMomentCounts() + 1);
        userMapper.updateById(user);
        //list转string
        String photoString = PhotoUtils.photoListToString(photo);
        //插入cos
        cosMapper.insert(new Cos(null,id,description,photoString,null,null));
        QueryWrapper<Cos> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("description",description)
                .eq("photo",photoString);
        Cos cos = cosMapper.selectOne(wrapper);
        //插入cos计数
        cosCountsMapper.insert(new CosCounts(cos.getNumber(),0,0,0,0,null));
        //插入发布
        for(String x:label){
            Circle circle = circleMapper.selectById(x);
            if(circle != null){
                //圈子存在
                String ck1 = redisUtils.getValue("circlePostCounts_" + circle.getCircleName());
                if(ck1 != null){
                    //redis有以redis数据为标准更新
                    int cnt = Integer.parseInt(ck1);
                    cnt ++;
                    redisUtils.saveByHoursTime("circlePostCounts_" + circle.getCircleName(), Integer.toString(cnt),12);
                }else{
                    //redis没有以mysql为标准更新
                    redisUtils.saveByHoursTime("circlePostCounts_" + circle.getCircleName(),Integer.toString(circle.getPostCounts() + 1),12);
                }
            }
            //加一
            circleCosMapper.insert(new CircleCos(null,x,cos.getNumber(),null));
        }
        log.info("生成cos成功");
        return "success";
    }


    @Override
    public JSONObject getCosTopic(Long id, Long number) {
        Cos cos = cosMapper.selectById(number);
        if(cos == null){
            log.error("获取cos内容失败，cos不存在");
            return null;
        }
        User user = userMapper.selectById(cos.getId());
        if(user == null){
            log.error("获取cos内容失败，cos作者已被封禁");
            return null;
        }
        //插入历史记录
        if(id != null){
            QueryWrapper<History> wrapper = new QueryWrapper<>();
            wrapper.eq("id",id)
                    .eq("cos_number",number);
            History history = historyMapper.selectOne(wrapper);
            if(history == null){
                //插入数据
                historyMapper.insert(new History(null,number,id,0,null));
            }else{
                //更新数据，让排序的时候在前面
                history.setReClickCounts(history.getReClickCounts() + 1);
                historyMapper.updateById(history);
            }
        }
        List<String> cosPhoto = PhotoUtils.photoStringToList(cos.getPhoto());
        List<String> label = circleCosMapper.getAllCircleNameFromCosNumber(number);
        CosForTopic cosForTopic = new CosForTopic(number,cos.getId(),user.getUsername(),user.getPhoto(),user.getFansCounts(),cos.getDescription(),cosPhoto,label,cos.getCreateTime());
        String ck = redisUtils.getValue("fansCounts_" + user.getId());
        if(ck == null){
            redisUtils.saveByHoursTime("fansCounts_" + user.getId(),user.getFansCounts().toString(),12);
        }else{
            //以redis为准
            cosForTopic.setFansCounts(Integer.parseInt(ck));
        }
        //获取信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cos",cosForTopic);
        log.info("获取cos内容成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getCosComment(Long id, Long number, Long page, Long cnt, Integer type) {
        JSONObject jsonObject = new JSONObject();
        Page<CosCommentForList> page1 = new Page<>(page,cnt);
        List<CosCommentForList> commentForList;
        if(type == 1){
            commentForList = cosCommentMapper.getCosCommentListByHot(number,page1);
        }else{
            commentForList = cosCommentMapper.getCosCommentListByTime(number,page1);
        }
        jsonObject.put("cosCommentList",commentForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取Cos下的评论列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public String likeCosComment(Long id, Long number) {
        CosComment cosComment = cosCommentMapper.selectById(number);
        if(cosComment == null){
            log.error("点赞cos下评论失败，评论不存在");
            return "existWrong";
        }
        QueryWrapper<CosCommentLike> wrapper = new QueryWrapper<>();
        wrapper.eq("comment_number",number)
                .eq("id",id);
        CosCommentLike cosCommentLike = cosCommentLikeMapper.selectOne(wrapper);
        if(cosCommentLike != null){
            log.error("点赞cos下评论失败，评论已被点赞");
            return "repeatWrong";
        }
        //插入数据
        cosCommentLikeMapper.insert(new CosCommentLike(null,number,id,null));
        //存redis
        String ck = redisUtils.getValue("cosCommentLikeCounts_" + number);
        if(ck == null){
            redisUtils.saveByHoursTime("cosCommentLikeCounts_" + number,cosComment.getLikeCounts().toString(),12);
        }
        redisUtils.addKeyByTime("cosCommentLikeCounts_" + number,12);
        log.info("点赞cos下评论成功");
        return "success";
    }

    @Override
    public String dislikeCosComment(Long id, Long number) {
        CosComment cosComment = cosCommentMapper.selectById(number);
        if(cosComment == null){
            log.error("取消点赞cos下评论失败，评论不存在");
            return "existWrong";
        }
        QueryWrapper<CosCommentLike> wrapper = new QueryWrapper<>();
        wrapper.eq("comment_number",number)
                .eq("id",id);
        CosCommentLike cosCommentLike = cosCommentLikeMapper.selectOne(wrapper);
        if(cosCommentLike == null){
            log.error("取消点赞cos下评论失败，评论未被点赞");
            return "repeatWrong";
        }
        //删除数据
        cosCommentLikeMapper.deleteById(cosCommentLike);
        //存入redis
        String ck = redisUtils.getValue("cosCommentLikeCounts_" + number);
        if(ck == null){
            redisUtils.saveByHoursTime("cosCommentLikeCounts_" + number,cosComment.getLikeCounts().toString(),12);
        }
        redisUtils.subKeyByTime("cosCommentLikeCounts_" + number,12);
        log.info("取消点赞cos下评论成功");
        return "success";
    }


    @Override
    public JSONObject getCosCommentList(Long id, Long number, Long cnt, Long page,Integer type) {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<CosComment> wrapper = new QueryWrapper<>();
        wrapper.eq("father_number",number);
        if(type == 1){
            wrapper.orderByDesc("like_counts");
        }else{
            wrapper.orderByAsc("create_time");
        }
        Page<CosComment> page1 = new Page<>(page,cnt);
        cosCommentMapper.selectPage(page1,wrapper);
        List<CosComment> cosCommentList = page1.getRecords();
        List<CosCommentCommentForList> cosCommentCommentForList = new LinkedList<>();
        for(CosComment x:cosCommentList){
            CosCommentCommentForList cosCommentCommentForList1 = new CosCommentCommentForList(x.getNumber(),x.getFromId(),null,
                    null,x.getDescription(),x.getToId(),null,x.getCreateTime());
            User user = userMapper.selectById(x.getFromId());
            User user1 = userMapper.selectById(x.getToId());
            if(user != null){
                cosCommentCommentForList1.setFromUsername(user.getUsername());
                cosCommentCommentForList1.setFromPhoto(user.getPhoto());
            }
            if(user1 != null){
                cosCommentCommentForList1.setToUsername(user1.getUsername());
            }
            cosCommentCommentForList.add(cosCommentCommentForList1);
        }
        jsonObject.put("commentCommentList",cosCommentCommentForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取cos的评论的评论列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    //推送待完成
    @Override
    public String addComment(Long id, Long cosNumber, String description, Long fatherNumber, Long toId, Long replyNumber) {
        //插入评论内容
        cosCommentMapper.insert(new CosComment(null,cosNumber,fatherNumber,id,toId,replyNumber,0,0,description,null));
        //添加父级评论评论数
        if(fatherNumber != null && fatherNumber != 0){
            String ck = redisUtils.getValue("cosCommentCommentCounts_" + fatherNumber);
            if(ck == null){
                CosComment cosComment = cosCommentMapper.selectById(fatherNumber);
                if(cosComment != null){
                    redisUtils.saveByHoursTime("cosCommentCommentCounts_" + fatherNumber,cosComment.getCommentCounts().toString(),12);
                }
            }
            redisUtils.addKeyByTime("cosCommentCommentCounts_" + fatherNumber,12);
        }
        //添加cos的评论数
        String ck = redisUtils.getValue("cosCommentCounts_" + cosNumber);
        if(ck == null){
            CosCounts cosCounts = cosCountsMapper.selectById(cosNumber);
            if(cosCounts != null){
                redisUtils.saveByHoursTime("cosCommentCounts_" + cosNumber,cosCounts.getCommentCounts().toString(),12);
            }
        }
        redisUtils.addKeyByTime("cosCommentCounts_" + cosNumber,12);
        //推送待完成
        log.info("添加cos下的评论成功");
        return "success";
    }


    @Override
    public String cosPhotoUpload(MultipartFile file) {
        String url = OssUtils.uploadPhoto(file,"cosPhoto");
        if(url.length() < 12){
            log.error("上传cos图片文件失败");
            return url;
        }
        //成功上传图片
        log.info("上传cos图片文件成功，url：" + url);
        return url;
    }

}