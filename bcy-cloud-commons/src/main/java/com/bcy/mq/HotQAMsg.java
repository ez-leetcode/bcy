package com.bcy.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HotQAMsg implements Serializable {

    private Long number;

    private String title;

    private String fromUsername;

}
