package com.study.mapper;

import com.study.dto.UserQueryDTO;
import com.study.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

  List<User> queryUserList(UserQueryDTO queryDTO);

  Long queryUserCount(UserQueryDTO queryDTO);
}
