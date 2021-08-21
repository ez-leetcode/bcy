package com.bcy.elasticsearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.service.QAService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(tags = "问答管理类")
@Slf4j
@RestController
public class QAController {

    @Autowired
    private QAService qaService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id（游客带0或不带）",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键词",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "问答搜索（会匹配标签内容和描述 拼音分词（搜拼音也能搜到） + ik细粒度分词）",notes = "success：成功 返回qaList （number：问答编号 id：发布用户id username：发布用户昵称 " +
            "photo：头像 description：内容 title：标题 label：标签（字符串list） createTime：创建时间）")
    @PostMapping("/es/searchQa")
    public Result<JSONObject> searchQa(@RequestParam(value = "id",required = false) Long id,
                                        @RequestParam("keyword") String keyword, @RequestParam("cnt") Integer cnt,
                                        @RequestParam("page") Integer page)throws IOException {
        log.info("正在获取问答搜索，用户：" + id + " ");
        return ResultUtils.getResult(qaService.searchQa(id,keyword, cnt, page),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id（游客带0）",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "问答推荐（随机推荐）",notes = "success：成功 返回qaRecommendList （number：问答编号 id：发布用户id username：发布用户昵称 " +
            "photo：头像 description：内容 title：标题 label：标签（字符串list） createTime：创建时间）")
    @GetMapping("/es/recommendQa")
    public Result<JSONObject> getRecommendQa(@RequestParam(value = "id") Long id,@RequestParam("cnt") Integer cnt)throws IOException{
        log.info("正在获取问答推荐，用户：" + id + " 页面数据量：" + cnt);
        return ResultUtils.getResult(qaService.getRecommendQa(id,cnt),"success");
    }

}
