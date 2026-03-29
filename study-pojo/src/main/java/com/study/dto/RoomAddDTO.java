package com.study.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RoomAddDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private String roomId;
  private String roomName;
  private String location;
  private Integer totalSeats;
  private LocalDateTime openTime;
  private LocalDateTime closeTime;
}
