package com.bcy.websocket.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserSetting implements Serializable {

    @TableId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private Integer pushComment;

    private Integer pushLike;

    private Integer pushFans;

    private Integer pushSystem;

    private Integer pushInfo;

    private Integer deleted;

    private Date updateTime;

}