package com.study.service;

import com.study.dto.UserRegisterDTO;
import com.study.entity.Notice;
import com.study.entity.Room;
import com.study.entity.Seats;
import java.util.List;

public interface PublicService {
    List<Room> listRooms();

    Room getRoomDetail(String roomId);

    List<Seats> listSeats(String roomId);

    List<Notice> listNotices();

    void register(UserRegisterDTO registerDTO);
}
