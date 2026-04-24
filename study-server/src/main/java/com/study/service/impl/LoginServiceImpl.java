package com.study.service.impl;

import com.study.constant.StatuConstant;
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
import org.springframework.util.StringUtils;

@Service
public class LoginServiceImpl implements LoginService {

  @Autowired
  private LoginMapper loginMapper;

  @Override
  public String userLogin(UserLoginDTO loginDTO) {
    validateLoginDTO(loginDTO.getUsername(), loginDTO.getPassword());
    User user = loginMapper.findByUsername(loginDTO.getUsername());
    if (user == null) {
      throw new RuntimeException("User not found");
    }
    if (!passwordMatches(loginDTO.getPassword(), user.getPassword())) {
      throw new RuntimeException("Password incorrect");
    }
    return JwtUtil.createToken(user.getUsername(), user.getUsername(), StatuConstant.ROLE_USER);
  }

  @Override
  public String adminLogin(AdminLoginDTO loginDTO) {
    validateLoginDTO(loginDTO.getUsername(), loginDTO.getPassword());
    Admin admin = loginMapper.findByAdminUsername(loginDTO.getUsername());
    if (admin == null) {
      throw new RuntimeException("Admin not found");
    }
    if (!passwordMatches(loginDTO.getPassword(), admin.getPassword())) {
      throw new RuntimeException("Password incorrect");
    }
    return JwtUtil.createToken(admin.getUsername(), admin.getUsername(), StatuConstant.ROLE_ADMIN);
  }

  private void validateLoginDTO(String username, String password) {
    if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
      throw new RuntimeException("Username and password are required");
    }
  }

  private boolean passwordMatches(String rawPassword, String dbPassword) {
    return MD5Util.encrypt(rawPassword).equals(dbPassword);
  }
}
