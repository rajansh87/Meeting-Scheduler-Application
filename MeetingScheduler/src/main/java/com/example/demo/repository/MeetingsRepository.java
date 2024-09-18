package com.example.demo.repository;
import com.example.demo.model.Employee;
import com.example.demo.model.Meetings;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface MeetingsRepository extends JpaRepository<Meetings, Long> {
    @Query(value = "SELECT * FROM meetings WHERE start_time >= :start AND end_time <= :end", nativeQuery = true)
    ArrayList<Meetings> findAllOverlappingMeetings(@Param("start") Timestamp start, @Param("end") Timestamp end);

    @Query(value = "SELECT * FROM meetings WHERE room_id = :roomId AND start_time < :end AND end_time > :start", nativeQuery = true)
    ArrayList<Meetings> findAllOverlappingMeetingsInRoom(@Param("roomId") Long roomId,@Param("start") Timestamp start, @Param("end") Timestamp end);

    @Query(value = "SELECT id FROM meetings WHERE end_time <= :time", nativeQuery = true)
    ArrayList<Long> findByEndTimeBeforeTime(@Param("time") Timestamp time);

    @Query(value = "SELECT * FROM meetings WHERE start_time BETWEEN :startTime AND :endTime", nativeQuery = true)
    ArrayList<Meetings> findByStartTimeBetween(@Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime);


}

