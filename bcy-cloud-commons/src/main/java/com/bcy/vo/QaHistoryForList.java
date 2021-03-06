package com.bcy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//是不是还要有标签

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QaHistoryForList implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String username;

    private String photo;

    private String description;

    private String title;

    private List<String> qaPhoto;

    private Date createTime;

}