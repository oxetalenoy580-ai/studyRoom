package com.study.service.impl;

import com.study.dto.AdminLoginDTO;
import com.study.dto.UserLoginDTO;
import com.study.entity.Admin;
import com.study.entity.User;
import com.study.mapper.LoginMapper;
import com.study.service.LoginService;
import com.study.utils.JwtUtil;
import com.study.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

  @Autowired LoginMapper loginMapper;

  @Override
  public String userLogin(UserLoginDTO loginDTO) {
    User user = loginMapper.findByUsername(loginDTO.getUsername());
    if (user == null) {
      throw new RuntimeException("用户不存在");
    }
    String encryptedPassword = MD5Util.encrypt(loginDTO.getPassword());
    if (!encryptedPassword.equals(user.getPassword())) {
      throw new RuntimeException("密码错误");
    }
    return JwtUtil.createToken(user.getUsername(), user.getUsername());
  }

  @Override
  public String adminLogin(AdminLoginDTO loginDTO) {
    Admin admin = loginMapper.findByAdminname(loginDTO.getUsername());
    if (admin == null) {
      throw new RuntimeException("管理员不存在");
    }
    String encryptedPassword = MD5Util.encrypt(loginDTO.getPassword());
    if (!encryptedPassword.equals(admin.getPassword())) {
      throw new RuntimeException("密码错误");
    }
    return JwtUtil.createToken(admin.getUsername(), admin.getUsername());
  }
}

