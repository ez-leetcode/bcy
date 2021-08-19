package com.bcy.elasticsearch.dto;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.AssertTrue;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Api("test")
public class Ck {

    private Integer id;

    private String username;

    private String password;

}
