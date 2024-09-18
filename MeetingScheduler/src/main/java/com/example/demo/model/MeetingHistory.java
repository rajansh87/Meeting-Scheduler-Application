package com.example.demo.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class MeetingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long meetingId;
    private Timestamp startTime;
    private Timestamp endTime;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "MeetingHistory{" +
                "id=" + id +
                ", meetingId=" + meetingId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
