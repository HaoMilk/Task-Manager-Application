package com.example.taskmanagerfrontend.api;

import com.example.taskmanagerfrontend.model.GetStatus;
import com.example.taskmanagerfrontend.model.Task;
import com.example.taskmanagerfrontend.model.TaskStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskApi {

    // Tạo mới task
    @POST("/api/tasks")
    Call<Task> createTask(@Body Task task);

    // Lấy tất cả các task theo categoryId
    @GET("/api/tasks/category/{categoryId}")
    Call<List<Task>> getTasksByCategory(@Path("categoryId") int categoryId);

    // Lấy task theo ID
    @GET("/api/tasks/{taskId}")
    Call<Task> getTaskById(@Path("taskId") int taskId);

    // Cập nhật task
    @PUT("/api/tasks/{taskId}")
    Call<Task> updateTask(@Path("taskId") int taskId, @Body Task task);

    // Xoá task theo ID
    @DELETE("/api/tasks/{taskId}")
    Call<Void> deleteTask(@Path("taskId") int taskId);

    // Gọi API để lấy danh sách task theo ngày bắt đầu
    @GET("/api/tasks/startDate/{startDate}")
    Call<List<Task>> getTasksByStartDate(@Path("startDate") String startDate);
    @GET("api/tasks/status/count")
    Call<List<GetStatus>> getTaskStatuses(); // API để lấy trạng thái task

}
