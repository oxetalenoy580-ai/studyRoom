package com.study.service;

import com.study.dto.ReservationAddDTO;
import com.study.dto.UserRegisterDTO;
import com.study.dto.UserUpdateInfoDTO;
import com.study.dto.UserUpdatePwdDTO;
import com.study.vo.ReservationVO;
import com.study.vo.UserInfoVO;
import java.util.List;

public interface UserService {
    void register(UserRegisterDTO registerDTO);

    UserInfoVO getCurrentUserInfo(String username);

    void updateUserInfo(String username, UserUpdateInfoDTO updateInfoDTO);

    void updatePassword(String username, UserUpdatePwdDTO updatePwdDTO);

    void addReservation(String username, ReservationAddDTO reservationAddDTO);

    List<ReservationVO> getMyReservations(String username);

    void cancelReservation(String username, Integer reservationId);
}
