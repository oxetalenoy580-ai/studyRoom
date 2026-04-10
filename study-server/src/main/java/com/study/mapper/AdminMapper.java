package com.study.mapper;

import com.study.dto.RoomUpdateDTO;
import com.study.dto.UserQueryDTO;
import com.study.entity.Room;
import com.study.entity.Seats;
import com.study.vo.UserVO;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AdminMapper {

    List<UserVO> queryUserList(UserQueryDTO queryDTO);

    @Insert("insert into room (room_id, room_name, location, total_seats, open_time, close_time, status,"
            + " full_status) values (#{roomId}, #{roomName}, #{location}, #{totalSeats}, #{openTime},"
            + " #{closeTime}, #{status}, #{fullStatus})")
    void addRoom(Room newRoom);

    @Select("Select * from room where room_id = #{roomId}")
    Room getRoomInfo(String roomId);

    @Delete("Delete from room where room_id = #{roomId}")
    void deleteRoom(String roomId);

    @Insert("insert into seats (seat_id, room_id, seat_number, status) values (#{seatId}, #{roomId}, #{seatNumber}, #{status})")
    void addSeatForRoom(Seats seat);

    @Update("update room set room_name=#{roomName}, location=#{location}, total_seats=#{totalSeats}, open_time=#{openTime}, close_time=#{closeTime}, status=#{status} where room_id=#{roomId}")
    void updateRoom(RoomUpdateDTO roomuUpdateDTO);
}
