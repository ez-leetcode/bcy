package com.bcy.acgpart.pojo;

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
@ApiModel(description = "回答的评论类")
public class QaComment implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("评论编号")
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("回答编号")
    private Long answerNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("父级评论编号")
    private Long fatherNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("评论者id")
    private Long fromId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("被回复者id，没有就为空")
    private Long toId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("被回复评论编号")
    private Long replyNumber;

    @ApiModelProperty("点赞数")
    private Integer likeCounts;

    @ApiModelProperty("下层评论数")
    private Integer commentCounts;

    @ApiModelProperty("评论内容")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
