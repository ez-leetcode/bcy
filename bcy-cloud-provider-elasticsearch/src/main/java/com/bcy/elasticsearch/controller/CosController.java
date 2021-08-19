package com.bcy.elasticsearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.service.CosService;
import com.bcy.elasticsearch.service.SyncService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "cos搜索类")
@Slf4j
@RestController
public class CosController {

    @Autowired
    private CosService cosService;

    @Autowired
    private SyncService syncService;

    @PostMapping("/es/test")
    public Result<JSONObject> test(@RequestParam("number") Long number)throws IOException {
        syncService.update(number,2);
        return ResultUtils.getResult(new JSONObject(), "success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword",value = "关键词",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "long",paramType = "query")
    })
    @ApiOperation(value = "cos搜索（会匹配标签内容和描述 拼音分词（搜拼音也能搜到） + ik细粒度分词）",notes = "success：成功 返回cosList（number：cos编号 id：发布用户id " +
            "username：发布用户昵称 photo：头像 description：内容 label：标签（这里是一个字符串list（es存储的问题，就是在list的[]旁边多了引号其他不变） cosPhoto：cos图片（字符串list） createTime:发布时间））")
    @PostMapping("/es/searchCos")
    public Result<JSONObject> searchCos(@RequestParam("keyword") String keyword,@RequestParam("cnt") Integer cnt,
                                    @RequestParam("page") Integer page)throws IOException{
        return ResultUtils.getResult(cosService.searchCos(keyword, cnt, page),"success");
    }


}
