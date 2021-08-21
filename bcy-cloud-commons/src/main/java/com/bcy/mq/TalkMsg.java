package com.bcy.mq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TalkMsg implements Serializable{

    //@JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    //@JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    private String msg;

    private String uuId;

}
