package com.bcy.community.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "用户聊天类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TalkMessage implements Serializable{

    @ApiModelProperty("聊天编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.ID_WORKER)
    private Long number;

    @ApiModelProperty("来自用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    @ApiModelProperty("接收用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    @ApiModelProperty("消息内容")
    private String message;

    @ApiModelProperty("消息编号，确保消息队列消费的幂等性")
    private String uuId;

    @ApiModelProperty("来自用户是否删除")
    private Integer fromDeleted;

    @ApiModelProperty("接收用户是否删除")
    private Integer toDeleted;

    @ApiModelProperty("消息时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}