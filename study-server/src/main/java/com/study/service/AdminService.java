package com.study.service;

import com.study.Result.PageResult;
import com.study.dto.RoomAddDTO;
import com.study.dto.RoomUpdateDTO;
import com.study.dto.UserQueryDTO;
import com.study.entity.Room;

public interface AdminService {
    PageResult getUserList(UserQueryDTO queryDTO);

    void addRoom(RoomAddDTO roomAddDTO);

    Room getRoomInfo(String roomId);

    void deleteRoom(String roomId);

    void addSeatForRoom(String roomId);

    void updateRoom(RoomUpdateDTO roomuUpdateDTO);
}
