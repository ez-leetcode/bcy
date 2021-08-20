package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class SearchWordForJson implements Serializable {

    private String keywordList;

    private Integer pages;

    private Integer counts;

}
