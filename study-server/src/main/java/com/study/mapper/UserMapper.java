package com.study.mapper;

import com.study.entity.Reservation;
import com.study.entity.Room;
import com.study.entity.Seats;
import com.study.entity.User;
import com.study.vo.ReservationVO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    User getUserByUsername(String username);

    @Update("update user set name = #{name}, phone = #{phone} where username = #{username}")
    void updateUserInfo(@Param("username") String username, @Param("name") String name, @Param("phone") String phone);

    @Update("update user set password = #{password} where username = #{username}")
    void updateUserPassword(@Param("username") String username, @Param("password") String password);

    @Select("select * from room where room_id = #{roomId}")
    Room getRoomById(String roomId);

    @Select("select * from seat where seat_id = #{seatId}")
    Seats getSeatById(String seatId);

    @Select("""
            select count(1)
            from reservation
            where seat_id = #{seatId}
              and reserve_date = #{reserveDate}
              and status = 0
              and start_time < #{endTime}
              and end_time > #{startTime}
            """)
    Integer countSeatConflict(@Param("seatId") String seatId,
            @Param("reserveDate") LocalDate reserveDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Select("""
            select count(1)
            from reservation
            where user_username = #{username}
              and reserve_date = #{reserveDate}
              and status = 0
              and start_time < #{endTime}
              and end_time > #{startTime}
            """)
    Integer countUserConflict(@Param("username") String username,
            @Param("reserveDate") LocalDate reserveDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("""
            insert into reservation (user_username, room_id, seat_id, reserve_date, start_time, end_time, status, create_time)
            values (#{userUsername}, #{roomId}, #{seatId}, #{reserveDate}, #{startTime}, #{endTime}, #{status}, now())
            """)
    void addReservation(Reservation reservation);

    @Select("""
            select r.id,
                   r.user_username,
                   u.name as user_name,
                   r.room_id,
                   rm.room_name,
                   r.seat_id,
                   r.reserve_date,
                   r.start_time,
                   r.end_time,
                   r.status,
                   r.create_time
            from reservation r
            left join user u on u.username = r.user_username
            left join room rm on rm.room_id = r.room_id
            where r.user_username = #{username}
            order by r.create_time desc, r.id desc
            """)
    List<ReservationVO> listReservationByUsername(String username);

    @Select("select * from reservation where id = #{id}")
    Reservation getReservationById(Integer id);

    @Update("update reservation set status = #{status} where id = #{id}")
    void updateReservationStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Select("select count(1) from reservation where seat_id = #{seatId} and status = #{status}")
    Integer countReservationsBySeatAndStatus(@Param("seatId") String seatId, @Param("status") Integer status);

    @Update("update seat set status = #{status} where seat_id = #{seatId}")
    void updateSeatStatus(@Param("seatId") String seatId, @Param("status") Integer status);

    @Select("select count(1) from seat where room_id = #{roomId}")
    Integer countSeatsByRoomId(String roomId);

    @Select("select count(1) from seat where room_id = #{roomId} and status = #{status}")
    Integer countSeatsByRoomIdAndStatus(@Param("roomId") String roomId, @Param("status") Integer status);

    @Update("update room set full_status = #{fullStatus} where room_id = #{roomId}")
    void updateRoomFullStatus(@Param("roomId") String roomId, @Param("fullStatus") Integer fullStatus);
}
