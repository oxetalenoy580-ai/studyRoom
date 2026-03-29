package com.study.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seats implements Serializable {
  private static final long serialVersionUID = 1L;
  private String seatId;
  private String roomId;
  private Integer seatNumber;

  // 座位状态：0可预约，1已预约
  private Integer status;
}
