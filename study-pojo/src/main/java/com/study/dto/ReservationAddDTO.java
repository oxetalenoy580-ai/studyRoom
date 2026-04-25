package com.study.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class ReservationAddDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String roomId;
    private String seatId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
}
