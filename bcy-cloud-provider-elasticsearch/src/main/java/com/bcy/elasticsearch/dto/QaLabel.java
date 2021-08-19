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

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@ApiModel(description = "问答标签类")
public class QaLabel implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("标签编号")
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("问答编号")
    private Long qaNumber;

    @ApiModelProperty("标签内容")
    private String label;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("创建时间")
    private Date createTime;

}
