package com.bcy.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FansMsg implements Serializable {

    private Long fromId;

    private String fromUsername;

    private Long toId;

    private Date createTime;

}
