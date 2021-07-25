package com.bcy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

//接入阿里云第三方api，进行垃圾话检测
@Slf4j
public class CommentUtils {

    public static boolean judgeComment(String comment){
        log.info("正在进行垃圾话文本检测");
        log.info("comment：" + comment);
        //直接用官方文档的样例代码就好啦~
        String host = "https://textfilter.xiaohuaerai.com";
        String path = "/textfilter";
        String method = "POST";
        String appcode = "20ec951bdb0c4856a568bf5aad6d1e07";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("src", comment);
        //可选严格：strict  宽松：easy
        bodys.put("type", "strict");
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //获取response的body
            String realResponse = EntityUtils.toString(response.getEntity());
            log.info(unicode2String(realResponse));
            JSONObject jsonObject = JSON.parseObject(realResponse);
            log.info(jsonObject.toString());
            String res = jsonObject.getString("res");
            log.info("res：" + res);
            if(res.equals("1")){
                log.info("文本检测为正常文本");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("垃圾话文本检测发生错误");
        }
        log.info("文本检测为异常文本");
        return true;
    }


    //传回来的是Unicode编码，我们转为中文
    private static String unicode2String(String unicode) {
        if (StringUtils.isBlank(unicode)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i;
        int pos = 0;

        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode, pos, i);
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        //如果pos位置后，有非中文字符，直接添加
        sb.append(unicode.substring(pos));

        return sb.toString();
    }

}
