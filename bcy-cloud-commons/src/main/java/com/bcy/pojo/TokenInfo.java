package com.bcy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo implements Serializable {

    private boolean active;

    private Long exp;

    private Long user_name;

    private List<String> authorities;

    private String client_id;

    private List<String> scope;
}
