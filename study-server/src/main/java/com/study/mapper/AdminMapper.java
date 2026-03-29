package com.study.mapper;

import com.study.dto.UserQueryDTO;
import com.study.entity.Room;
import com.study.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

  List<User> queryUserList(UserQueryDTO queryDTO);

  Long queryUserCount(UserQueryDTO queryDTO);

  @Insert(
      "insert into room (room_id, room_name, location, total_seats, open_time, close_time, status,"
          + " full_status) values (#{roomId}, #{roomName}, #{location}, #{totalSeats}, #{openTime},"
          + " #{closeTime}, #{status}, #{fullStatus})")
  void addRoom(Room newRoom);
}
