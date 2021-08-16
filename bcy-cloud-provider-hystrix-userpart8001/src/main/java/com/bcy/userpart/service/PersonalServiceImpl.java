package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.userpart.mapper.UserLoginMapper;
import com.bcy.userpart.mapper.UserMessageMapper;
import com.bcy.userpart.pojo.UserLogin;
import com.bcy.userpart.pojo.UserMessage;
import com.bcy.userpart.utils.RedisUtils;
import com.bcy.vo.PersonalInfo;
import com.bcy.vo.PersonalSetting;
import com.bcy.userpart.mapper.UserMapper;
import com.bcy.userpart.mapper.UserSettingMapper;
import com.bcy.userpart.pojo.User;
import com.bcy.userpart.pojo.UserSetting;
import com.bcy.userpart.utils.OssUtils;
import com.bcy.vo.UserCountsForList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Service
@Slf4j
public class PersonalServiceImpl implements PersonalService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String userPhotoUpload(MultipartFile file, Long id) {
        String url = OssUtils.uploadPhoto(file,"userPhoto");
        if(url.length() >= 12){
            //上传成功
            User user = userMapper.selectById(id);
            //释放原来的资源
            if(user.getPhoto() != null){
                OssUtils.deletePhoto(user.getPhoto(),"userPhoto");
            }
            user.setPhoto(url);
            userMapper.updateById(user);
            log.info("用户头像上传成功");
        }
        return url;
    }

    @Override
    public String changeInfo(Long id, String username, String sex, String description, String province, String city, String birthday) {
        User user = userMapper.selectById(id);
        if(user == null){
            log.error("修改信息失败，用户已被冻结或不存在");
            return "existWrong";
        }
        user.setUsername(username);
        user.setSex(sex);
        user.setDescription(description);
        user.setProvince(province);
        user.setCity(city);
        if(birthday != null && !birthday.equals("")){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat.parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            log.info(date.toString());
            user.setBirthday(date);
        }
        //生日转化待完成
        int result = userMapper.updateById(user);
        log.info("修改用户信息成功，共修改了：" + result + "条");
        return "success";
    }

    @Override
    public String changeSetting(Long id, Integer pushComment, Integer pushLike, Integer pushFans, Integer pushSystem, Integer pushInfo) {
        UserSetting userSetting = userSettingMapper.selectById(id);
        if(userSetting == null){
            log.error("修改个人设置失败，用户已被冻结或不存在");
            return "existWrong";
        }
        userSetting.setPushComment(pushComment);
        userSetting.setPushLike(pushLike);
        userSetting.setPushFans(pushFans);
        userSetting.setPushSystem(pushSystem);
        userSetting.setPushInfo(pushInfo);
        int result = userSettingMapper.updateById(userSetting);
        log.info("修改个人设置成功，共修改了：" + result + "条");
        return "success";
    }


    @Override
    public JSONObject getPersonalSetting(Long id) {
        UserSetting userSetting = userSettingMapper.selectById(id);
        if(userSetting == null){
            log.error("获取用户个人设置失败，用户已被冻结或不存在");
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personalSetting",new PersonalSetting(userSetting.getId(),userSetting.getPushComment(),userSetting.getPushLike(),userSetting.getPushFans(),userSetting.getPushSystem(),userSetting.getPushInfo()));
        log.info("获取用户个人设置成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getPersonalInfo(Long id,String phone) {
        User user = null;
        if(id != null){
            user = userMapper.selectById(id);
        }else{
            QueryWrapper<UserLogin> wrapper = new QueryWrapper<>();
            wrapper.eq("phone",phone);
            UserLogin userLogin = userLoginMapper.selectOne(wrapper);
            if(userLogin != null){
                user = userMapper.selectById(userLogin.getId());
            }
        }
        if(user == null){
            log.error("获取个人信息失败，用户不存在或已被冻结");
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personalInfo",new PersonalInfo(user.getId(),user.getUsername(),user.getSex(),user.getPhoto(),user.getDescription(),user.getProvince(),user.getCity(),user.getBirthday()));
        log.info("获取用户个人信息成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getUserCounts(List<Long> userId) {
        JSONObject jsonObject = new JSONObject();
        List<UserCountsForList> userCountsForLists = new LinkedList<>();
        for(Long x:userId){
            UserCountsForList userCountsForList = new UserCountsForList(x,null,null,null);
            String fansCounts = redisUtils.getValue("fansCounts_" + x);
            String followCounts = redisUtils.getValue("followCounts_" + x);
            String momentCounts = redisUtils.getValue("momentCounts_" + x);
            User user = new User();
            if(followCounts == null || fansCounts == null || momentCounts == null){
                user = userMapper.selectById(x);
            }
            if(followCounts == null){
                redisUtils.saveByHoursTime("followCounts_" + x,user.getFollowCounts().toString(),12);
                userCountsForList.setFollowCounts(user.getFollowCounts());
            }else{
                userCountsForList.setFollowCounts(Integer.parseInt(followCounts));
            }
            if(fansCounts == null){
                redisUtils.saveByHoursTime("fansCounts_" + x,user.getFansCounts().toString(),12);
                userCountsForList.setFansCounts(user.getFansCounts());
            }else{
                userCountsForList.setFansCounts(Integer.parseInt(fansCounts));
            }
            if(momentCounts == null){
                redisUtils.saveByHoursTime("momentCounts_" + x,user.getMomentCounts().toString(),12);
                userCountsForList.setMomentCounts(user.getMomentCounts());
            }else{
                userCountsForList.setMomentCounts(Integer.parseInt(momentCounts));
            }
            userCountsForLists.add(userCountsForList);
        }
        jsonObject.put("userCountsList",userCountsForLists);
        log.info("获取用户计数数据成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public JSONObject judgeNew(Long id) {
        JSONObject jsonObject = new JSONObject();
        UserLogin userLogin = userLoginMapper.selectById(id);
        UserMessage userMessage = userMessageMapper.selectById(id);
        if(userLogin == null || userMessage == null){
            log.error("判断是否为新用户失败，用户不存在");
            return null;
        }
        if(userLogin.getCreateTime().equals(userLogin.getUpdateTime()) && userMessage.getCreateTime().equals(userMessage.getUpdateTime())){
            jsonObject.put("isNew",1);
        }else{
            jsonObject.put("isNew",0);
        }
        log.info("判断是否为新用户成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public String setPassword(Long id, String password) {
        UserLogin userLogin = userLoginMapper.selectById(id);
        if(userLogin == null){
            log.error("新用户修改密码失败，用户不存在");
            return "existWrong";
        }
        //这个修改密码不会强制下线
        userLogin.setPassword(new BCryptPasswordEncoder().encode(password));
        userLoginMapper.updateById(userLogin);
        log.info("新用户设置密码成功");
        return "success";
    }

}