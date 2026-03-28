package com.study.mapper;

import com.study.entity.User;
import com.study.vo.UserQueryVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

  List<User> queryUserList(UserQueryVO queryVO);

  Long queryUserCount(UserQueryVO queryVO);
}
