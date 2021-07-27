package com.bcy.userpart.pojo;

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
@ApiModel(description = "用户反馈类")
public class Feedback implements Serializable {

    @ApiModelProperty("反馈编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.ID_WORKER)
    private Long number;

    @ApiModelProperty("反馈者id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    @ApiModelProperty("反馈内容")
    private String description;

    @ApiModelProperty("是否已读")
    private Integer isRead;

    @ApiModelProperty("反馈时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
