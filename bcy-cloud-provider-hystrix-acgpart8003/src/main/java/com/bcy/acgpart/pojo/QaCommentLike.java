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
@ApiModel(description = "回答底下评论的点赞类")
public class QaCommentLike implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("点赞编号")
    private Long number;

    @ApiModelProperty("评论编号")
    private Long commentNumber;

    @ApiModelProperty("点赞人id")
    private Long id;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("点赞时间")
    private Date createTime;

}
