package com.example.taskmanagerbackend.model;

public class GetStatus {
    private String status;  // Trạng thái task như PENDING, IN_PROGRESS, v.v.
    private int count;      // Số lượng task trong trạng thái đó

    // Constructor
    public GetStatus(String status, int count) {
        this.status = status;
        this.count = count;
    }

    // Getter và Setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
