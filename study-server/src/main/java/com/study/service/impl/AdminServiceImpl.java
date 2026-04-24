package com.study.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.Result.PageResult;
import com.study.constant.StatuConstant;
import com.study.dto.NoticeAddDTO;
import com.study.dto.RoomAddDTO;
import com.study.dto.RoomUpdateDTO;
import com.study.dto.UserQueryDTO;
import com.study.entity.Notice;
import com.study.entity.Reservation;
import com.study.entity.Room;
import com.study.entity.Seats;
import com.study.mapper.AdminMapper;
import com.study.service.AdminService;
import com.study.vo.ReservationVO;
import com.study.vo.UserVO;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public PageResult getUserList(UserQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<UserVO> userList = adminMapper.queryUserList(queryDTO);
        PageInfo<UserVO> pageInfo = new PageInfo<>(userList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    @Transactional
    public void addRoom(RoomAddDTO roomAddDTO) {
        validateRoomRequest(roomAddDTO.getRoomId(), roomAddDTO.getRoomName(), roomAddDTO.getLocation(),
                roomAddDTO.getTotalSeats(), roomAddDTO.getOpenTime(), roomAddDTO.getCloseTime());
        if (adminMapper.countRoomByRoomId(roomAddDTO.getRoomId()) > 0) {
            throw new RuntimeException("Room id already exists");
        }

        Room room = new Room();
        BeanUtils.copyProperties(roomAddDTO, room);
        room.setStatus(StatuConstant.ROOM_ENABLED);
        room.setFullStatus(StatuConstant.ROOM_NOT_FULL);
        adminMapper.addRoom(room);

        int totalSeats = roomAddDTO.getTotalSeats();
        for (int i = 1; i <= totalSeats; i++) {
            String seatId = roomAddDTO.getRoomId() + "_" + i;
            Seats seat = Seats.builder()
                    .seatId(seatId)
                    .roomId(roomAddDTO.getRoomId())
                    .seatNumber(i)
                    .status(StatuConstant.SEAT_AVAILABLE)
                    .build();
            adminMapper.addSeatForRoom(seat);
        }
        refreshRoomSeatSummary(roomAddDTO.getRoomId());
    }

    @Override
    @Transactional
    public void deleteSeatForRoom(String seatId) {
        String[] parts = seatId.split("_");
        if (parts.length != 2) {
            throw new RuntimeException("Invalid seat id format");
        }
        String roomId = parts[0];
        int deleteSeatNumber = Integer.parseInt(parts[1]);

        Room room = getRoomInfo(roomId);
        int roomTotalSeat = room.getTotalSeats();

        List<Seats> seatsToUpdate = adminMapper.findSeatsAfter(roomId, deleteSeatNumber);
        for (Seats seat : seatsToUpdate) {
            int newSeatNumber = seat.getSeatNumber() - 1;
            String newSeatId = roomId + "_" + newSeatNumber;
            adminMapper.updateSeat(seat.getSeatId(), newSeatId, newSeatNumber);
        }

        adminMapper.deleteSeatForRoom(seatId);

        room.setTotalSeats(roomTotalSeat - 1);
        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO();
        roomUpdateDTO.setRoomId(roomId);
        roomUpdateDTO.setTotalSeats(roomTotalSeat - 1);
        adminMapper.updateRoom(roomUpdateDTO);
        updateRoomFullStatus(roomId);
    }

    @Override
    public Room getRoomInfo(String roomId) {
        Room room = adminMapper.getRoomInfo(roomId);
        if (room == null) {
            throw new RuntimeException("Room not found");
        }
        return room;
    }

    @Override
    @Transactional
    public void deleteRoom(String roomId) {
        Room room = getRoomInfo(roomId);
        int activeReservations = adminMapper.countActiveReservationsByRoomId(roomId);
        if (activeReservations > 0) {
            throw new RuntimeException("Cannot delete room with active reservations");
        }
        adminMapper.deleteReservationsByRoomId(roomId);
        adminMapper.deleteSeatsByRoomId(roomId);
        adminMapper.deleteRoom(roomId);
    }

    @Override
    @Transactional
    public void addSeatForRoom(String roomId) {
        Room room = getRoomInfo(roomId);
        int currentCount = adminMapper.countSeatsByRoomId(roomId);
        int newSeatNumber = currentCount + 1;
        String seatId = roomId + "_" + newSeatNumber;

        adminMapper.addSeatForRoom(Seats.builder()
                .seatId(seatId)
                .roomId(roomId)
                .seatNumber(newSeatNumber)
                .status(StatuConstant.SEAT_AVAILABLE)
                .build());
        refreshRoomSeatSummary(roomId);
    }

    @Override
    @Transactional
    public void deleteSeat(Integer seatId) {
        Seats seat = adminMapper.getSeatById(seatId);
        if (seat == null) {
            throw new RuntimeException("Seat not found");
        }
        adminMapper.deleteReservationsBySeatId(seatId);
        adminMapper.deleteSeat(seatId);
        refreshRoomSeatSummary(seat.getRoomId());
    }

    @Override
    @Transactional
    public void updateRoom(RoomUpdateDTO roomUpdateDTO) {
        Room currentRoom = getRoomInfo(roomUpdateDTO.getRoomId());
        validateRoomRequest(roomUpdateDTO.getRoomId(), roomUpdateDTO.getRoomName(), roomUpdateDTO.getLocation(),
                Math.max(adminMapper.countSeatsByRoomId(roomUpdateDTO.getRoomId()), 0),
                roomUpdateDTO.getOpenTime(), roomUpdateDTO.getCloseTime());
        if (roomUpdateDTO.getStatus() == null) {
            roomUpdateDTO.setStatus(currentRoom.getStatus());
        }
        roomUpdateDTO.setTotalSeats(adminMapper.countSeatsByRoomId(roomUpdateDTO.getRoomId()));
        adminMapper.updateRoom(roomUpdateDTO);
        refreshRoomFullStatus(roomUpdateDTO.getRoomId());
    }

    @Override
    public List<ReservationVO> getReservationList() {
        return adminMapper.listReservationVOs();
    }

    @Override
    @Transactional
    public void forceCancelReservation(Integer id) {
        Reservation reservation = adminMapper.getReservationById(id);
        if (reservation == null) {
            throw new RuntimeException("Reservation not found");
        }
        if (StatuConstant.RESERVATION_CANCELED.equals(reservation.getStatus())) {
            return;
        }
        adminMapper.updateReservationStatus(id, StatuConstant.RESERVATION_CANCELED);
        refreshSeatStatus(reservation.getSeatId());
        refreshRoomFullStatus(reservation.getRoomId());
    }

    @Override
    @Transactional
    public void addNotice(NoticeAddDTO noticeAddDTO) {
        if (!StringUtils.hasText(noticeAddDTO.getTitle()) || !StringUtils.hasText(noticeAddDTO.getContent())) {
            throw new RuntimeException("Notice title and content are required");
        }
        adminMapper.addNotice(Notice.builder()
                .title(noticeAddDTO.getTitle())
                .content(noticeAddDTO.getContent())
                .build());
    }

    @Override
    @Transactional
    public void deleteNotice(Integer id) {
        if (adminMapper.countNoticeById(id) == 0) {
            throw new RuntimeException("Notice not found");
        }
        adminMapper.deleteNotice(id);
    }

    private void validateRoomRequest(String roomId, String roomName, String location, Integer totalSeats,
            LocalTime openTime, LocalTime closeTime) {
        if (!StringUtils.hasText(roomId) || !StringUtils.hasText(roomName) || !StringUtils.hasText(location)) {
            throw new RuntimeException("Room information is incomplete");
        }
        if (totalSeats == null || totalSeats < 0) {
            throw new RuntimeException("Total seats must be greater than or equal to 0");
        }
        if (openTime == null || closeTime == null || !openTime.isBefore(closeTime)) {
            throw new RuntimeException("Room open time is invalid");
        }
    }

    private void refreshSeatStatus(Integer seatId) {
        int pendingCount = adminMapper.countReservationsBySeatAndStatus(seatId, StatuConstant.RESERVATION_PENDING);
        adminMapper.updateSeatStatus(seatId,
                pendingCount > 0 ? StatuConstant.SEAT_RESERVED : StatuConstant.SEAT_AVAILABLE);
    }

    private void refreshRoomSeatSummary(String roomId) {
        int totalSeats = adminMapper.countSeatsByRoomId(roomId);
        adminMapper.updateRoomTotalSeats(roomId, totalSeats);
        refreshRoomFullStatus(roomId);
    }

    private void refreshRoomFullStatus(String roomId) {
        int totalSeats = adminMapper.countSeatsByRoomId(roomId);
        int availableSeats = adminMapper.countSeatsByRoomIdAndStatus(roomId, StatuConstant.SEAT_AVAILABLE);
        int fullStatus = totalSeats > 0 && availableSeats == 0 ? StatuConstant.ROOM_FULL : StatuConstant.ROOM_NOT_FULL;
        adminMapper.updateRoomFullStatus(roomId, fullStatus);
    }

    @Override
    public void updateRoomFullStatus(String roomId) {
        int available = adminMapper.countSeatsByRoomIdAndStatus(roomId, StatuConstant.SEAT_AVAILABLE);
        int total = adminMapper.getTotalSeats(roomId);
        int fullStatus = (available == 0 && total > 0) ? StatuConstant.ROOM_FULL : StatuConstant.ROOM_NOT_FULL;
        adminMapper.updateRoomFullStatus(roomId, fullStatus);
    }

}
