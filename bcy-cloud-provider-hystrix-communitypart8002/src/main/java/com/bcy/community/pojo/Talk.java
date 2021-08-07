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
@ApiModel(description = "用户聊天类")
public class Talk implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    @ApiModelProperty("聊天编号")
    private Long number;

    @ApiModelProperty("来自用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    @ApiModelProperty("接收用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    @ApiModelProperty("来自用户删除")
    private Integer fromDeleted;

    @ApiModelProperty("接收用户删除")
    private Integer toDeleted;

    @ApiModelProperty("内容")
    private String info;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("创建时间")
    private Date createTime;

}
