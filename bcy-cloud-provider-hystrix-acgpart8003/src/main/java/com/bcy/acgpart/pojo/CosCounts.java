package com.bcy.acgpart.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(description = "cos类（计数部分，业务垂直拆分）")
public class CosCounts implements Serializable {

    @ApiModelProperty("cos编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @ApiModelProperty("评论数")
    private Integer commentCounts;

    @ApiModelProperty("点赞数")
    private Integer likeCounts;

    @ApiModelProperty("收藏数")
    private Integer favorCounts;

    @ApiModelProperty("分享数")
    private Integer shareCounts;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
