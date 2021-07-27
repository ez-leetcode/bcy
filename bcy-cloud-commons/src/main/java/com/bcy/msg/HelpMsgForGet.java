package com.bcy.msg;

import lombok.*;

import java.io.Serializable;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HelpMsgForGet{

    private Long number;

    private String question;

    private String answer;
}
