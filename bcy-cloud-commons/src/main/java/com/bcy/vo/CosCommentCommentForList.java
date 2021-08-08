package com.bcy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    private String fromUsername;

    private String fromPhoto;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    private String toUsername;

    private Date createTime;

}
