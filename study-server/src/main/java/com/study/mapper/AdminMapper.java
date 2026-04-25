package com.study.mapper;

import com.study.dto.RoomUpdateDTO;
import com.study.dto.UserQueryDTO;
import com.study.entity.Notice;
import com.study.entity.Reservation;
import com.study.entity.Room;
import com.study.entity.Seats;
import com.study.vo.ReservationVO;
import com.study.vo.UserVO;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AdminMapper {

    List<UserVO> queryUserList(UserQueryDTO queryDTO);

    List<ReservationVO> listReservationVOs();

    // Room
    @Insert("insert into room (room_id, room_name, location, total_seats, open_time, close_time, status,"
            + " full_status) values (#{roomId}, #{roomName}, #{location}, #{totalSeats}, #{openTime},"
            + " #{closeTime}, #{status}, #{fullStatus})")
    void addRoom(Room newRoom);

    @Select("select count(1) from room where room_id = #{roomId}")
    Integer countRoomByRoomId(String roomId);

    @Select("select * from room where room_id = #{roomId}")
    Room getRoomInfo(String roomId);

    @Delete("delete from room where room_id = #{roomId}")
    void deleteRoom(String roomId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into seat (seat_id, room_id, seat_number, status, create_time) values (#{seatId}, #{roomId}, #{seatNumber}, #{status}, now())")
    void addSeatForRoom(Seats seat);

    @Select("select * from seat where seat_id = #{seatId}")
    Seats getSeatById(String seatId);

    @Delete("delete from seat where seat_id = #{seatId}")
    void deleteSeat(String seatId);

    @Update("""
            update room
            set location = COALESCE(#{location}, location),
                total_seats = COALESCE(#{totalSeats}, total_seats),
                open_time = COALESCE(#{openTime}, open_time),
                close_time = COALESCE(#{closeTime}, close_time),
                status = COALESCE(#{status}, status)
            where room_id = #{roomId}
            """)
    void updateRoom(RoomUpdateDTO roomUpdateDTO);

    @Select("select count(1) from seat where room_id = #{roomId}")
    Integer countSeatsByRoomId(String roomId);

    @Select("select count(1) from seat where room_id = #{roomId} and status = #{status}")
    Integer countSeatsByRoomIdAndStatus(@Param("roomId") String roomId, @Param("status") Integer status);

    @Update("update room set total_seats = #{totalSeats} where room_id = #{roomId}")
    void updateRoomTotalSeats(@Param("roomId") String roomId, @Param("totalSeats") Integer totalSeats);

    @Update("update room set full_status = #{fullStatus} where room_id = #{roomId}")
    void updateRoomFullStatus(@Param("roomId") String roomId, @Param("fullStatus") Integer fullStatus);

    @Update("update seat set status = #{status} where seat_id = #{seatId}")
    void updateSeatStatus(@Param("seatId") String seatId, @Param("status") Integer status);

    @Select("select count(1) from reservation where seat_id = #{seatId} and status = #{status}")
    Integer countReservationsBySeatAndStatus(@Param("seatId") String seatId, @Param("status") Integer status);

    @Select("select * from reservation where id = #{id}")
    Reservation getReservationById(Integer id);

    @Update("update reservation set status = #{status} where id = #{id}")
    void updateReservationStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Delete("delete from reservation where room_id = #{roomId}")
    void deleteReservationsByRoomId(String roomId);

    @Delete("delete from reservation where seat_id = #{seatId}")
    void deleteReservationsBySeatId(String seatId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into notice (title, content, create_time) values (#{title}, #{content}, now())")
    void addNotice(Notice notice);

    @Delete("delete from notice where id = #{id}")
    void deleteNotice(Integer id);

    @Select("select count(1) from notice where id = #{id}")
    Integer countNoticeById(Integer id);

    // Seat
    @Delete("Delete from seat where room_id = #{roomId}")
    void deleteSeatsByRoomId(String roomId);

    @Delete("Delete from seat where seat_id = #{seatId}")
    void deleteSeatForRoom(String seatId);

    @Select("select * from seat where room_id = #{roomId} and seat_number > #{seatNumber} order by seat_number")
    List<Seats> findSeatsAfter(String roomId, Integer seatNumber);

    @Update("update seat set seat_id = #{newSeatId}, seat_number = #{newSeatNumber} where seat_id = #{oldSeatId}")
    void updateSeat(@Param("oldSeatId") String oldSeatId, @Param("newSeatId") String newSeatId,
            @Param("newSeatNumber") int newSeatNumber);

    @Select("select count(*) from seat where room_id = #{roomId} and status = 1")
    int countReservedSeats(String roomId);

    @Select("select total_seats from room where room_id = #{roomId}")
    Integer getTotalSeats(String roomId);

    @Select("select count(1) from reservation where room_id = #{roomId} and status in (0, 1)")
    Integer countActiveReservationsByRoomId(String roomId);

}
