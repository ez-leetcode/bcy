package com.bcy.userpart.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@ApiModel(description = "问答回答类")
public class QaAnswer implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    @ApiModelProperty("回答编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("问答编号")
    private Long qaNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("图片")
    private String photo;

    @ApiModelProperty("内容")
    private String description;

    @ApiModelProperty("点赞数")
    private Integer likeCounts;

    @ApiModelProperty("评论数")
    private Integer commentCounts;

    @ApiModelProperty("创建时间")
    private Date createTime;

}