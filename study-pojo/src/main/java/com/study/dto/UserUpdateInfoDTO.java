package com.study.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserUpdateInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
}
