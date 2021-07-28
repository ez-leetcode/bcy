package com.bcy.userpart.service;

import org.springframework.web.multipart.MultipartFile;

public interface PersonalService {

    String userPhotoUpload(MultipartFile file,Long id);

    String changeInfo(Long id,String username,String sex,String description,String province,String city,String birthday);

}
