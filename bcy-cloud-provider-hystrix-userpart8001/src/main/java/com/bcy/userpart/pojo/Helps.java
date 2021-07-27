package com.bcy.userpart.pojo;

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

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@ApiModel(description = "用户帮助实例类")
public class Helps implements Serializable {

    @ApiModelProperty("帮助编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.ID_WORKER)
    private Long number;

    @ApiModelProperty("帮助问题")
    private String question;

    @ApiModelProperty("帮助内容")
    private String answer;

    @ApiModelProperty("问题分类")
    private Integer type;

    @ApiModelProperty("已解决次数")
    private Integer solveCounts;

    @ApiModelProperty("未解决次数")
    private Integer noSolveCounts;

    @ApiModelProperty("帮助创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}