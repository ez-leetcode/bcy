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
public class AtMessageForList implements Serializable {

    private Long id;

    private String username;

    private String photo;

    private String description;

    private Integer type;

    private String info;

    private Integer isRead;

    private Date createTime;

}
