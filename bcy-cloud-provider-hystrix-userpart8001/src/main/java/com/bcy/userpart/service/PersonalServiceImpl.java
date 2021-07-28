package com.bcy.userpart.service;

import com.bcy.userpart.mapper.UserMapper;
import com.bcy.userpart.mapper.UserSettingMapper;
import com.bcy.userpart.pojo.User;
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
            log.error("修改信息失败，用户已被冻结");
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
}
