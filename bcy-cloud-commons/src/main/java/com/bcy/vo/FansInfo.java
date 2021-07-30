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
public class FansInfo implements Serializable {

    private Long id;

    private String username;

    private String photo;

    private String description;

    private String sex;

    private Integer fansCounts;

}
