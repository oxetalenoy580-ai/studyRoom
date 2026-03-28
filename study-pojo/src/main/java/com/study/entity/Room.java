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
public class Room implements Serializable {
  private static final long serialVersionUID = 1L;

  private String roomId;
  private String roomName;
  private String location;
  private Integer totalSeats;
  private LocalDateTime openTime;
  private LocalDateTime closeTime;

  // 自习室整体状态：0停用，1启用
  private Integer status;

  // 当前时段满员状态：0未满，1已满（所有座位被预约）
  private Integer fullStatus;
}
