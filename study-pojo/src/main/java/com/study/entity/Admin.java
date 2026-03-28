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
public class Admin implements Serializable {
  private static final long serialVersionUID = 1L;
  private Integer id;
  private String username;
  private String password;
  private String name;
  private LocalDateTime createTime;
}
