package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCountsForList implements Serializable {

    private Long id;

    private Integer fansCounts;

    private Integer followCounts;

    private Integer momentCounts;

}
