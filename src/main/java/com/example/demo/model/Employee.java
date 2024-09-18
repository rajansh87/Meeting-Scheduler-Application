package com.example.demo.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Employee {
    @Id
    private Long id;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + (id != null ? id : 0) +
                ", name='" + (name != null ? name : "N/A") +
                ", email='" + (email != null ? email : "N/A") +
                ", contact='" + (contact != null ? contact : "N/A") +
                ", meetings=" + (meetings != null ? meetings.size() : "N/A") +
                '}';
    }


    private String name;
    private String email;
    private String contact;

    @ManyToMany(mappedBy = "invitees")
    private Set<Meetings> meetings=new HashSet<>();

    public Employee() {
    }

    public Employee(Long id,String name, String email, String contact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Set<Meetings> getMeetings() {
        return meetings;
    }

    public void setMeetings(Set<Meetings> meetings) {
        this.meetings = meetings;
    }
}
