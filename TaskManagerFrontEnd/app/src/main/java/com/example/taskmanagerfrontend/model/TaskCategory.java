package com.example.taskmanagerfrontend.model;

public class TaskCategory {
    private int id;
    private String name;
    private int taskCount;
    private String imageUrl;


    public TaskCategory(int id, String name, int taskCount, String imageUrl) {
        this.id = id;
        this.name = name;
        this.taskCount = taskCount;
        this.imageUrl = imageUrl;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public TaskCategory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
