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
public class UserNoReadCounts implements Serializable {

    private Long id;

    private Integer atCounts;

    private Integer commentCounts;

    private Integer likeCounts;

    private Integer messageCounts;

}
