package com.bcy.quartz.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
public class UserMessage implements Serializable {

    @TableId
    private Long id;

    private Integer atCounts;

    private Integer commentCounts;

    private Integer likeCounts;

    private Integer messageCounts;

    private Date createTime;

    private Date updateTime;

}
