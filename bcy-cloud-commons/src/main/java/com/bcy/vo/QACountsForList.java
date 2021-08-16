package com.bcy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QACountsForList implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    private Integer followCounts;

    private Integer answerCounts;
}
