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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class QAServiceImpl implements QAService{

    @Autowired
    private QaMapper qaMapper;

    @Autowired
    private QaFollowMapper qaFollowMapper;

    @Autowired
    private QaCommentMapper qaCommentMapper;

    @Autowired
    private QaCommentLikeMapper qaCommentLikeMapper;

    @Autowired
    private QaAnswerMapper qaAnswerMapper;

    @Autowired
    private QaAnswerLikeMapper qaAnswerLikeMapper;

    @Autowired
    private QaLabelMapper qaLabelMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private QaHistoryMapper qaHistoryMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String followQA(Long id, Long number) {
        Qa qa = qaMapper.selectById(number);
        if(qa == null){
            log.error("关注问答失败，问答不存在");
            return "existWrong";
        }
        QueryWrapper<QaFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("qa_number",number)
                .eq("id",id);
        QaFollow qaFollow = qaFollowMapper.selectOne(wrapper);
        if(qaFollow != null){
            log.error("关注问题失败，问题已被关注");
            return "repeatWrong";
        }
        //插入记录
        qaFollowMapper.insert(new QaFollow(null,number,id,null));
        String ck = redisUtils.getValue("followQA_" + number);
        if(ck == null){
            //redis中没存，存入redis
            redisUtils.saveByHoursTime("followQA_" + number,qa.getFollowCounts().toString(),12);
        }
        //问答关注数加1，等定时调度
        redisUtils.addKeyByTime("followQA_" + number,12);
        log.info("关注问答成功");
        return "success";
    }

    @Override
    public String disFollowQA(Long id, Long number) {
        Qa qa = qaMapper.selectById(number);
        if(qa == null){
            log.error("取消关注问答失败，问答不存在");
            return "existWrong";
        }
        QueryWrapper<QaFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("qa_number",number)
                .eq("id",id);
        QaFollow qaFollow = qaFollowMapper.selectOne(wrapper);
        if(qaFollow == null){
            log.error("取消关注问题失败，问题已被关注");
            return "repeatWrong";
        }
        //插入记录
        qaFollowMapper.deleteById(qaFollow);
        String ck = redisUtils.getValue("followQA_" + number);
        if(ck == null){
            //redis中没存，存入redis
            redisUtils.saveByHoursTime("followQA_" + number,qa.getFollowCounts().toString(),12);
        }
        //问答关注数减1，等定时调度
        redisUtils.subKeyByTime("followQA_" + number,12);
        log.info("取消关注问答成功");
        return "success";
    }

    @Override
    public String likeAnswer(Long id, Long number) {
        QaAnswer qaAnswer = qaAnswerMapper.selectById(number);
        if(qaAnswer == null){
            log.error("点赞回答失败，回答不存在");
            return "existWrong";
        }
        QueryWrapper<QaAnswerLike> wrapper = new QueryWrapper<>();
        wrapper.eq("answer_number",number)
                .eq("id",id);
        QaAnswerLike qaAnswerLike = qaAnswerLikeMapper.selectOne(wrapper);
        if(qaAnswerLike != null){
            log.error("点赞回答失败，回答已被点赞");
            return "repeatWrong";
        }
        //插入点赞
        qaAnswerLikeMapper.insert(new QaAnswerLike(null,number,id,null));
        String ck = redisUtils.getValue("likeQaAnswer_" + number);
        if(ck == null){
            //如果没有存入redis
            redisUtils.saveByHoursTime("likeQaAnswer_" + number,qaAnswer.getLikeCounts().toString(),12);
        }
        //redis加1
        redisUtils.addKeyByTime("likeQaAnswer_" + number,12);
        log.info("点赞回答成功");
        return "success";
    }

    @Override
    public String dislikeAnswer(Long id, Long number) {
        QaAnswer qaAnswer = qaAnswerMapper.selectById(number);
        if(qaAnswer == null){
            log.error("取消点赞回答失败，回答不存在");
            return "existWrong";
        }
        QueryWrapper<QaAnswerLike> wrapper = new QueryWrapper<>();
        wrapper.eq("answer_number",number)
                .eq("id",id);
        QaAnswerLike qaAnswerLike = qaAnswerLikeMapper.selectOne(wrapper);
        if(qaAnswerLike == null){
            log.error("取消点赞回答失败，回答未点赞");
            return "repeatWrong";
        }
        //删除点赞
        qaAnswerLikeMapper.deleteById(qaAnswerLike);
        String ck = redisUtils.getValue("likeQaAnswer_" + number);
        if(ck == null){
            //如果没有存入redis
            redisUtils.saveByHoursTime("likeQaAnswer_" + number,qaAnswer.getLikeCounts().toString(),12);
        }
        //redis减1
        redisUtils.subKeyByTime("likeQaAnswer_" + number,12);
        log.info("取消点赞回答成功");
        return "success";
    }

    @Override
    public String likeComment(Long id, Long number) {
        QaComment qaComment = qaCommentMapper.selectById(number);
        if(qaComment == null){
            log.error("点赞评论失败，评论不存在");
            return "existWrong";
        }
        QueryWrapper<QaCommentLike> wrapper = new QueryWrapper<>();
        wrapper.eq("comment_number",number)
                .eq("id",id);
        QaCommentLike qaCommentLike = qaCommentLikeMapper.selectOne(wrapper);
        if(qaCommentLike != null){
            log.error("点赞评论失败，评论已被点赞");
            return "repeatWrong";
        }
        //添加点赞
        qaCommentLikeMapper.insert(new QaCommentLike(null,number,id,null));
        String ck = redisUtils.getValue("likeQaComment_" + number);
        if(ck == null){
            //如果没有存入redis
            redisUtils.saveByHoursTime("likeQaComment_" + number,qaComment.getLikeCounts().toString(),12);
        }
        //redis加1
        redisUtils.addKeyByTime("likeQaComment_" + number,12);
        log.info("点赞评论成功");
        return "success";
    }

    @Override
    public String dislikeComment(Long id, Long number) {
        QaComment qaComment = qaCommentMapper.selectById(number);
        if(qaComment == null){
            log.error("取消点赞评论失败，评论不存在");
            return "existWrong";
        }
        QueryWrapper<QaCommentLike> wrapper = new QueryWrapper<>();
        wrapper.eq("comment_number",number)
                .eq("id",id);
        QaCommentLike qaCommentLike = qaCommentLikeMapper.selectOne(wrapper);
        if(qaCommentLike == null){
            log.error("取消点赞评论失败，评论未被点赞");
            return "repeatWrong";
        }
        //删除点赞
        qaCommentLikeMapper.deleteById(qaCommentLike);
        String ck = redisUtils.getValue("likeQaComment_" + number);
        if(ck == null){
            //如果没有存入redis
            redisUtils.saveByHoursTime("likeQaComment_" + number,qaComment.getLikeCounts().toString(),12);
        }
        //redis减1
        redisUtils.subKeyByTime("likeQaComment_" + number,12);
        log.info("取消点赞评论成功");
        return "success";
    }


    @Override
    public JSONObject getFollowQAList(Long id, Long number, String keyword, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        Page<FollowQAForList> page1 = new Page<>(page,cnt);
        List<FollowQAForList> followQAForListList;
        if(keyword.equals("")){
            followQAForListList = qaFollowMapper.getFollowQAList(number,page1);
        }else{
            followQAForListList = qaFollowMapper.getFollowQAListByKeyWord(keyword,number,page1);
        }
        //从redis调取数据，因为会有一部分数据还没更新，这里给最新的数据
        for(FollowQAForList x:followQAForListList){
            String ck = redisUtils.getValue("fansCounts_" + x.getId());
            if(ck != null){
                x.setFansCounts(Integer.parseInt(ck));
            }
        }
        //粉丝数待改变
        jsonObject.put("followQAList",followQAForListList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取关注问答列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getQATopic(Long id, Long number) {
        QATopic qaTopic = new QATopic();
        JSONObject jsonObject = new JSONObject();
        Qa qa = qaMapper.selectById(number);
        if(qa == null){
            log.error("获取问答头部信息失败，问答不存在");
            return null;
        }
        QueryWrapper<QaLabel> wrapper = new QueryWrapper<>();
        wrapper.eq("qa_number",number);
        List<QaLabel> labelList = qaLabelMapper.selectList(wrapper);
        List<String> realLabelList = new ArrayList<>();
        for(QaLabel x:labelList){
            realLabelList.add(x.getLabel());
        }
        qaTopic.setLabel(realLabelList);
        qaTopic.setNumber(qa.getNumber());
        qaTopic.setTitle(qa.getTitle());
        qaTopic.setDescription(qa.getDescription());
        List<String> photoList = PhotoUtils.photoStringToList(qa.getPhoto());
        qaTopic.setPhoto(photoList);
        String ck = redisUtils.getValue("followQA_" + number);
        if(ck == null){
            //redis没有，更新redis
            redisUtils.saveByHoursTime("followQA_" + number,qa.getFollowCounts().toString(),12);
            qaTopic.setFollowCounts(qa.getFollowCounts());
        }else{
            //redis有用redis的
            qaTopic.setFollowCounts(Integer.parseInt(ck));
        }
        String ck1 = redisUtils.getValue("answerQA_" + number);
        if(ck1 == null){
            redisUtils.saveByHoursTime("answerQA_" + number,qa.getAnswerCounts().toString(),12);
            qaTopic.setAnswerCounts(qa.getAnswerCounts());
        }else{
            qaTopic.setFollowCounts(Integer.parseInt(ck1));
        }
        jsonObject.put("QATopic",qaTopic);
        //添加历史记录
        if(id != null){
            QueryWrapper<QaHistory> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("qa_number",number)
                    .eq("id",id);
            QaHistory qaHistory = qaHistoryMapper.selectOne(wrapper1);
            if(qaHistory == null){
                qaHistoryMapper.insert(new QaHistory(null,number,id,0,null));
            }else{
                qaHistory.setReClickCounts(qaHistory.getReClickCounts() + 1);
                qaHistoryMapper.updateById(qaHistory);
            }
        }
        log.info("获取问答头部信息成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    //历史记录待完成
    @Override
    public JSONObject getQAAnswerList(Long id, Long number, Integer type, Long cnt, Long page) {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<QaAnswer> wrapper = new QueryWrapper<>();
        wrapper.eq("qa_number",number);
        if(type == 1){
            wrapper.orderByDesc("like_counts");
        }else{
            wrapper.orderByDesc("create_time");
        }
        Page<QaAnswer> page1 = new Page<>(page,cnt);
        qaAnswerMapper.selectPage(page1,wrapper);
        List<QaAnswer> qaAnswerList = page1.getRecords();
        List<QAAnswerForList> qaAnswerForList = new LinkedList<>();
        for(QaAnswer x:qaAnswerList){
            QAAnswerForList qaAnswerForList1 = new QAAnswerForList(x.getNumber(),x.getId(),null,null,x.getDescription(),null,x.getCreateTime());
            List<String> answerPhoto = PhotoUtils.photoStringToList(x.getPhoto());
            qaAnswerForList1.setAnswerPhoto(answerPhoto);
            User user = userMapper.selectById(x.getId());
            if(user != null){
                qaAnswerForList1.setPhoto(user.getPhoto());
                qaAnswerForList1.setUsername(user.getUsername());
            }
        }
        jsonObject.put("answerList",qaAnswerForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取问答回答列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public JSONObject getAnswerCommentList(Long id, Long answerNumber, Long page, Long cnt,Integer type) {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<QaComment> wrapper = new QueryWrapper<>();
        wrapper.eq("answer_number",answerNumber)
                //不是子评论
                .eq("father_number",0);
        if(type == 1){
            wrapper.orderByDesc("like_counts");
        }else{
            wrapper.orderByDesc("create_time");
        }
        Page<QaComment> page1 = new Page<>(page,cnt);
        List<QaComment> qaCommentList = page1.getRecords();
        List<QAAnswerCommentForList> qaAnswerCommentForList = new LinkedList<>();
        for(QaComment x:qaCommentList){
            QAAnswerCommentForList qaAnswerCommentForList1 = new QAAnswerCommentForList(x.getNumber(),x.getFromId(),null,null,x.getDescription(),x.getCreateTime());
            User user = userMapper.selectById(x.getFromId());
            if(user != null){
                qaAnswerCommentForList1.setUsername(user.getUsername());
                qaAnswerCommentForList1.setPhoto(user.getPhoto());
            }
            qaAnswerCommentForList.add(qaAnswerCommentForList1);
        }
        jsonObject.put("answerCommentList",qaAnswerCommentForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取回答下的评论列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public JSONObject getAnswerCommentCommentList(Long id, Long number, Long page, Long cnt, Integer type) {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<QaComment> wrapper = new QueryWrapper<>();
        Page<QaComment> page1 = new Page<>(page,cnt);
        wrapper.eq("father_number",number);
        if(type == 1){
            wrapper.orderByDesc("like_counts");
        }else{
            wrapper.orderByDesc("create_time");
        }
        qaCommentMapper.selectPage(page1,wrapper);
        List<QaComment> qaCommentList = page1.getRecords();
        List<QACommentCommentForList> qaCommentCommentForList = new LinkedList<>();
        for(QaComment x:qaCommentList){
            QACommentCommentForList qaCommentCommentForList1 = new QACommentCommentForList(x.getNumber(),x.getFromId(),null,null,x.getDescription(),x.getToId(),null,x.getCreateTime());
            User user1 = userMapper.selectById(x.getToId());
            User user = userMapper.selectById(x.getFromId());
            if(user != null){
                qaCommentCommentForList1.setFromUsername(user.getUsername());
                qaCommentCommentForList1.setFromPhoto(user.getPhoto());
            }
            if(user1 != null){
                qaCommentCommentForList1.setToUsername(user1.getUsername());
            }
            qaCommentCommentForList.add(qaCommentCommentForList1);
        }
        jsonObject.put("answerCommentCommentList",qaCommentCommentForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取问答下回答的评论的评论列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public String generateQA(Long id, String title, String description, List<String> photo) {
        //图片转换
        String photoString = PhotoUtils.photoListToString(photo);
        qaMapper.insert(new Qa(null,id,title,description,photoString,0,0,null,null));
        log.info("生成新的问答成功");
        return "success";
    }

    @Override
    public String photoUpload(MultipartFile file) {
        String url = OssUtils.uploadPhoto(file,"QAPhoto");
        if(url.length() < 12){
            log.error("上传问答图片文件失败");
            return url;
        }
        //成功上传图片
        log.info("上传问答图片文件成功，url：" + url);
        return url;
    }

}