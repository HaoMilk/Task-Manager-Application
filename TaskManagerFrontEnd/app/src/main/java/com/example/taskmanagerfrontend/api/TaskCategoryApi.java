package com.example.taskmanagerfrontend.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;  // Thêm import này

import com.example.taskmanagerfrontend.model.Task;
import com.example.taskmanagerfrontend.model.TaskCategory;

public interface TaskCategoryApi {
    @GET("/api/task-categories")  // Địa chỉ API để lấy danh sách các danh mục công việc
    Call<List<TaskCategory>> getAllCategories();
    @GET("/api/task-categories")
    Call<List<TaskCategory>> getTaskCategories();
    @GET("/api/task-categories/{id}/task-count")
    Call<Integer> getTaskCountForCategory(@Path("id") int categoryId);

}
