package com.study.dto;

import java.io.Serializable;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roomId;
    private String roomName;
    private String location;
    private Integer totalSeats;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime openTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime closeTime;

    // 自习室整体状态：0停用，1启用
    private Integer status;
}
