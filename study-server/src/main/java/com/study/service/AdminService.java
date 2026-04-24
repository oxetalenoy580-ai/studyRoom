package com.study.service;

import com.study.Result.PageResult;
import com.study.dto.NoticeAddDTO;
import com.study.dto.RoomAddDTO;
import com.study.dto.RoomUpdateDTO;
import com.study.dto.UserQueryDTO;
import com.study.entity.Room;
import com.study.vo.ReservationVO;
import java.util.List;

public interface AdminService {
    PageResult getUserList(UserQueryDTO queryDTO);

    void addRoom(RoomAddDTO roomAddDTO);

    Room getRoomInfo(String roomId);

    void deleteRoom(String roomId);

    void addSeatForRoom(String roomId);

<<<<<<< HEAD
    void deleteSeat(Integer seatId);

    void updateRoom(RoomUpdateDTO roomUpdateDTO);

    List<ReservationVO> getReservationList();

    void forceCancelReservation(Integer id);

    void addNotice(NoticeAddDTO noticeAddDTO);

    void deleteNotice(Integer id);
=======
    void updateRoom(RoomUpdateDTO roomuUpdateDTO);

    void deleteSeatForRoom(String seatId);

    void updateRoomFullStatus(String roomId);
>>>>>>> b81f9d5 (修复bug)
}
