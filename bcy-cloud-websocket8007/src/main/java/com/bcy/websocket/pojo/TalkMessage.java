package com.bcy.websocket.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class TalkMessage implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.ID_WORKER)
    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    private String message;

    private String uuId;

    private Integer fromDeleted;

    private Integer toDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}