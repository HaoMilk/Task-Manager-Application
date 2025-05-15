package com.example.taskmanagerbackend.repository;

import com.example.taskmanagerbackend.model.Task;
import com.example.taskmanagerbackend.model.TaskCategory;
import com.example.taskmanagerbackend.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByCategory(TaskCategory category);
    List<Task> findByStatus(TaskStatus status);
    boolean existsByCategory(TaskCategory category);
    // Thêm phương thức tìm task theo ngày bắt đầu
    List<Task> findByStartDate(LocalDate startDate);
}
