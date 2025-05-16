package com.example.taskmanagerfrontend.api;

import com.example.taskmanagerfrontend.model.Task;
import com.example.taskmanagerfrontend.model.TaskCategory;
import com.example.taskmanagerfrontend.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.Map;

public interface AuthApi {
    @POST("/api/auth/login")
    Call<Map<String, String>> loginUser(@Body User user);

    // Thêm phương thức đăng ký
    @POST("/api/auth/register")
    Call<String> registerUser(@Body User user);
    @PUT("/api/users/{userId}")
    Call<User> updateUser(@Path("userId") int userId, @Body User user);
    @GET("/api/users/{userId}")
    Call<User> getUser(@Path("userId") int userId);
    // Cập nhật ảnh đại diện
    @PUT("/api/users/{userId}/avatar")
    Call<String> updateAvatar(@Path("userId") int userId, @Body String imgUrl);


}
