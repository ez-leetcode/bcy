package com.bcy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class CosForFollow implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String username;

    private String photo;

    private String description;

    private List<String> label;

    private List<String> cosPhoto;

    private Date createTime;

}
