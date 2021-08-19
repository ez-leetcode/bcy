package com.bcy.mq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EsMsg implements Serializable {

    //cos/qa编号
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    // 1.cos更新 2.cos删除 3.qa更新 4.qa删除
    private Integer type;

}
