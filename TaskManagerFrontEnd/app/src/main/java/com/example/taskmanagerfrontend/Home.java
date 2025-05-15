package com.example.taskmanagerfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerfrontend.adapter.CategoryAdapter;
import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.TaskCategoryApi;
import com.example.taskmanagerfrontend.model.TaskCategory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        recyclerView = findViewById(R.id.recyclerViewCategory);
        FloatingActionButton addTask = findViewById(R.id.fabAdd);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch categories from API
        getTaskCategories();

        // BottomNavigationView logic with if-else
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.item_1) {
                    // Home
                    if (!(Home.this instanceof Home)) {
                        Intent intent = new Intent(Home.this, Home.class);
                        startActivity(intent);
                    }
                    return true;
                } else if (item.getItemId() == R.id.item_2) {
                    // SearchTask
                    Intent intent = new Intent(Home.this, SearchTask.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.item_3) {
                    // TaskStatusChart
                    Intent intent = new Intent(Home.this, TaskStatusChart.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.item_4) {
                    // Account
                    Intent intent = new Intent(Home.this, Account.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // FAB click logic to add task
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddTask();
            }
        });
    }

    // Fetch TaskCategories from API
    private void getTaskCategories() {
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        TaskCategoryApi api = retrofit.create(TaskCategoryApi.class);

        Call<List<TaskCategory>> call = api.getAllCategories();
        call.enqueue(new Callback<List<TaskCategory>>() {
            @Override
            public void onResponse(Call<List<TaskCategory>> call, Response<List<TaskCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskCategory> categories = response.body();
                    adapter = new CategoryAdapter(Home.this, categories);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Home.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TaskCategory>> call, Throwable t) {
                Toast.makeText(Home.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Navigation methods for BottomNavigationView
    private void navigateToHome() {
        if (!(Home.this instanceof Home)) {
            Intent intent = new Intent(Home.this, Home.class);
            startActivity(intent);
        }
    }

    private void navigateToSearchTask() {
        Intent intent = new Intent(Home.this, SearchTask.class);
        startActivity(intent);
    }

    private void navigateToTaskStatusChart() {
        Intent intent = new Intent(Home.this, TaskStatusChart.class);
        startActivity(intent);
    }

    private void navigateToAccount() {
        Intent intent = new Intent(Home.this, Account.class);
        startActivity(intent);
    }

    private void navigateToAddTask() {
        Intent intent = new Intent(Home.this, AddTask.class);
        startActivity(intent);
    }
}
