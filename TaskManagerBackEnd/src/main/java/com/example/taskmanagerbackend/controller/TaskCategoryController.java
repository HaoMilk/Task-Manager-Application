package com.example.taskmanagerbackend.controller;

import com.example.taskmanagerbackend.model.TaskCategory;
import com.example.taskmanagerbackend.service.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/task-categories")
public class TaskCategoryController {

    @Autowired
    private TaskCategoryService taskCategoryService;

    // Thêm một TaskCategory mới
    @PostMapping
    public ResponseEntity<TaskCategory> createCategory(@RequestBody TaskCategory taskCategory) {
        try {
            TaskCategory savedCategory = taskCategoryService.addCategory(taskCategory);
            return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Cập nhật một TaskCategory
    @PutMapping("/{id}")
    public ResponseEntity<TaskCategory> updateCategory(@PathVariable("id") Long id, @RequestBody TaskCategory taskCategory) {
        Optional<TaskCategory> existingCategory = taskCategoryService.findCategoryById(id);
        if (existingCategory.isPresent()) {
            TaskCategory updatedCategory = existingCategory.get();
            updatedCategory.setName(taskCategory.getName());
            taskCategoryService.updateCategory(updatedCategory);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Lấy danh sách tất cả TaskCategories
    @GetMapping
    public ResponseEntity<List<TaskCategory>> getAllCategories() {
        try {
            List<TaskCategory> categories = taskCategoryService.getAllCategories();
            if (categories.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Xoá một TaskCategory theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) throws Exception {
        Optional<TaskCategory> existingCategory = taskCategoryService.findCategoryById(id);
        if (existingCategory.isPresent()) {
            taskCategoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
