package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class CosCountsForList implements Serializable {

    private Long number;

    private Integer commentCounts;

    private Integer likeCounts;

    private Integer favorCounts;

    private Integer shareCounts;

}
