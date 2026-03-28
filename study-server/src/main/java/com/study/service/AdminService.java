package com.study.service;

import com.study.Result.PageResult;
import com.study.dto.UserQueryDTO;

public interface AdminService {

  PageResult getUserList(UserQueryDTO queryDTO);
}
