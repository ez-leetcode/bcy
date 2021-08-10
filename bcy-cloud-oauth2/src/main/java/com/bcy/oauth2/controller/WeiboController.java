package com.bcy.oauth2.controller;

import com.bcy.oauth2.service.WeiboService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "微博登录管理类")
@Slf4j
@RestController
public class WeiboController {

    @Autowired
    private WeiboService weiboService;

    @ApiOperation(value = "微博登录回调",notes = "这条新浪官方使用，你们不用")
    @GetMapping("/oauth/weiboCallBack")
    public String weiboLogin(@RequestParam("code") String code){
        log.info("正在进行微博登录回调，代码：" + code);
        return weiboService.weiboLogin(code);
    }

}
