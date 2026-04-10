package com.study.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserVO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String username;

  private String name;

  private String phone;

  private LocalDateTime createTime;
}
