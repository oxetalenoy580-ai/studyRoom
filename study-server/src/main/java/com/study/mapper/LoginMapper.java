package com.study.mapper;

import com.study.entity.Admin;
import com.study.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {

  @Select("select * from admin where username = #{username}")
  Admin findByAdminname(String username);

  @Select("select * from user where username = #{username}")
  User findByUsername(String username);
}
