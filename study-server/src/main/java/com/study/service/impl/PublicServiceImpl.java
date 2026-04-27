package com.study.service.impl;

import com.study.dto.UserRegisterDTO;
import com.study.entity.Notice;
import com.study.entity.Room;
import com.study.entity.Seats;
import com.study.entity.User;
import com.study.mapper.PublicMapper;
import com.study.service.PublicService;
import com.study.utils.MD5Util;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class PublicServiceImpl implements PublicService {

    @Autowired
    private PublicMapper publicMapper;

    @Override
    public List<Room> listRooms() {
        return publicMapper.listRooms();
    }

    @Override
    public Room getRoomDetail(String roomId) {
        Room room = publicMapper.getRoomDetail(roomId);
        if (room == null) {
            throw new RuntimeException("Room not found");
        }
        return room;
    }

    @Override
    public List<Seats> listSeats(String roomId) {
        getRoomDetail(roomId);
        return publicMapper.listSeatsByRoomId(roomId);
    }

    @Override
    public List<Notice> listNotices() {
        return publicMapper.listNotices();
    }

    @Override
    @Transactional
    public void register(UserRegisterDTO registerDTO) {
        if (!StringUtils.hasText(registerDTO.getUsername())
                || !StringUtils.hasText(registerDTO.getPassword())
                || !StringUtils.hasText(registerDTO.getName())) {
            throw new RuntimeException("Registration information is incomplete");
        }
        if (publicMapper.countUserByUsername(registerDTO.getUsername()) > 0) {
            throw new RuntimeException("Username already exists");
        }
        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(MD5Util.encrypt(registerDTO.getPassword()))
                .name(registerDTO.getName())
                .phone(registerDTO.getPhone())
                .build();
        publicMapper.addUser(user);
    }
}
