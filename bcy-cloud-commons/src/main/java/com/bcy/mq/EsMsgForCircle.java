package com.bcy.mq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class EsMsgForCircle implements Serializable {

    //圈子名
    private String circleName;

    //1.更新 2.删除
    private Integer type;

}
