package com.bcy.acgpart.pojo;

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
@ApiModel(description = "用户收到点赞类")
public class LikeMessage implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    @ApiModelProperty("收到点赞编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @ApiModelProperty("来自用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    @ApiModelProperty("被点赞用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    @ApiModelProperty("评论/cos/问答编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cosOrQaNumber;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("是否已读")
    private Integer isRead;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("点赞时间")
    private Date createTime;

}