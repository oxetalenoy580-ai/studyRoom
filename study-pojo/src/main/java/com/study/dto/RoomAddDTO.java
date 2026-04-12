package com.study.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Data;

@Data
public class RoomAddDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private String roomId;
  private String roomName;
  private String location;
  private Integer totalSeats;

  @JsonFormat(pattern = "HH:mm:ss")
  private LocalTime openTime;

  @JsonFormat(pattern = "HH:mm:ss")
  private LocalTime closeTime;
}
