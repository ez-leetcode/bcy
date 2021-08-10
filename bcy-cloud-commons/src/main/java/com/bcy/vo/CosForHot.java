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
public class CosForHot implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cosNumber;

    private Long id;

    private String username;

    private String photo;

    private List<String> cosPhoto;

    private List<String> cosLabel;

    private Date createTime;

}
