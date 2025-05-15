package com.example.taskmanagerfrontend.model;

public class GetStatus {
    private String status;  // String type for status instead of enum (if API returns String)
    private int count;      // Number of tasks in that status

    // Constructor
    public GetStatus(String status, int count) {
        this.status = status;
        this.count = count;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter for count
    public int getCount() {
        return count;
    }

    // Setter for count
    public void setCount(int count) {
        this.count = count;
    }
}
