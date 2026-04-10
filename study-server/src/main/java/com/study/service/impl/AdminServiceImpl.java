package com.study.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.Result.PageResult;
import com.study.constant.StatuConstant;
import com.study.dto.RoomAddDTO;
import com.study.dto.RoomUpdateDTO;
import com.study.dto.UserQueryDTO;
import com.study.entity.Room;
import com.study.entity.Seats;
import com.study.mapper.AdminMapper;
import com.study.service.AdminService;
import com.study.vo.UserVO;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public PageResult getUserList(@RequestBody UserQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<UserVO> userList = adminMapper.queryUserList(queryDTO);
        PageInfo<UserVO> pageInfo = new PageInfo<>(userList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void addRoom(RoomAddDTO roomAddDTO) {
        Room newRoom = new Room();
        BeanUtils.copyProperties(roomAddDTO, newRoom);
        newRoom.setStatus(StatuConstant.RoomEnabled);
        newRoom.setFullStatus(0);
        // TODO addSeats()
        adminMapper.addRoom(newRoom);
    }

    @Override
    public void deleteRoom(String roomId) {
        adminMapper.deleteRoom(roomId);
        // TODO deleteSeats()
    }

    @Override
    public void addSeatForRoom(String roomId) {
        Room room = getRoomInfo(roomId);

        Integer roomTotalSeat = room.getTotalSeats();
        Integer seatNumber = roomTotalSeat + 1;
        String seatId = roomId + "_" + seatNumber.toString();
        Integer statu = StatuConstant.SeatIsNotRevered;

        Seats seat = new Seats(seatId, roomId, seatNumber, statu);

        adminMapper.addSeatForRoom(seat);
        room.setTotalSeats(seatNumber);

        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO();
        BeanUtils.copyProperties(room, roomUpdateDTO);

        adminMapper.updateRoom(roomUpdateDTO);
    }

    @Override
    public Room getRoomInfo(String roomId) {
        Room room = adminMapper.getRoomInfo(roomId);
        return room;
    }

    @Override
    public void updateRoom(RoomUpdateDTO roomUpdateDTO) {
        adminMapper.updateRoom(roomUpdateDTO);
    }
}
