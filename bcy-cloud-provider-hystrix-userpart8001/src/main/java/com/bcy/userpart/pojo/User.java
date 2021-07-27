package com.bcy.userpart.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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
@ApiModel(description = "用户实例类")
public class User {

    @ApiModelProperty("用户编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("用户昵称")
    private String username;

    @ApiModelProperty("粉丝数")
    private Integer fansCounts;

    @ApiModelProperty("关注数")
    private Integer followCounts;

    @ApiModelProperty("是否被封号")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty("创建时间")
    private Date createTime;
}