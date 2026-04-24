package com.study.controller.admin;

import com.study.Result.PageResult;
import com.study.Result.Result;
import com.study.constant.StatuConstant;
import com.study.controller.BaseController;
import com.study.dto.NoticeAddDTO;
import com.study.dto.RoomAddDTO;
import com.study.dto.RoomUpdateDTO;
import com.study.dto.SeatAddDTO;
import com.study.dto.UserQueryDTO;
import com.study.entity.Room;
import com.study.service.AdminService;
import com.study.vo.ReservationVO;
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
@RequestMapping("/admin")
@Tag(name = "Admin")
public class AdminController extends BaseController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/user/list")
    @Operation(summary = "List users")
    public Result<PageResult> getUserList(@RequestHeader(value = "token", required = false) String token,
                                          UserQueryDTO queryDTO) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        return Result.success(adminService.getUserList(queryDTO));
    }

    @PostMapping("/room/add")
    @Operation(summary = "Add room")
    public Result<Void> addRoom(@RequestHeader(value = "token", required = false) String token,
                                @RequestBody RoomAddDTO roomAddDTO) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        adminService.addRoom(roomAddDTO);
        return Result.success();
    }

    @PostMapping("/room/delete/{roomId}")
    @Operation(summary = "Delete room")
    public Result<Void> deleteRoom(@RequestHeader(value = "token", required = false) String token,
                                   @PathVariable String roomId) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        adminService.deleteRoom(roomId);
        return Result.success();
    }

    @GetMapping("/room/info/{roomId}")
    @Operation(summary = "Room detail")
    public Result<Room> getRoomInfo(@RequestHeader(value = "token", required = false) String token,
                                    @PathVariable String roomId) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        return Result.success(adminService.getRoomInfo(roomId));
    }

    @PostMapping("/room/update")
<<<<<<< HEAD
    @Operation(summary = "Update room")
    public Result<Void> updateRoom(@RequestHeader(value = "token", required = false) String token,
                                   @RequestBody RoomUpdateDTO roomUpdateDTO) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        adminService.updateRoom(roomUpdateDTO);
        return Result.success();
    }

    @PostMapping("/seat/add")
    @Operation(summary = "Add seat")
    public Result<Void> addSeat(@RequestHeader(value = "token", required = false) String token,
                                @RequestBody SeatAddDTO seatAddDTO) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        adminService.addSeatForRoom(seatAddDTO.getRoomId());
        return Result.success();
    }

    @PostMapping("/seat/delete/{seatId}")
    @Operation(summary = "Delete seat")
    public Result<Void> deleteSeat(@RequestHeader(value = "token", required = false) String token,
                                   @PathVariable Integer seatId) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        adminService.deleteSeat(seatId);
        return Result.success();
    }

    @GetMapping("/reservation/list")
    @Operation(summary = "List reservations")
    public Result<List<ReservationVO>> reservationList(@RequestHeader(value = "token", required = false) String token) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        return Result.success(adminService.getReservationList());
    }

    @PostMapping("/reservation/forceCancel/{id}")
    @Operation(summary = "Force cancel reservation")
    public Result<Void> forceCancelReservation(@RequestHeader(value = "token", required = false) String token,
                                               @PathVariable Integer id) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        adminService.forceCancelReservation(id);
        return Result.success();
    }

    @PostMapping("/notice/add")
    @Operation(summary = "Add notice")
    public Result<Void> addNotice(@RequestHeader(value = "token", required = false) String token,
                                  @RequestBody NoticeAddDTO noticeAddDTO) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        adminService.addNotice(noticeAddDTO);
        return Result.success();
    }

    @PostMapping("/notice/delete/{id}")
    @Operation(summary = "Delete notice")
    public Result<Void> deleteNotice(@RequestHeader(value = "token", required = false) String token,
                                     @PathVariable Integer id) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        adminService.deleteNotice(id);
        return Result.success();
    }
=======
    @Operation(summary = "修改自习室信息")
    public Result<Room> updateRoom(@RequestBody RoomUpdateDTO roomUpdateDTO) {
        log.info("修改自习室信息：{}", roomUpdateDTO);
        adminService.updateRoom(roomUpdateDTO);
        return Result.success();
    }

    @PostMapping("/seat/add")
    @Operation(summary = "为自习室添加座位")
    public Result addSeatForRoom(@RequestBody SeatAddDTO seatAddDTO) {
        String roomId = seatAddDTO.getRoomId();
        log.info("为自习室{}添加座位", roomId);
        adminService.addSeatForRoom(roomId);
        return Result.success();
    }

    @PostMapping("/seat/delete/{seatId}")
    @Operation(summary = "为自习室删除座位")
    public Result deleteSeatForRoom(@RequestParam String seatId) {
        log.info("删除座位{}", seatId);
        adminService.deleteSeatForRoom(seatId);
        return Result.success();
    }

>>>>>>> b81f9d5 (修复bug)
}
