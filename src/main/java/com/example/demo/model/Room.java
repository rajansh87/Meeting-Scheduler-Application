package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;

@Transactional
@Data
@Table(name = "Room")
@Entity
public class Room {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public String toString() {
        return "Room{" +
                "id=" + (id != null ? id : "null") +
                ", name='" + (name != null ? name : "null") + '\'' +
                '}';
    }

    private String name;

    public Room(Long id,String name) {
        this.id = id;
        this.name = name;
    }

    public Room() {
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
}
