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

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@ApiModel(description = "用户收藏类（cos）")
public class Favor {

    @TableId(type = IdType.ID_WORKER)
    @ApiModelProperty("收藏编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @ApiModelProperty("cos编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cosNumber;

    @ApiModelProperty("用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("收藏时间")
    private Date createTime;

}
