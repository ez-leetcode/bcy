package com.bcy.vo;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HelpMsgForList implements Serializable {

    private Long number;

    private String question;

}