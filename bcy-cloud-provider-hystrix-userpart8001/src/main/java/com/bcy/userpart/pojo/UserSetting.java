package com.bcy.userpart.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@ApiModel(description = "用户个人设置实例类（业务垂直拆分）")
public class UserSetting {

    @ApiModelProperty("用户编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("推送评论与@")
    private Integer pushComment;

    @ApiModelProperty("推送点赞")
    private Integer pushLike;

    @ApiModelProperty("推送粉丝")
    private Integer pushFans;

    @ApiModelProperty("推送系统通知")
    private Integer pushSystem;

    @ApiModelProperty("推送消息")
    private Integer pushInfo;

    @ApiModelProperty("伪删除")
    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("最近更新时间")
    private Date updateTime;

}