package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserLikeForList implements Serializable {

    private Long id;

    private String username;

    private String photo;

    private Integer type;

    private Long number;

    private String info;

    private Integer isRead;

    private Date createTime;

}
