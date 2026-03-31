package com.study.service;

import com.study.dto.AdminLoginDTO;
import com.study.dto.UserLoginDTO;

public interface LoginService {
    String userLogin(UserLoginDTO loginDTO);
    String adminLogin(AdminLoginDTO loginDTO);
}