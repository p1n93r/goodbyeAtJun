package com.test.domain;

import java.io.Serializable;


import com.sun.istack.internal.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * user
 * @author 
 */
@Data
public class User implements Serializable {
    /**
     * 自增主键
     */
    private Integer uid;

    /**
     * 学号
     */
    @Length(min=8,max=8,message = "学号不对")
    private String num;

    /**
     * 加密后的密码
     */
    @NotEmpty(message = "密码不能为空")
    private String pwd;

    /**
     * 最新的一次wid
     */
    private String lastCollectorWid;

    /**
     * 辅导员姓名
     */
    @NotEmpty(message = "辅导员姓名必须要")
    private String teacher;

    /**
     * 位置信息
     */
    @NotEmpty(message = "需要位置信息，格式：xx省/xx市/xx县")
    private String location;

    @NotNull
    private Boolean isAtTianJin;

    @Email(message = "邮箱不对")
    private String email;

    private static final long serialVersionUID = 1L;
}