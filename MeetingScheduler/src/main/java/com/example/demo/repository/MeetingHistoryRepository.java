package com.example.demo.repository;

import com.example.demo.model.MeetingHistory;
import com.example.demo.model.Meetings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;


@Repository
public interface MeetingHistoryRepository extends JpaRepository<MeetingHistory, Long> {
    // Additional custom queries can be added here if needed

//    @Query(value = "SELECT * FROM meeting_history WHERE start_time >= :startTimestamp AND end_time <= :endTimestamp", nativeQuery = true)
//    ArrayList<MeetingHistory> findByStartTimeBetween(@Param("startTimestamp") Timestamp startTimestamp, @Param("endTimestamp") Timestamp endTimestamp, @Param("pageable") Pageable pageable);

    @Query(value = "SELECT * FROM meeting_history WHERE start_time >= :startTimestamp AND end_time <= :endTimestamp", nativeQuery = true)
    Page<MeetingHistory> findByStartTimeBetween(@Param("startTimestamp") Timestamp startTimestamp, @Param("endTimestamp") Timestamp endTimestamp, Pageable pageable);


}