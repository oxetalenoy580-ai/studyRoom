package com.study.mapper;

import com.study.entity.Notice;
import com.study.entity.Room;
import com.study.entity.Seats;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PublicMapper {

    @Select("select * from room where status = 1 order by room_id asc")
    List<Room> listRooms();

    @Select("select * from room where room_id = #{roomId} and status = 1")
    Room getRoomDetail(String roomId);

    @Select("select * from seat where room_id = #{roomId} order by seat_id asc")
    List<Seats> listSeatsByRoomId(String roomId);

    @Select("select * from notice order by create_time desc, id desc")
    List<Notice> listNotices();
}
