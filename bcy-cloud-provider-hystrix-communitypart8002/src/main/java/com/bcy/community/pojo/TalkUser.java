package com.bcy.community.pojo;

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

@ApiModel(description = "用户聊天角色类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TalkUser implements Serializable {

    @ApiModelProperty("用户id1")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id1;

    @ApiModelProperty("用户id2")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id2;

    @ApiModelProperty("用户1未读数")
    private Integer id1Read;

    @ApiModelProperty("用户2未读数")
    private Integer id2Read;

    @ApiModelProperty("用户1删除")
    private Integer id1Deleted;

    @ApiModelProperty("用户2删除")
    private Integer id2Deleted;

    @ApiModelProperty("最后一次发言")
    private String lastTalk;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("最近一次更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}