package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AskForAnswer {

    private Long number;

    private Long fromId;

    private String username;

    private String photo;

    private String question;

    private Date createTime;

}
