package com.quevent.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users") // @Table() is defined because user is a reserved keyword in PostgreSQL
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    // One-to-many relationship with Event
    @OneToMany(mappedBy = "user")  // The "user" field in Event
    private List<Event> events;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
