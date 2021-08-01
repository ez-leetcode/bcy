package com.bcy.oauth2.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ApiModel(description = "角色实例类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role {

    @ApiModelProperty("角色id编号")
    private Integer id;

    @ApiModelProperty("角色名")
    private String roleName;

    @ApiModelProperty("创建日期")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("最近更新日期")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
