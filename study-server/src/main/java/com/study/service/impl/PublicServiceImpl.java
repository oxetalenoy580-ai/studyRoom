package com.study.service.impl;

import com.study.entity.Notice;
import com.study.entity.Room;
import com.study.entity.Seats;
import com.study.mapper.PublicMapper;
import com.study.service.PublicService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
