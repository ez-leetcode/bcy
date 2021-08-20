package com.bcy.elasticsearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.service.CircleService;
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

@Api(tags = "圈子推荐 里面有推荐的热门圈子（根据历史搜索关键词相关性）")
@Slf4j
@RestController
public class CircleController {

    @Autowired
    private CircleService circleService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "获取推荐圈子列表（根据历史搜索推荐的，所以要登录才能用）",notes = "success：成功 返回recommendCircleList（circleName：圈子名 description：描述 " +
            "photo：图片 nickName：昵称 postCounts：发布数 followCounts：关注数 createTime：创建时间） ")
    @GetMapping("/es/recommendCircle")
    public Result<JSONObject> getRecommendCircle(@RequestParam("id") Long id,@RequestParam("cnt") Integer cnt,
                                                 @RequestParam("page") Integer page){
        log.info("正在获取推荐圈子列表，用户：" + id + " 当前页面：" + cnt + " 页面数据量：" + page);
        return ResultUtils.getResult(circleService.getRecommendCircle(id,cnt,page),"success");
    }

}
