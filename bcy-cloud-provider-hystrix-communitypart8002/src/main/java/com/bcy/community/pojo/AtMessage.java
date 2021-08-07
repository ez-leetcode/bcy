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
@ApiModel(description = "用户@信息类")
public class AtMessage implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    @ApiModelProperty("at编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @ApiModelProperty("来自用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    @ApiModelProperty("被at用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    @ApiModelProperty("内容")
    private String description;

    @ApiModelProperty("cos或问答编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cosOrQaNumber;

    @ApiModelProperty("是否已读")
    private Integer isRead;

    @ApiModelProperty("类型")
    private Integer type;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("创建时间")
    private Date createTime;

}