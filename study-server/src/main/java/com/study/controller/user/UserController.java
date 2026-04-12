package com.study.controller.user;

import com.study.Result.Result;
import com.study.constant.StatuConstant;
import com.study.controller.BaseController;
import com.study.dto.ReservationAddDTO;
import com.study.dto.UserRegisterDTO;
import com.study.dto.UserUpdateInfoDTO;
import com.study.dto.UserUpdatePwdDTO;
import com.study.service.UserService;
import com.study.vo.ReservationVO;
import com.study.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping
@Tag(name = "User")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    @Operation(summary = "Register user")
    public Result<Void> register(@RequestBody UserRegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success();
    }

    @GetMapping("/user/info")
    @Operation(summary = "Get user info")
    public Result<UserInfoVO> info(@RequestHeader(value = "token", required = false) String token) {
        return Result.success(userService.getCurrentUserInfo(requireRole(token, StatuConstant.ROLE_USER)));
    }

    @PostMapping("/user/update/info")
    @Operation(summary = "Update user info")
    public Result<Void> updateInfo(@RequestHeader(value = "token", required = false) String token,
                                   @RequestBody UserUpdateInfoDTO updateInfoDTO) {
        userService.updateUserInfo(requireRole(token, StatuConstant.ROLE_USER), updateInfoDTO);
        return Result.success();
    }

    @PostMapping("/user/update/pwd")
    @Operation(summary = "Update password")
    public Result<Void> updatePwd(@RequestHeader(value = "token", required = false) String token,
                                  @RequestBody UserUpdatePwdDTO updatePwdDTO) {
        userService.updatePassword(requireRole(token, StatuConstant.ROLE_USER), updatePwdDTO);
        return Result.success();
    }

    @PostMapping("/reservation/add")
    @Operation(summary = "Add reservation")
    public Result<Void> addReservation(@RequestHeader(value = "token", required = false) String token,
                                       @RequestBody ReservationAddDTO reservationAddDTO) {
        userService.addReservation(requireRole(token, StatuConstant.ROLE_USER), reservationAddDTO);
        return Result.success();
    }

    @GetMapping("/reservation/my")
    @Operation(summary = "My reservations")
    public Result<List<ReservationVO>> myReservations(@RequestHeader(value = "token", required = false) String token) {
        return Result.success(userService.getMyReservations(requireRole(token, StatuConstant.ROLE_USER)));
    }

    @PostMapping("/reservation/cancel/{id}")
    @Operation(summary = "Cancel reservation")
    public Result<Void> cancelReservation(@RequestHeader(value = "token", required = false) String token,
                                          @PathVariable Integer id) {
        userService.cancelReservation(requireRole(token, StatuConstant.ROLE_USER), id);
        return Result.success();
    }
}
