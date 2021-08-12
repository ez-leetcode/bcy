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
public class LikeMsg implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    //1是cos点赞 2是问答点赞 3是评论点赞
    private Integer type;

    private String username;

    private Long toId;

}
