package com.example.taskmanagerbackend.service;

import com.example.taskmanagerbackend.model.Task;
import com.example.taskmanagerbackend.model.TaskCategory;
import com.example.taskmanagerbackend.model.TaskStatus;
import com.example.taskmanagerbackend.repository.TaskCategoryRepository;
import com.example.taskmanagerbackend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskCategoryRepository taskCategoryRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskCategoryRepository taskCategoryRepository) {
        this.taskRepository = taskRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    // Add a new Task
    public Task addTask(Task task) {
        // Kiểm tra dữ liệu bắt buộc không được null
        if (task.getUser() == null || task.getCategory() == null || task.getTitle() == null) {
            throw new IllegalArgumentException("User, category and title must not be null");
        }
        // Kiểm tra ngày hợp lệ: startDatetime không được sau endDatetime (nếu có)
        if (task.getStartDatetime() != null && task.getEndDatetime() != null) {
            if (task.getStartDatetime().isAfter(task.getEndDatetime())) {
                throw new IllegalArgumentException("Start datetime must not be after end datetime");
            }
        }
        return taskRepository.save(task);
    }

    // Update an existing Task
    public Task updateTask(int taskId, Task taskDetails) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            // Cập nhật từng trường nếu không null
            if (taskDetails.getTitle() != null) task.setTitle(taskDetails.getTitle());
            if (taskDetails.getDescription() != null) task.setDescription(taskDetails.getDescription());
            if (taskDetails.getStartDatetime() != null) task.setStartDatetime(taskDetails.getStartDatetime());
            if (taskDetails.getEndDatetime() != null) task.setEndDatetime(taskDetails.getEndDatetime());
            if (taskDetails.getStatus() != null) task.setStatus(taskDetails.getStatus());
            if (taskDetails.getUser() != null) task.setUser(taskDetails.getUser());
            if (taskDetails.getCategory() != null) task.setCategory(taskDetails.getCategory());

            // Kiểm tra ngày hợp lệ sau khi cập nhật
            if (task.getStartDatetime() != null && task.getEndDatetime() != null) {
                if (task.getStartDatetime().isAfter(task.getEndDatetime())) {
                    throw new IllegalArgumentException("Start datetime must not be after end datetime");
                }
            }

            return taskRepository.save(task);
        }
        return null;
    }

    // Delete a Task by ID
    public boolean deleteTask(int taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            taskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }

    // Get Task by ID
    public Optional<Task> getTaskById(int taskId) {
        return taskRepository.findById(taskId);
    }

    // Get all tasks
    public Iterable<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get count of all tasks
    public long getTaskCount() {
        return taskRepository.count();
    }

    // Lấy Task theo category
    public Iterable<Task> getTasksByCategory(String categoryName) {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findByName(categoryName);
        if (taskCategory.isPresent()) {
            return taskRepository.findByCategory(taskCategory.get());
        }
        // Trả về danh sách rỗng thay vì null
        return java.util.Collections.emptyList();
    }

    // Lấy Task theo status
    public Iterable<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    // Lấy Task theo start date (bằng LocalDate)
    public Iterable<Task> getTasksByStartDate(LocalDate startDate) {
        // Truy vấn trực tiếp từ repository thay vì lọc trong bộ nhớ
        return taskRepository.findByStartDatetime(startDate);
    }

    public int getTaskCountByCategoryId(Long categoryId) {
        return taskRepository.countByCategoryId(categoryId); // Giả sử bạn có phương thức countByCategoryId
    }

    // Method in TaskService
    public Iterable<Task> getTasksByCategoryId(int categoryId) {
        return taskRepository.findByCategoryId(categoryId);  // Giả sử bạn có một phương thức trong repository để tìm theo categoryId
    }

}

