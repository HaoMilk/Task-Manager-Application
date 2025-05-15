package com.example.taskmanagerbackend.controller;

import com.example.taskmanagerbackend.model.Task;
import com.example.taskmanagerbackend.model.TaskStatus;
import com.example.taskmanagerbackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Add a new Task
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.addTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // Update an existing Task
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable int taskId, @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(taskId, taskDetails);
        if (updatedTask != null) {
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete a Task by ID
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable int taskId) {
        boolean isDeleted = taskService.deleteTask(taskId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get a Task by ID
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable int taskId) {
        Optional<Task> task = taskService.getTaskById(taskId);
        if (task.isPresent()) {
            return new ResponseEntity<>(task.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get all tasks (optional, depending on needs)
    @GetMapping
    public ResponseEntity<Iterable<Task>> getAllTasks() {
        Iterable<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Get the count of tasks
    @GetMapping("/count")
    public ResponseEntity<Long> getTaskCount() {
        long count = taskService.getTaskCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }


    // Get tasks by category
    @GetMapping("/category/{category}")
    public ResponseEntity<Iterable<Task>> getTasksByCategory(@PathVariable String category) {
        Iterable<Task> tasks = taskService.getTasksByCategory(category);
        if (tasks == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Trả về lỗi nếu không tìm thấy category
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Get tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<Iterable<Task>> getTasksByStatus(@PathVariable String status) {
        TaskStatus taskStatus;
        try {
            taskStatus = TaskStatus.valueOf(status.toUpperCase());  // Convert String to TaskStatus Enum
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // If the status is invalid, return BAD_REQUEST
        }
        Iterable<Task> tasks = taskService.getTasksByStatus(taskStatus);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/startDate/{startDate}")
    public ResponseEntity<Iterable<Task>> getTasksByStartDate(@PathVariable String startDate) {
        LocalDate date;
        try {
            date = LocalDate.parse(startDate);  // parse chuỗi ngày thành LocalDate
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // nếu ngày không đúng định dạng
        }

        Iterable<Task> tasks = taskService.getTasksByStartDate(date);
        if (tasks == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}
