package com.bcy.mq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AskMsg implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    private String username;

    private String question;

}
