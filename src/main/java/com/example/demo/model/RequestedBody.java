package com.example.demo.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RequestedBody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp startTime;
    private Timestamp endTime;
    private ArrayList<Long> invitees = null;

    // Getters and Setters

    public ArrayList<Long> getInvitees() {
        return invitees;
    }

    public void setInvitees(ArrayList<Long> invitees) {
        this.invitees = invitees;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "RequestedBody{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", invitees=" + invitees +
                '}';
    }
}
