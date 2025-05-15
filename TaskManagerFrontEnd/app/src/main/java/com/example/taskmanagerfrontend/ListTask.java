package com.example.taskmanagerfrontend;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerfrontend.adapter.CategoryAdapter;
import com.example.taskmanagerfrontend.adapter.TaskAdapter;
import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.TaskApi;
import com.example.taskmanagerfrontend.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTask extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);

        recyclerView = findViewById(R.id.recyclerViewSearchTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy categoryId từ biến static (CategoryData)
        int categoryId = CategoryAdapter.categoryId;
        Log.d("ListTask", "categoryId: " + categoryId);  // Kiểm tra xem categoryId đã nhận được chưa

        // Kiểm tra categoryId không phải là giá trị mặc định
        if (categoryId != 0) {  // Kiểm tra nếu categoryId hợp lệ
            getTasksForCategory(categoryId);  // Gọi API để lấy danh sách task cho category
        } else {
            Toast.makeText(this, "Category ID is missing or invalid", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức gọi API để lấy danh sách task cho category
    private void getTasksForCategory(int categoryId) {
        TaskApi taskApi = ApiClient.getRetrofitInstance().create(TaskApi.class);
        Call<List<Task>> call = taskApi.getTasksByCategory(categoryId);

        // Thực hiện API call
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body();
                    // Kiểm tra nếu không có task nào
                    if (tasks.isEmpty()) {
                        Toast.makeText(ListTask.this, "No tasks found for this category", Toast.LENGTH_SHORT).show();
                    } else {
                        taskAdapter = new TaskAdapter(ListTask.this, tasks);
                        recyclerView.setAdapter(taskAdapter);
                    }
                } else {
                    Toast.makeText(ListTask.this, "No tasks available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(ListTask.this, "Error fetching tasks: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
