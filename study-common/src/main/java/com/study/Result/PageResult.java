package com.study.Result;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class PageResult implements Serializable {
  private Long total;
  private List<?> records;

  public PageResult(Long total, List<?> records) {
    this.total = total;
    this.records = records;
  }
}
