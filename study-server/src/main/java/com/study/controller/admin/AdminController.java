package com.study.controller.admin;

import com.study.Result.PageResult;
import com.study.Result.Result;
import com.study.dto.UserQueryDTO;
import com.study.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin")
@Tag(name = "管理员接口")
public class AdminController {
  @Autowired private AdminService adminService;

  @GetMapping("/user/list")
  @Operation(summary = "查询用户列表")
  public Result getUserList(@ModelAttribute UserQueryDTO queryDTO) {
    log.info("查询用户列表, 条件: {}", queryDTO);
    PageResult pageResult = adminService.getUserList(queryDTO);
    return Result.success(pageResult);
  }
}
