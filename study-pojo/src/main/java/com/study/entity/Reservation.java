package com.study.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String userUsername;
    private String roomId;
    private String seatId;
    private LocalDate reserveDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer status;
    private LocalDateTime createTime;
}
