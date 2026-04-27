package com.study.mapper;

import com.study.entity.Notice;
import com.study.entity.Room;
import com.study.entity.Seats;
import com.study.entity.User;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PublicMapper {
    @Select("select count(1) from user where username = #{username}")
    Integer countUserByUsername(String username);

    @Insert("insert into user (username, password, name, phone, create_time) values (#{username}, #{password}, #{name}, #{phone}, now())")
    void addUser(User user);

    @Select("select * from room where status = 1 order by room_id asc")
    List<Room> listRooms();

    @Select("select * from room where room_id = #{roomId} and status = 1")
    Room getRoomDetail(String roomId);

    @Select("select * from seat where room_id = #{roomId} order by seat_id asc")
    List<Seats> listSeatsByRoomId(String roomId);

    @Select("select * from notice order by create_time desc, id desc")
    List<Notice> listNotices();
}
