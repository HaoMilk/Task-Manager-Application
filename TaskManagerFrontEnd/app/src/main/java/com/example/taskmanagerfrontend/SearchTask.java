package com.example.taskmanagerfrontend;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerfrontend.adapter.TaskAdapter;
import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.TaskApi;
import com.example.taskmanagerfrontend.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTask extends AppCompatActivity {

    private RecyclerView recyclerViewSearchTasks;
    private TaskAdapter taskAdapter;
    private TaskApi taskApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_task);

        // Set up RecyclerView
        recyclerViewSearchTasks = findViewById(R.id.recyclerViewSearchTasks);
        recyclerViewSearchTasks.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the TaskApi
        taskApi = ApiClient.getRetrofitInstance().create(TaskApi.class);

        // Lấy đối tượng DatePicker và lắng nghe sự kiện thay đổi ngày
        DatePicker datePicker = findViewById(R.id.datePickerStartDate);
        datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            // Lấy ngày đã chọn từ DatePicker
            String selectedDate = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
            // Gọi API để tìm kiếm task bắt đầu từ ngày đã chọn
            searchTasksByStartDate(selectedDate);
        });
    }

    // Tìm kiếm các task theo ngày bắt đầu
    private void searchTasksByStartDate(String selectedDate) {
        // Gọi API để lấy danh sách task bắt đầu từ selectedDate
        Call<List<Task>> call = taskApi.getTasksByStartDate(selectedDate); // Giả sử API có phương thức này

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body();

                    if (tasks.isEmpty()) {
                        Toast.makeText(SearchTask.this, "No tasks found for the selected date", Toast.LENGTH_SHORT).show();
                    } else {
                        taskAdapter = new TaskAdapter(SearchTask.this, tasks);
                        recyclerViewSearchTasks.setAdapter(taskAdapter);
                    }
                } else {
                    Toast.makeText(SearchTask.this, "Error fetching tasks for the selected date", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(SearchTask.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
