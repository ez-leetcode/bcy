package com.bcy.acgpart.pojo;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("点赞编号")
    private Long number;

    @ApiModelProperty("评论编号")
    private Long commentNumber;

    @ApiModelProperty("点赞人id")
    private Long id;

    @ApiModelProperty("点赞时间")
    private Date createTime;

}
