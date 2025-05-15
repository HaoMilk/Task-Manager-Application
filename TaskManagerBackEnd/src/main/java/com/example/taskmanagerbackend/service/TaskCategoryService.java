package com.example.taskmanagerbackend.service;

import com.example.taskmanagerbackend.model.TaskCategory;
import com.example.taskmanagerbackend.repository.TaskCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskCategoryService {

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    // Add Task Category
    public TaskCategory addCategory(TaskCategory category) {
        return taskCategoryRepository.save(category);
    }

    // Update Task Category
    public TaskCategory updateCategory(TaskCategory category) {
        return taskCategoryRepository.save(category);
    }

    // Get all categories
    public List<TaskCategory> getAllCategories() {
        return taskCategoryRepository.findAll();
    }

    // Find category by ID
    public Optional<TaskCategory> findCategoryById(Long id) {
        return taskCategoryRepository.findById(id);
    }

    // Delete Task Category
    public void deleteCategory(Long id) {
        taskCategoryRepository.deleteById(id);
    }
}
