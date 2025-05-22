package com.example.taskmanagerfrontend;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerfrontend.adapter.NotificationAdapter;
import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.TaskApi;
import com.example.taskmanagerfrontend.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTasksForTomorrow();
    }

    private void loadTasksForTomorrow() {
        TaskApi api = ApiClient.getRetrofitInstance().create(TaskApi.class);

        // Lấy ngày mai theo kiểu truyền thống
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String tomorrowStr = sdf.format(calendar.getTime());

        Call<List<Task>> call = api.getTasksByStartDate(tomorrowStr);
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Task> tasks = response.body();
                    adapter = new NotificationAdapter(Notification.this, tasks);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Notification.this, "Không có công việc nào diễn ra ngày mai", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(Notification.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
