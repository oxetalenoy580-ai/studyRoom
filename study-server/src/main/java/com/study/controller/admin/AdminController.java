package com.study.controller.admin;

import com.study.Result.PageResult;
import com.study.Result.Result;
import com.study.service.AdminService;
import com.study.vo.UserQueryVO;
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
  public Result getUserList(@ModelAttribute UserQueryVO queryVO) {
    log.info("查询用户列表, 条件: {}", queryVO);
    PageResult pageResult = adminService.getUserList(queryVO);
    return Result.success(pageResult);
  }
}
