package com.bcy.vo;

import lombok.*;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HelpMsgForGet{

    private Long number;

    private String question;

    private String answer;
}
