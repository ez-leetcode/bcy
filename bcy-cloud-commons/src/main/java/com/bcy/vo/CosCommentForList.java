package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CosCommentForList implements Serializable {

    private Long number;

    private Long id;

    private String username;

    private String photo;

    private String description;

    private Date createTime;

}
