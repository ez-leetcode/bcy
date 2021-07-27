package com.bcy.quartz.pojo;

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
public class Helps implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    private String question;

    private String answer;

    private Integer type;

    private Integer solveCounts;

    private Integer noSolveCounts;

    private Date createTime;

}