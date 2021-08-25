package com.bcy.community.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(description = "问答类")
public class Qa implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    @ApiModelProperty("问答编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("提问的id")
    private Long id;

    @ApiModelProperty("问答标题")
    private String title;

    @ApiModelProperty("问答内容")
    private String description;

    @ApiModelProperty("图片")
    private String photo;

    @ApiModelProperty("关注数")
    private Integer followCounts;

    @ApiModelProperty("回答数")
    private Integer answerCounts;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("最近更新时间")
    private Date updateTime;

}