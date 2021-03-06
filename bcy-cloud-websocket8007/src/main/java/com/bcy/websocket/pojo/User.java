package com.bcy.websocket.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class User {

    @TableId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String username;

    private String sex;

    private String photo;

    private String description;

    private String province;

    private String city;

    private Date birthday;

    private Integer momentCounts;

    private Integer fansCounts;

    private Integer followCounts;

    private Integer deleted;
}