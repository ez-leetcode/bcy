package com.bcy.userpart.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class OssUtils {

    private static final String endpoint = "oss-cn-shenzhen.aliyuncs.com";

    private static final String accessKeyId = "LTAI5tAEUYp3P4DMn85ekabBxxxxxx";

    private static final String accessKeySecret = "luRzwpshRD1AHqEldvXe4XSKhu8Fmmxxxxxx";

    private static final String bucket = "rat-bcy";

    //上传文件方法
    public static String uploadPhoto(MultipartFile file, String tableName){
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        log.info("已建立oss连接");
        String fileName = UUID.randomUUID().toString();
        //获取头像上传类型
        if(file.getOriginalFilename() == null){
            log.warn("上传失败，文件名为空");
            return "fileWrong";
        }
        String suffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if(!(suffixName.equals("jpg") || suffixName.equals("jpeg") || suffixName.equals("png"))){
            log.warn("上传失败，格式错误：" + suffixName);
            return "typeWrong";
        }
        String objectName = tableName + "/" + fileName + "." + suffixName;
        //创建上传文件元信息，可通过链接直接访问
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpg");
        //上传文件
        try{
            ossClient.putObject(bucket,objectName,file.getInputStream(),objectMetadata);
        }catch (IOException e){
            log.warn("上传失败，文件出错");
            e.printStackTrace();
        }
        ossClient.shutdown();
        log.info("关闭oss成功");
        return "http://" + bucket + "." + endpoint + "/" + objectName;
    }

    //通用文件删除方法
    public static void deletePhoto(String objectName,String tableName){
        log.info("正在尝试删除文件");
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        log.info("已建立oss连接");
        ossClient.deleteObject(bucket,tableName + "/" + objectName);
        log.info("文件已被删除");
        ossClient.shutdown();
        log.info("oss连接已关闭");
    }

}
