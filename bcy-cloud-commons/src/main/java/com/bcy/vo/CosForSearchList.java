package com.bcy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CosForSearchList implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String username;

    private String photo;

    private String description;

    private String label;

    private String cosPhoto;

    private Date createTime;

}
