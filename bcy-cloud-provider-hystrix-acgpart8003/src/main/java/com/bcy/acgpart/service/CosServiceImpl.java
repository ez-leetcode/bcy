package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.mapper.*;
import com.bcy.acgpart.pojo.*;
import com.bcy.acgpart.utils.OssUtils;
import com.bcy.acgpart.utils.RedisUtils;
import com.bcy.mq.AtMsg;
import com.bcy.mq.CommentMsg;
import com.bcy.mq.EsMsg;
import com.bcy.mq.LikeMsg;
import com.bcy.utils.CommentUtils;
import com.bcy.utils.PhotoUtils;
import com.bcy.utils.RecommendUtils;
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
    private CosPlayMapper cosPlayMapper;

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

    @Autowired
    private CosDayHotMapper cosDayHotMapper;

    @Autowired
    private CosMonthMapper cosMonthMapper;

    @Autowired
    private RabbitmqProducerService rabbitmqProducerService;

    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private LikeMessageMapper likeMessageMapper;

    @Autowired
    private CommentMessageMapper commentMessageMapper;

    @Autowired
    private AtMessageMapper atMessageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    //这个接口暂时不用
    @Override
    public String deleteCos(List<Long> numbers) {
        //先删动态
        for(Long x:numbers){
            CosPlay cosPlay = cosPlayMapper.selectById(x);
            if(cosPlay != null){
                User user = userMapper.selectById(cosPlay.getId());
                if(user != null){
                    user.setMomentCounts(user.getMomentCounts() - 1);
                    userMapper.updateById(user);
                }
            }
            //es更新
            rabbitmqProducerService.sendEsMessage(new EsMsg(x,2));
        }
        //删除cos
        int result = cosPlayMapper.deleteBatchIds(numbers);
        if(result == 0){
            log.error("删除cos失败，cos不存在");
            return "existWrong";
        }
        //通知用户待完成
        log.info("删除cos成功，共删除：" + result + "条");
        return "success";
    }

    @Override
    public String deleteCosByOwner(Long id, Long number) {
        CosPlay cosPlay = cosPlayMapper.selectById(number);
        if(cosPlay == null){
            log.error("删除cos失败，cos不存在");
            return "existWrong";
        }
        if(!cosPlay.getId().equals(id)){
            log.error("删除cos失败，用户不正确");
            return "userWrong";
        }
        //删除cos
        cosPlayMapper.deleteById(cosPlay.getNumber());
        //删除cosCounts
        cosCountsMapper.deleteById(cosPlay.getNumber());
        //删除circleCos
        QueryWrapper<CircleCos> wrapper = new QueryWrapper<>();
        wrapper.eq("cos_number",number);
        List<CircleCos> circleCosList = circleCosMapper.selectList(wrapper);
        //减少发布次数
        for(CircleCos x:circleCosList){
            String ck = redisUtils.getValue("circlePostCounts_" + x.getCircleName());
            if(ck == null){
                Circle circle = circleMapper.selectById(x.getCircleName());
                if(circle != null){
                    redisUtils.saveByHoursTime("circlePostCounts_" + x.getCircleName(),circle.getCircleName(),12);
                }
                redisUtils.subKeyByTime("circlePostCounts_" + x.getCircleName(),12);
            }
        }
        //es更新
        rabbitmqProducerService.sendEsMessage(new EsMsg(number,2));
        return "success";
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
                if(cosComment == null){
                    cosComment = new CosComment(0L,0L,0L,0L,0L,0L,0,0,null,null);
                }
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
    public String generateCos(Long id, String description, List<String> photo, List<String> label,Integer permission) {
        String ck = redisUtils.getValue("generateCos_" + id);
        int cosCounts = 0;
        if(ck != null){
            cosCounts = Integer.parseInt(ck);
        }
        if(cosCounts >= 15){
            log.warn("生成cos失败，短期内次数过多");
            return "repeatWrong";
        }
        //判断是否合法，推送待完成
        if(CommentUtils.judgeComment(description)){
            log.error("生成cos失败，用户描述不合法");
            return "dirtyWrong";
        }
        //加一
        redisUtils.saveByHoursTime("generateCos_" + id,String.valueOf(cosCounts + 1),24);
        //list转string
        String photoString = PhotoUtils.photoListToString(photo);
        //插入cos
        cosPlayMapper.insert(new CosPlay(null,id,description,photoString,permission,null,null));
        QueryWrapper<CosPlay> wrapper = new QueryWrapper<>();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wrapper.eq("id",id)
                .eq("description",description)
                .eq("photo",photoString)
                .eq("permission",permission)
                .orderByDesc("create_time");
        List<CosPlay> cosPlayList = cosPlayMapper.selectList(wrapper);
        CosPlay cosPlay = new CosPlay();
        if(!cosPlayList.isEmpty()){
            cosPlay = cosPlayList.get(0);
        }
        //插入cos计数
        cosCountsMapper.insert(new CosCounts(cosPlay.getNumber(),0,0,0,0,null));
        //个人动态加1
        String ck2 = redisUtils.getValue("momentCounts_" + id);
        User user = userMapper.selectById(id);
        if(ck2 == null){
            redisUtils.saveByHoursTime("momentCounts_" + id,user.getMomentCounts().toString(),12);
        }
        redisUtils.addKeyByTime("momentCounts_" + id,12);
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
            circleCosMapper.insert(new CircleCos(null,x, cosPlay.getNumber(),null));
        }
        //把标签给redis做个推荐标签
        for(String x:label){
            redisUtils.addKeyByTime("recommendLabel_" + x,48);
        }
        //发布之后，给所有关注的人新内容+1
        if(permission != 3){
            //不是仅自己可见就有推送
            List<Long> fansIdList = fansMapper.getAllFansId(id);
            for(Long x:fansIdList){
                redisUtils.addKeyByTime("followNoRead_" + x,999);
            }
        }
        //es更新
        rabbitmqProducerService.sendEsMessage(new EsMsg(cosPlay.getNumber(),1));
        log.info("生成cos成功");
        return "success";
    }


    @Override
    public JSONObject getCosTopic(Long id, Long number) {
        CosPlay cosPlay = cosPlayMapper.selectById(number);
        if(cosPlay == null){
            log.error("获取cos内容失败，cos不存在");
            return null;
        }
        User user = userMapper.selectById(cosPlay.getId());
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
        List<String> cosPhoto = PhotoUtils.photoStringToList(cosPlay.getPhoto());
        List<String> label = circleCosMapper.getAllCircleNameFromCosNumber(number);
        CosForTopic cosForTopic = new CosForTopic(number, cosPlay.getId(),user.getUsername(),user.getPhoto(),user.getFansCounts(), cosPlay.getDescription(),cosPhoto,label, cosPlay.getCreateTime());
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
    public JSONObject getCosCountsList(Long id, List<Long> number) {
        JSONObject jsonObject = new JSONObject();
        List<CosCountsForList> cosCountsForList = new LinkedList<>();
        for(Long x:number){
            CosCounts cosCounts = new CosCounts(x,-1,-1,-1,-1,null);
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
            cosCountsForList1.setNumber(x);
            cosCountsForList.add(cosCountsForList1);
        }
        jsonObject.put("cosCountsList",cosCountsForList);
        log.info("获取cos计数信息成功");
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
        //推送
        User user = userMapper.selectById(id);
        if(user != null){
            rabbitmqProducerService.sendLikeMessage(new LikeMsg(cosComment.getNumber(),4,user.getUsername(),cosComment.getFromId()));
        }
        CosPlay cosPlay = cosPlayMapper.selectById(cosComment.getCosNumber());
        if(cosPlay != null){
            likeMessageMapper.insert(new LikeMessage(null,id,cosPlay.getId(),cosPlay.getNumber(),2,0,null));
            String ck1 = redisUtils.getValue("likeCounts_" + cosPlay.getId());
            if(ck1 == null){
                UserMessage userMessage = userMessageMapper.selectById(cosPlay.getId());
                if(userMessage != null){
                    redisUtils.saveByHoursTime("likeCounts_" + cosPlay.getId(),userMessage.getLikeCounts().toString(),12);
                }
            }
            redisUtils.addKeyByTime("likeCounts_" + cosPlay.getId(),12);
        }
        //增加点赞数
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
        if(fatherNumber == null){
            fatherNumber = 0L;
        }
        if(replyNumber == null){
            replyNumber = 0L;
        }
        if(toId == null){
            toId = 0L;
        }
        cosCommentMapper.insert(new CosComment(null,cosNumber,fatherNumber,id,toId,replyNumber,0,0,description,null));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        QueryWrapper<CosComment> wrapper = new QueryWrapper<>();
        wrapper.eq("cos_number",cosNumber)
                .eq("from_id",id)
                .eq("to_id",toId)
                .orderByDesc("create_time");
        List<CosComment> cosCommentList = cosCommentMapper.selectList(wrapper);
        CosComment cosComment1 = new CosComment(null,cosNumber,fatherNumber,id,toId,replyNumber,0,0,description,null);
        if(!cosCommentList.isEmpty()){
            cosComment1 = cosCommentList.get(0);
        }
        //添加父级评论评论数
        if(fatherNumber != 0){
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
        //推送
        User user = userMapper.selectById(id);
        CosPlay cosPlay = cosPlayMapper.selectById(cosNumber);
        if(fatherNumber == 0){
            //是cos下的评论
            //这个number给评论编号
            rabbitmqProducerService.sendCommentMessage(new CommentMsg(cosComment1.getNumber(), cosPlay.getId(),user.getUsername(),description));
            //添加评论
            commentMessageMapper.insert(new CommentMessage(null,id,cosPlay.getId(),description,cosPlay.getNumber(),0,1,null));
            String ck1 = redisUtils.getValue("commentCounts_" + cosPlay.getId());
            if(ck1 == null){
                UserMessage userMessage = userMessageMapper.selectById(cosPlay.getId());
                if(userMessage != null){
                    redisUtils.saveByHoursTime("commentCounts_" + cosPlay.getId(),userMessage.getCommentCounts().toString(),12);
                }
                redisUtils.addKeyByTime("commentCounts_" + cosPlay.getId(),12);
            }
        }else{
            CosComment cosComment;
            if(replyNumber == 0){
                //评论下的评论，给父级评论
                cosComment = cosCommentMapper.selectById(fatherNumber);
                rabbitmqProducerService.sendCommentMessage(new CommentMsg(fatherNumber,toId,user.getUsername(),description));
                commentMessageMapper.insert(new CommentMessage(null,id,cosComment.getFromId(),description,cosPlay.getNumber(),0,1,null));
                String ck1 = redisUtils.getValue("commentCounts_" + cosPlay.getId());
                if(ck1 == null){
                    UserMessage userMessage = userMessageMapper.selectById(cosPlay.getId());
                    if(userMessage != null){
                        redisUtils.saveByHoursTime("commentCounts_" + cosPlay.getId(),userMessage.getCommentCounts().toString(),12);
                    }
                    redisUtils.addKeyByTime("commentCounts_" + cosPlay.getId(),12);
                }
            }else{
                //评论下的回复评论
                cosComment = cosCommentMapper.selectById(replyNumber);
                log.info(user.getUsername());
                rabbitmqProducerService.sendAtMessage(new AtMsg(fatherNumber,user.getUsername(),toId));
                //@
                atMessageMapper.insert(new AtMessage(null,id,cosComment.getFromId(),description,cosPlay.getNumber(),0,1,null));
                String ck1 = redisUtils.getValue("atCounts_" + cosComment.getFromId());
                if(ck1 == null){
                    UserMessage userMessage = userMessageMapper.selectById(cosPlay.getId());
                    if(userMessage != null){
                        redisUtils.saveByHoursTime("atCounts_" + cosComment.getFromId(),userMessage.getCommentCounts().toString(),12);
                    }
                    redisUtils.addKeyByTime("atCounts_" + cosComment.getFromId(),12);
                }
            }
        }
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

    @Override
    public JSONObject getRecommendLabelList() {
        JSONObject jsonObject = new JSONObject();
        List<String> labelList = new LinkedList<>();
        for(int i = 1 ; i <= 20; i++){
            String ck = redisUtils.getValue("hotRecommendLabel_" + i);
            if(ck != null){
                labelList.add(ck.substring(15));
            }
        }
        int cnt = 0;
        //标签不够，补足20个
        while(labelList.size() < 20){
            labelList.add(RecommendUtils.recommendList.get(cnt));
            cnt ++;
        }
        jsonObject.put("cosRecommendLabelList",labelList);
        log.info("获取当前推荐标签成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public JSONObject getCosDayHotList(String time,Integer type) {
        JSONObject jsonObject = new JSONObject();
        List<CosForHot> cosForHotList = cosDayHotMapper.getDayHotList(time,type);
        for(CosForHot x:cosForHotList){
            User user = userMapper.selectById(x.getId());
            if(user != null){
                x.setUsername(user.getUsername());
                x.setPhoto(user.getPhoto());
            }
            CosPlay cosPlay = cosPlayMapper.selectById(x.getCosNumber());
            if(cosPlay != null){
                x.setCosPhoto(PhotoUtils.photoStringToList(cosPlay.getPhoto()));
                x.setDescription(cosPlay.getDescription());
            }
            List<String> circleNameList = circleCosMapper.getAllCircleNameFromCosNumber(x.getCosNumber());
            x.setCosLabel(circleNameList);
        }
        jsonObject.put("cosHotList",cosForHotList);
        log.info("获取cos日榜成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getCosMonthHotList(String time,Integer type) {
        JSONObject jsonObject = new JSONObject();
        List<CosForHot> cosForHotList = cosMonthMapper.getMonthHotList(time,type);
        for(CosForHot x:cosForHotList){
            CosPlay cosPlay = cosPlayMapper.selectById(x.getCosNumber());
            if(cosPlay != null){
                x.setCosPhoto(PhotoUtils.photoStringToList(cosPlay.getPhoto()));
                x.setDescription(cosPlay.getDescription());
            }
            User user = userMapper.selectById(x.getId());
            if(user != null){
                x.setUsername(user.getUsername());
                x.setPhoto(user.getPhoto());
            }
            List<String> circleNameList = circleCosMapper.getAllCircleNameFromCosNumber(x.getCosNumber());
            x.setCosLabel(circleNameList);
        }
        jsonObject.put("cosHotList",cosForHotList);
        log.info("获取cos月榜成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getFollowList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        Page<CosForFollow> page1 = new Page<>(page,cnt);
        List<CosForFollow> cosForFollowList = cosPlayMapper.getFollowCosList(id,page1);
        for(CosForFollow x:cosForFollowList){
            //用户信息
            User user = userMapper.selectById(x.getId());
            if(user != null){
                x.setUsername(user.getUsername());
                x.setId(user.getId());
                x.setPhoto(user.getPhoto());
            }
            //cos信息
            CosPlay cosPlay = cosPlayMapper.selectById(x.getNumber());
            if(cosPlay != null){
                x.setCosPhoto(PhotoUtils.photoStringToList(cosPlay.getPhoto()));
                x.setCreateTime(cosPlay.getCreateTime());
            }
            //标签
            List<String> label = circleCosMapper.getAllCircleNameFromCosNumber(x.getNumber());
            x.setLabel(label);
        }
        jsonObject.put("cosFollowList",cosForFollowList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        //把redis的未读清掉
        redisUtils.delete("followNoRead_" + id);
        log.info("获取关注cos列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getFollowNoRead(Long id) {
        JSONObject jsonObject = new JSONObject();
        String cnt = redisUtils.getValue("followNoRead_" + id);
        if(cnt == null){
            jsonObject.put("noReadCounts",0);
        }else{
            jsonObject.put("noReadCounts",cnt);
        }
        log.info("获取关注未读数成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public String patchCos(Long id, Long number, String description, List<String> cosPhoto, Integer permission) {
        CosPlay cosPlay = cosPlayMapper.selectById(number);
        if(cosPlay == null){
            log.error("修改cos失败，cos不存在");
            return "existWrong";
        }
        if(!cosPlay.getId().equals(id)){
            log.error("修改cos失败，号主不对");
            return "userWrong";
        }
        if(description != null && !description.equals("")){
            cosPlay.setDescription(description);
        }
        if(cosPhoto != null){
            cosPlay.setPhoto(PhotoUtils.photoListToString(cosPhoto));
        }
        if(permission != null){
            cosPlay.setPermission(permission);
        }
        //更新cos
        cosPlayMapper.updateById(cosPlay);
        //es更新
        rabbitmqProducerService.sendEsMessage(new EsMsg(number,1));
        log.info("修改cos成功");
        return "success";
    }

}