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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = "cos搜索类，这里面还有历史搜索的获取 清空等")
@Slf4j
@RestController
public class CosController {

    @Autowired
    private CosService cosService;

    @Autowired
    private SyncService syncService;

    @PostMapping("/es/test")
    public Result<JSONObject> test(@RequestParam("circleName") String circleName)throws IOException {
        syncService.updateCircle(circleName);
        return ResultUtils.getResult(new JSONObject(), "success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id（有登陆就带，没登录不带或者带0）",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键词",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "cos搜索（会匹配标签内容和描述 拼音分词（搜拼音也能搜到） + ik细粒度分词）",notes = "success：成功 返回cosList（number：cos编号 id：发布用户id " +
            "username：发布用户昵称 photo：头像 description：内容 label：标签（这里是一个字符串list（es存储的问题，就是在list的[]旁边多了引号其他不变） cosPhoto：cos图片（字符串list） createTime:发布时间））")
    @PostMapping("/es/searchCos")
    public Result<JSONObject> searchCos(@RequestParam(value = "id",required = false) Long id,
                                    @RequestParam("keyword") String keyword,@RequestParam("cnt") Integer cnt,
                                    @RequestParam("page") Integer page)throws IOException{
        return ResultUtils.getResult(cosService.searchCos(id,keyword, cnt, page),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "发现（推荐cos，在不同的获取是会有重复推荐的）",notes = "success：成功 （这里没有pages和counts参数 直接获取就行） cosList（参数和cos搜索接口那里的一样）")
    @GetMapping("/es/recommendCos")
    public Result<JSONObject> getRecommendCos(@RequestParam("id") Long id,@RequestParam("cnt") Integer cnt) throws IOException {
        log.info("正在获取发现cos，用户：" + id + " 页面数据量：" + cnt);
        return ResultUtils.getResult(cosService.getRecommendCos(id, cnt),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键词（写死绘画/cos/写作）",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "获取标题（绘画 cos 写作）下的cos内容（也是推荐制的，随机）",notes = "success：成功 cosList（参数和cos搜索接口那里的一样）")
    @GetMapping("/es/labelCos")
    public Result<JSONObject> getLabelCos(@RequestParam("id") Long id,@RequestParam("keyword") String keyword,
                                          @RequestParam("cnt") Integer cnt,@RequestParam("page") Integer page) throws IOException {
        log.info("获取标签下的cos列表，用户：" + id + " 关键词：" + keyword + " 当前页面：" + page + " 页面数据量：" + cnt);
        return ResultUtils.getResult(cosService.getLabelCos(id, keyword, cnt, page),"success");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取搜索历史",notes = "success：成功 keywordList（至多20个历史搜索）")
    @GetMapping("/es/searchHistory")
    public Result<JSONObject> getSearchHistory(@RequestParam("id") Long id){
        log.info("正在获取搜索历史，用户：" + id);
        return ResultUtils.getResult(cosService.getSearchKeywordList(id),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "搜索关键词",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "删除历史搜索",notes = "success：成功")
    @DeleteMapping("/es/searchHistory")
    public Result<JSONObject> deleteSearchHistory(@RequestParam("id") Long id,@RequestParam("keyword") String keyword){
        log.info("正在删除历史搜索，用户：" + id + " 搜索关键词：" + keyword);
        return ResultUtils.getResult(new JSONObject(), cosService.deleteHistory(id, keyword));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "清空所有历史记录",notes = "success：成功")
    @DeleteMapping("/es/allSearchHistory")
    public Result<JSONObject> deleteAllSearchHistory(@RequestParam("id") Long id){
        log.info("正在清空所有历史记录，用户：" + id);
        return ResultUtils.getResult(new JSONObject(), cosService.deleteAllHistory(id));
    }

}