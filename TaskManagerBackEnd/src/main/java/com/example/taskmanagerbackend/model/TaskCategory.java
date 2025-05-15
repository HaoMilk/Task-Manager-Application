package com.example.taskmanagerbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "task_categories")
public class TaskCategory {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(nullable = false)
        private String name;

        @Column(name = "image_url")
        private String imageUrl;

}
