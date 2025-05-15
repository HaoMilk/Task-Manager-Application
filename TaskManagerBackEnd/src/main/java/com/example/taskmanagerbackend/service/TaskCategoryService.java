package com.example.taskmanagerbackend.service;

import com.example.taskmanagerbackend.model.TaskCategory;
import com.example.taskmanagerbackend.repository.TaskCategoryRepository;
import com.example.taskmanagerbackend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskCategoryService {

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @Autowired
    private TaskRepository taskRepository;

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

    // Delete Task Category, only if no tasks belong to this category
    public void deleteCategory(Long id) throws Exception {
        Optional<TaskCategory> categoryOpt = taskCategoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            TaskCategory category = categoryOpt.get();
            boolean hasTasks = taskRepository.existsByCategory(category);
            if (hasTasks) {
                throw new Exception("Cannot delete category because it still has tasks.");
            }
            taskCategoryRepository.deleteById(id);
        } else {
            throw new Exception("Category not found.");
        }
    }
}
