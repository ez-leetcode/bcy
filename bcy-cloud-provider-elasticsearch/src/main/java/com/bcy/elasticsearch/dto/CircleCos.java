package com.bcy.elasticsearch.dto;

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

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@ApiModel(description = "圈子的cos类")
public class CircleCos{

    @TableId(type = IdType.ID_WORKER)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("圈子与cos关联编号")
    private Long number;

    @ApiModelProperty("圈子")
    private String circleName;

    @ApiModelProperty("cos编号")
    private Long cosNumber;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

}
