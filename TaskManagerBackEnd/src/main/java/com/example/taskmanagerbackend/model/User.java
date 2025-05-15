package com.example.taskmanagerbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

//    @OneToMany(mappedBy = "user")
//    private Set<Task> tasks;

//    @OneToMany(mappedBy = "user")
//    private Set<TaskComment> comments;

    public User() {

    }
}

