package com.study.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserQueryVO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String username;

  private String name;

  private String phone;

  private Integer pageNum = 1;

  private Integer pageSize = 10;
}
