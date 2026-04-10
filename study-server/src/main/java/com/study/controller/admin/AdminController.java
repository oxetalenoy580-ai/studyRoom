package com.study.controller.admin;

import com.study.Result.PageResult;
import com.study.Result.Result;
import com.study.dto.RoomAddDTO;
import com.study.dto.RoomUpdateDTO;
import com.study.dto.SeatAddDTO;
import com.study.dto.UserQueryDTO;
import com.study.entity.Room;
import com.study.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin")
@Tag(name = "管理员接口")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/user/list")
    @Operation(summary = "查询用户列表")
    public Result<PageResult> getUserList(UserQueryDTO queryDTO) {
        log.info("查询用户列表, 条件: {}", queryDTO);
        PageResult pageResult = adminService.getUserList(queryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/room/add")
    @Operation(summary = "新增自习室")
    public Result addRoom(@RequestBody RoomAddDTO roomAddDTO) {
        log.info("新增自习室：{}", roomAddDTO);
        adminService.addRoom(roomAddDTO);
        // TODO 座位关联逻辑
        return Result.success();
    }

    @PostMapping("/room/delete/{roomId}")
    @Operation(summary = "删除自习室")
    public Result deleteRoom(@RequestParam String roomId) {
        Room room = adminService.getRoomInfo(roomId);
        log.info("删除自习室：{}", room);
        adminService.deleteRoom(roomId);
        // TODO 座位相关逻辑
        return Result.success();
    }

    @GetMapping("/room/info")
    @Operation(summary = "查询自习室详情")
    public Result<Room> getRoomInfo(@RequestParam String roomId) {
        log.info("查询自习室详情: {}", roomId);
        Room room = adminService.getRoomInfo(roomId);
        return Result.success(room);
    }

    @PostMapping("/room/update")
    @Operation(summary = "修改自习室信息")
    public Result<Room> updateRoom(@RequestBody RoomUpdateDTO roomUpdateDTO) {
        log.info("修改自习室信息：{}", roomUpdateDTO);
        Room oldRoom = adminService.getRoomInfo(roomUpdateDTO.getRoomId());
        adminService.updateRoom(roomUpdateDTO);
        return Result.success(oldRoom);
    }

    @PostMapping("/seat/add")
    @Operation(summary = "为自习室添加座位")
    public Result addSeatForRoom(@RequestBody SeatAddDTO seatAddDTO) {
        String roomId = seatAddDTO.getRoomId();
        log.info("为自习室{}添加座位", roomId);
        adminService.addSeatForRoom(roomId);
        return Result.success();
    }

}
