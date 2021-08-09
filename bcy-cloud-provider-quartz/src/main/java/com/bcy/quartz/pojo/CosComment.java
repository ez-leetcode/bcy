package com.bcy.quartz.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class CosComment implements Serializable {

    @TableId
    private Long number;

    private Long cosNumber;

    private Long fatherNumber;

    private Long fromId;

    private Long toId;

    private Long replyNumber;

    private Integer likeCounts;

    private Integer commentCounts;

    private String description;

    private Date createTime;

}