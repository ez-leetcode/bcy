package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CosForTopic implements Serializable {

    private Long number;

    private Long id;

    private String username;

    private String photo;

    private Integer fansCounts;

    private String description;

    private List<String> cosPhoto;

    private List<String> label;

    private Date createTime;

}
