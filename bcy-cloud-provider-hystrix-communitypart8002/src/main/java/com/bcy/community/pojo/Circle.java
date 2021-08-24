package com.bcy.community.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(description = "圈子类")
public class Circle implements Serializable {

    @TableId
    @ApiModelProperty("圈子名称")
    private String circleName;

    @ApiModelProperty("圈子简介")
    private String description;

    @ApiModelProperty("圈子图片url")
    private String photo;

    @ApiModelProperty("成员昵称")
    private String nickName;

    @ApiModelProperty("帖子数")
    private Integer postCounts;

    @ApiModelProperty("关注成员数")
    private Integer followCounts;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}