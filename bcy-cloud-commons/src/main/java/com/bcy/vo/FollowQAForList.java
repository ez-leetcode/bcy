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
public class FollowQAForList implements Serializable {

    private Long id;

    private String username;

    private String photo;

    private Integer fansCounts;

}
