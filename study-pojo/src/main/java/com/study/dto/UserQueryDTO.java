package com.study.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserQueryDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String username;

  private String name;

  private String phone;

  private Integer pageNum = 1;

  private Integer pageSize = 10;
}
