package com.study.controller.login;

import com.study.Result.Result;
import com.study.dto.AdminLoginDTO;
import com.study.dto.UserLoginDTO;
import com.study.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/login")
@Tag(name = "登录模块")
public class LoginController {

  @Autowired LoginService loginService;

  @PostMapping("/user")
  @Operation(summary = "学生登录")
  public Result<String> userLogin(@RequestBody UserLoginDTO loginDTO) {
    log.info("学生登录: {}", loginDTO.getUsername());
    String token = loginService.userLogin(loginDTO);
    return Result.success(token);
  }

  @PostMapping("/admin")
  @Operation(summary = "管理员登录")
  public Result<String> adminLogin(@RequestBody AdminLoginDTO loginDTO) {
    log.info("管理员登录: {}", loginDTO.getUsername());
    String token = loginService.adminLogin(loginDTO);
    return Result.success(token);
  }
}

