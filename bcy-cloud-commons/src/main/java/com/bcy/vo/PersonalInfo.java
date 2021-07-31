package com.bcy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInfo implements Serializable {

    private Long id;

    private String username;

    private String sex;

    private String photo;

    private String description;

    private String province;

    private String city;

    private Date birthday;
}