package com.study.controller.login;

import com.study.Result.Result;
import com.study.controller.BaseController;
import com.study.dto.AdminLoginDTO;
import com.study.dto.UserLoginDTO;
import com.study.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping
@Tag(name = "Login")
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login/user")
    @Operation(summary = "User login")
    public Result<String> userLogin(@RequestBody UserLoginDTO loginDTO) {
        log.info("User login: {}", loginDTO.getUsername());
        return Result.success(loginService.userLogin(loginDTO));
    }

    @PostMapping("/login/admin")
    @Operation(summary = "Admin login")
    public Result<String> adminLogin(@RequestBody AdminLoginDTO loginDTO) {
        log.info("Admin login: {}", loginDTO.getUsername());
        return Result.success(loginService.adminLogin(loginDTO));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public Result<Void> logout(@RequestHeader(value = "token", required = false) String token) {
        requireToken(token);
        return Result.success();
    }
}
