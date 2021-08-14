package com.bcy.quartz.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class CosPlay implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String description;

    private String photo;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}