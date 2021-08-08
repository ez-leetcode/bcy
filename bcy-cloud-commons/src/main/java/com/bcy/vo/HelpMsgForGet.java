package com.bcy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HelpMsgForGet implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long number;

    private String question;

    private String answer;
}
