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
public class JudgeCircleFollowForList implements Serializable {

    private String circleName;

    private Integer isFollow;

}
