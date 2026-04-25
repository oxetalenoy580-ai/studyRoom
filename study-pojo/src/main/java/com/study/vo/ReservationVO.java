package com.study.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

@Data
public class ReservationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String userUsername;
    private String userName;
    private String roomId;
    private String roomName;
    private String seatId;
    private LocalDate reserveDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer status;
    private LocalDateTime createTime;
}
