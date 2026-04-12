package com.study.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserRegisterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String name;
    private String phone;
}
