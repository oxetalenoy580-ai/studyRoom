package com.study.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.Result.PageResult;
import com.study.entity.User;
import com.study.mapper.AdminMapper;
import com.study.service.AdminService;
import com.study.vo.UserQueryVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

  @Autowired private AdminMapper adminMapper;

  @Override
  public PageResult getUserList(UserQueryVO queryVO) {
    PageHelper.startPage(queryVO.getPageNum(), queryVO.getPageSize());
    List<User> userList = adminMapper.queryUserList(queryVO);
    PageInfo<User> pageInfo = new PageInfo<>(userList);
    return new PageResult(pageInfo.getTotal(), pageInfo.getList());
  }
}
