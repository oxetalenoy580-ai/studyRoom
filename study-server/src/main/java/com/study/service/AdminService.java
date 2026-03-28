package com.study.service;

import com.study.Result.PageResult;
import com.study.vo.UserQueryVO;

public interface AdminService {

  PageResult getUserList(UserQueryVO queryVO);
}
