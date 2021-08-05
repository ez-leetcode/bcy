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
public class CosCommentCommentForList implements Serializable {

    private Long number;

    private Long fromId;

    private String fromUsername;

    private String fromPhoto;

    private String description;

    private Long toId;

    private String toUsername;

    private Date createTime;

}
