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
public class PersonalSetting implements Serializable {

    private Long id;

    private Integer pushComment;

    private Integer pushLike;

    private Integer pushFans;

    private Integer pushSystem;

    private Integer pushInfo;

}
