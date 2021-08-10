package com.bcy.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class HotCosMsg implements Serializable {

    private Long number;

    private String description;

    private String fromUsername;

}
