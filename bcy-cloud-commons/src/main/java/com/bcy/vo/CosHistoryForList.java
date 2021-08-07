package com.bcy.vo;

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
public class CosHistoryForList implements Serializable {

    private Long number;

    private Long id;

    private String username;

    private String photo;

    private String description;

    private List<String> cosPhoto;

    private Date createTime;
}
