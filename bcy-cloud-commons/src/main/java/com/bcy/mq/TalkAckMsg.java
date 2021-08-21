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
public class TalkAckMsg implements Serializable{

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String uuId;

    private Integer isSuccess;

}