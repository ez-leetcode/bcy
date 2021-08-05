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
public class QAAnswerCommentForList implements Serializable {

    private Long number;

    private Long id;

    private String username;

    private String photo;

    private String description;

    private Date createTime;

}
