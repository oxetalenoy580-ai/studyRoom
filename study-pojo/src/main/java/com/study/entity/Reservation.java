package com.study.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
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
  private String roomId;
  private String userName;
  private String seatId;
  private LocalDateTime reserveDate;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  // 预约状态：0待使用，1已使用，2已取消，3逾期
  private Integer status;
  private LocalDateTime createTime;
}
