package com.bcy.quartz.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Circle implements Serializable {

    @TableId
    private String circleName;

    private String description;

    private String photo;

    private String nickName;

    private Integer postCounts;

    private Integer followCounts;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}