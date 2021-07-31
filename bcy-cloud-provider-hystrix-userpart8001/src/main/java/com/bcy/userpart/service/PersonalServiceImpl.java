package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.vo.PersonalInfo;
import com.bcy.vo.PersonalSetting;
import com.bcy.userpart.mapper.UserMapper;
import com.bcy.userpart.mapper.UserSettingMapper;
import com.bcy.userpart.pojo.User;
import com.bcy.userpart.pojo.UserSetting;
import com.bcy.userpart.utils.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class PersonalServiceImpl implements PersonalService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserSettingMapper userSettingMapper;

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
    public JSONObject getPersonalInfo(Long id) {
        User user = userMapper.selectById(id);
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

}