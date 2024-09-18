package com.example.demo.repository;

import com.example.demo.model.Meetings;
import com.example.demo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // Additional custom queries can be added here if needed
}
