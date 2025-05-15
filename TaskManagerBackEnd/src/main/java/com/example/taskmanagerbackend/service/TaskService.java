package com.example.taskmanagerbackend.service;

import com.example.taskmanagerbackend.model.Task;
import com.example.taskmanagerbackend.model.TaskCategory;
import com.example.taskmanagerbackend.model.TaskStatus;
import com.example.taskmanagerbackend.repository.TaskCategoryRepository;
import com.example.taskmanagerbackend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return taskRepository.save(task);
    }

    // Update an existing Task
    public Task updateTask(int taskId, Task taskDetails) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setStartDatetime(taskDetails.getStartDatetime());
            task.setEndDatetime(taskDetails.getEndDatetime());
            task.setStatus(taskDetails.getStatus());
            task.setUser(taskDetails.getUser());
            task.setCategory(taskDetails.getCategory());
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

    // Get all tasks (optional, depending on needs)
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
        return null;  // Trả về null nếu không tìm thấy category
    }

    // Lấy Task theo status
    public Iterable<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }
}
