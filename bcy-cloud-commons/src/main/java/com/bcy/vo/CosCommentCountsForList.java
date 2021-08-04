package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CosCommentCountsForList implements Serializable {

    private Long number;

    private Integer likeCounts;

    private Integer commentCounts;
}
