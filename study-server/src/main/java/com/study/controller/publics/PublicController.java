package com.study.controller.publics;

import com.study.Result.Result;
import com.study.entity.Notice;
import com.study.entity.Room;
import com.study.entity.Seats;
import com.study.service.PublicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Tag(name = "Public")
public class PublicController {

    @Autowired
    private PublicService publicService;

    @GetMapping("/room/list")
    @Operation(summary = "Room list")
    public Result<List<Room>> roomList() {
        return Result.success(publicService.listRooms());
    }

    @GetMapping("/room/detail/{roomId}")
    @Operation(summary = "Room detail")
    public Result<Room> roomDetail(@PathVariable String roomId) {
        return Result.success(publicService.getRoomDetail(roomId));
    }

    @GetMapping("/seat/list/{roomId}")
    @Operation(summary = "Seat list")
    public Result<List<Seats>> seatList(@PathVariable String roomId) {
        return Result.success(publicService.listSeats(roomId));
    }

    @GetMapping("/notice/list")
    @Operation(summary = "Notice list")
    public Result<List<Notice>> noticeList() {
        return Result.success(publicService.listNotices());
    }
}
