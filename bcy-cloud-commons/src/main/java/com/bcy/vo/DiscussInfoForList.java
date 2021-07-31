package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
//在收藏和历史里展示
public class DiscussInfoForList implements Serializable {

    private Long number;

    private Long id;

    private String username;

    private String photo;

    private String description;

    private List<String> discussPhoto;

    private Integer likeCounts;

    private Integer favorCounts;

    private Integer commentCounts;

    private Integer shareCounts;

    private Date createTime;

}
