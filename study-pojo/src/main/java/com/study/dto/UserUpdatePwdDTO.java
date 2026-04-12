package com.study.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserUpdatePwdDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String oldPwd;
    private String newPwd;
}
