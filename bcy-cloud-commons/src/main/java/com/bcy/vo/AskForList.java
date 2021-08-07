package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AskForList implements Serializable {

    private Long number;

    private Long fromId;

    private String username;

    private String photo;

    private String question;

    private String answer;

    private Date createTime;

}
