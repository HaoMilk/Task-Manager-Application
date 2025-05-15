package com.example.taskmanagerfrontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
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
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;

    private TextView txtHelloUser;  // Add TextView to show "Hello + username"
    private String userName = Login.UserName; // Assuming you get the username from Login class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        recyclerView = findViewById(R.id.recyclerViewCategory);
        txtHelloUser = findViewById(R.id.txtHelloUser);  // Initialize TextView

        FloatingActionButton addTask = findViewById(R.id.fabAdd);

        // Get userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId == null) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            // Optionally, navigate to login screen
        }

        // Display "Hello + userName" in the TextView
        if (userName != null) {
            txtHelloUser.setText("Hello, " + userName); // Display "Hello + username"
        }

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch task categories
        getTaskCategories();

        // BottomNavigationView item selection
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.item_1) {
                navigateToHome();
                return true;
            } else if (item.getItemId() == R.id.item_2) {
                navigateToSearchTask();
                return true;
            } else if (item.getItemId() == R.id.item_3) {
                navigateToTaskStatusChart();
                return true;
            } else if (item.getItemId() == R.id.item_4) {
                navigateToAccount();
                return true;
            } else {
                return false;
            }
        });

        // Floating Action Button click listener
        addTask.setOnClickListener(v -> navigateToAddTask());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload task categories when the activity is resumed or comes back from another screen
        getTaskCategories();
    }

    private void getTaskCategories() {
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        TaskCategoryApi api = retrofit.create(TaskCategoryApi.class);

        // Fetch all task categories from the API
        Call<List<TaskCategory>> call = api.getAllCategories();
        call.enqueue(new Callback<List<TaskCategory>>() {
            @Override
            public void onResponse(Call<List<TaskCategory>> call, Response<List<TaskCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskCategory> categories = response.body();

                    // For each category, fetch the task count
                    for (TaskCategory category : categories) {
                        // Fetch task count for the current category
                        fetchTaskCountForCategory(category, api);
                    }

                    // Set adapter for RecyclerView
                    adapter = new CategoryAdapter(Home.this, categories);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Home.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TaskCategory>> call, Throwable t) {
                Toast.makeText(Home.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTaskCountForCategory(TaskCategory category, TaskCategoryApi api) {
        // Fetch the task count for the category
        Call<Integer> taskCountCall = api.getTaskCountForCategory(category.getId());
        taskCountCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int taskCount = response.body();
                    category.setTaskCount(taskCount); // Update the task count
                    adapter.notifyDataSetChanged();  // Notify adapter to refresh the view
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                // Handle error in fetching task count
            }
        });
    }

    // Navigation methods for BottomNavigationView
    private void navigateToHome() {
        // Home is already the current activity, no need to recreate it
        // Can be used to refresh data or actions
        Intent intent = new Intent(Home.this, Home.class);
        startActivity(intent);
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
