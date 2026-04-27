package com.study.service.impl;

import com.study.annotation.CheckRoomFull;
import com.study.constant.StatuConstant;
import com.study.dto.ReservationAddDTO;
import com.study.dto.UserRegisterDTO;
import com.study.dto.UserUpdateInfoDTO;
import com.study.dto.UserUpdatePwdDTO;
import com.study.entity.Reservation;
import com.study.entity.Room;
import com.study.entity.Seats;
import com.study.entity.User;
import com.study.mapper.UserMapper;
import com.study.service.UserService;
import com.study.utils.MD5Util;
import com.study.vo.ReservationVO;
import com.study.vo.UserInfoVO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserInfoVO getCurrentUserInfo(String username) {
        User user = getUser(username);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        return userInfoVO;
    }

    @Override
    @Transactional
    public void updateUserInfo(String username, UserUpdateInfoDTO updateInfoDTO) {
        getUser(username);
        if (!StringUtils.hasText(updateInfoDTO.getName())) {
            throw new RuntimeException("Name is required");
        }
        userMapper.updateUserInfo(username, updateInfoDTO.getName(), updateInfoDTO.getPhone());
    }

    @Override
    @Transactional
    public void updatePassword(String username, UserUpdatePwdDTO updatePwdDTO) {
        User user = getUser(username);
        if (!StringUtils.hasText(updatePwdDTO.getOldPwd()) || !StringUtils.hasText(updatePwdDTO.getNewPwd())) {
            throw new RuntimeException("Password is required");
        }
        if (!passwordMatches(updatePwdDTO.getOldPwd(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        userMapper.updateUserPassword(username, MD5Util.encrypt(updatePwdDTO.getNewPwd()));
    }

    @Override
    @Transactional
    @CheckRoomFull(roomIdParam = "reservationAddDTO")
    public void addReservation(String username, ReservationAddDTO reservationAddDTO) {
        Room room = userMapper.getRoomById(reservationAddDTO.getRoomId());
        if (room == null || !StatuConstant.ROOM_ENABLED.equals(room.getStatus())) {
            throw new RuntimeException("Room is not available");
        }
        Seats seat = userMapper.getSeatById(reservationAddDTO.getSeatId());
        if (seat == null || !reservationAddDTO.getRoomId().equals(seat.getRoomId())) {
            throw new RuntimeException("Seat not found");
        }
        if (!StatuConstant.SEAT_AVAILABLE.equals(seat.getStatus())) {
            throw new RuntimeException("Seat is not available");
        }
        if (reservationAddDTO.getReserveDate() == null || reservationAddDTO.getStartTime() == null
                || reservationAddDTO.getEndTime() == null) {
            throw new RuntimeException("Reservation time is required");
        }
        if (reservationAddDTO.getReserveDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new RuntimeException("Cannot reserve for today or past dates");
        }
        if (!reservationAddDTO.getStartTime().isBefore(reservationAddDTO.getEndTime())) {
            throw new RuntimeException("Reservation time range is invalid");
        }
        if (reservationAddDTO.getStartTime().isBefore(room.getOpenTime())
                || reservationAddDTO.getEndTime().isAfter(room.getCloseTime())) {
            throw new RuntimeException("Reservation time is outside room hours");
        }
        if (userMapper.countSeatConflict(reservationAddDTO.getSeatId(), reservationAddDTO.getReserveDate(),
                reservationAddDTO.getStartTime(), reservationAddDTO.getEndTime()) > 0) {
            throw new RuntimeException("Seat is already reserved for the selected time");
        }
        if (userMapper.countUserConflict(username, reservationAddDTO.getReserveDate(),
                reservationAddDTO.getStartTime(), reservationAddDTO.getEndTime()) > 0) {
            throw new RuntimeException("You already have another reservation in this time range");
        }

        Reservation reservation = Reservation.builder()
                .userUsername(username)
                .roomId(reservationAddDTO.getRoomId())
                .seatId(reservationAddDTO.getSeatId())
                .reserveDate(reservationAddDTO.getReserveDate())
                .startTime(reservationAddDTO.getStartTime())
                .endTime(reservationAddDTO.getEndTime())
                .status(StatuConstant.RESERVATION_PENDING)
                .build();
        userMapper.addReservation(reservation);
        userMapper.updateSeatStatus(seat.getSeatId(), StatuConstant.SEAT_RESERVED);
        refreshRoomFullStatus(reservationAddDTO.getRoomId());
    }

    @Override
    public List<ReservationVO> getMyReservations(String username) {
        getUser(username);
        return userMapper.listReservationByUsername(username);
    }

    @Override
    @Transactional
    public void cancelReservation(String username, Integer reservationId) {
        Reservation reservation = userMapper.getReservationById(reservationId);
        if (reservation == null) {
            throw new RuntimeException("Reservation not found");
        }
        if (!username.equals(reservation.getUserUsername())) {
            throw new RuntimeException("Cannot cancel another user's reservation");
        }
        if (!StatuConstant.RESERVATION_PENDING.equals(reservation.getStatus())) {
            throw new RuntimeException("Reservation cannot be canceled in current status");
        }
        userMapper.updateReservationStatus(reservationId, StatuConstant.RESERVATION_CANCELED);
        refreshSeatStatus(String.valueOf(reservation.getSeatId()));
        refreshRoomFullStatus(reservation.getRoomId());
    }

    private User getUser(String username) {
        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    private boolean passwordMatches(String rawPassword, String dbPassword) {
        return MD5Util.encrypt(rawPassword).equals(dbPassword);
    }

    private void refreshSeatStatus(String seatId) {
        int pendingCount = userMapper.countReservationsBySeatAndStatus(seatId, StatuConstant.RESERVATION_PENDING);
        userMapper.updateSeatStatus(seatId,
                pendingCount > 0 ? StatuConstant.SEAT_RESERVED : StatuConstant.SEAT_AVAILABLE);
    }

    private void refreshRoomFullStatus(String roomId) {
        int totalSeats = userMapper.countSeatsByRoomId(roomId);
        int availableSeats = userMapper.countSeatsByRoomIdAndStatus(roomId, StatuConstant.SEAT_AVAILABLE);
        int fullStatus = totalSeats > 0 && availableSeats == 0 ? StatuConstant.ROOM_FULL : StatuConstant.ROOM_NOT_FULL;
        userMapper.updateRoomFullStatus(roomId, fullStatus);
    }
}
