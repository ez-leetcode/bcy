package com.bcy.userpart.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@ApiModel(description = "用户基本信息实例类")
public class User {

    @TableId
    @ApiModelProperty("用户编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("用户昵称")
    private String username;

    @ApiModelProperty("用户性别")
    private String sex;

    @ApiModelProperty("头像")
    private String photo;

    @ApiModelProperty("自我介绍")
    private String description;

    @ApiModelProperty("所在省")
    private String province;

    @ApiModelProperty("所在市")
    private String city;

    @ApiModelProperty("生日")
    private Date birthday;

    @ApiModelProperty("动态数")
    private Integer momentCounts;

    @ApiModelProperty("粉丝数")
    private Integer fansCounts;

    @ApiModelProperty("关注数")
    private Integer followCounts;

    @ApiModelProperty("是否被封号")
    @TableLogic
    private Integer deleted;
}