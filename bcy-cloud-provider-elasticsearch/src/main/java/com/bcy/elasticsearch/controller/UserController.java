package com.bcy.elasticsearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.service.UserService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户推荐")
@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取用户推荐（新用户注册完那里，按粉丝给的，点换一批就等于下一页就好了 1-3页来回循环（手机号不够用。。））",notes = "success：成功 返回 recommendUserList" +
            "（id：用户id username：昵称 photo：头像） 这里用了分页缓存技术 没有pages和counts 其实counts就是总用户数 为了不达到上限所以建议页面只取1-3页")
    @GetMapping("/es/recommendUser")
    public Result<JSONObject> getRecommendUser(@RequestParam("cnt") Long cnt,@RequestParam("page") Long page){
        log.info("正在获取用户推荐，当前页面：" + page + " 页面数据量：" + cnt);
        return ResultUtils.getResult(userService.getRecommendUser(page,cnt),"success");
    }

}
