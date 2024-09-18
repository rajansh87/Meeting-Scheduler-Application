package com.example.demo.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
public class Meetings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private Timestamp startTime;
    private Timestamp endTime;
    private Date date;

    @ManyToMany
    @JoinTable(
            name = "Meeting_Invitees",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "emp_id")
    )
    private Set<Employee> invitees;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Employee> getInvitees() {
        return invitees;
    }

    public void setInvitees(Set<Employee> invitees) {
        this.invitees = invitees;
    }

    @Override
    public String toString() {
        return "Meetings{" +
                "id=" + id +
                ", room=" + room +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", date=" + date +
                ", invitees=" + invitees.toString() +
                '}';
    }
}
