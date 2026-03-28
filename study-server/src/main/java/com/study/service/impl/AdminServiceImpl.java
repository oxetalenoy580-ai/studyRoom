package com.study.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.Result.PageResult;
import com.study.dto.UserQueryDTO;
import com.study.entity.User;
import com.study.mapper.AdminMapper;
import com.study.service.AdminService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

  @Autowired private AdminMapper adminMapper;

  @Override
  public PageResult getUserList(UserQueryDTO queryDTO) {
    PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
    List<User> userList = adminMapper.queryUserList(queryDTO);
    PageInfo<User> pageInfo = new PageInfo<>(userList);
    return new PageResult(pageInfo.getTotal(), pageInfo.getList());
  }
}
