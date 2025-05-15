package com.example.taskmanagerfrontend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.TaskApi;
import com.example.taskmanagerfrontend.model.Task;
import com.example.taskmanagerfrontend.model.TaskCategory;
import com.example.taskmanagerfrontend.model.TaskStatus;
import com.example.taskmanagerfrontend.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTask extends AppCompatActivity {

    private EditText txtTitle, txtDescription;
    private TextView txtStartDateTime, txtEndDateTime;
    private Spinner spinnerCategory;
    private Button btnSaveTask;
    private User user;
    private TaskApi taskApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Initialize views
        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtStartDateTime = findViewById(R.id.txtStartDateTime);
        txtEndDateTime = findViewById(R.id.txtEndDateTime);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveTask = findViewById(R.id.btnAddTask);

        // Initialize API client
        taskApi = ApiClient.getRetrofitInstance().create(TaskApi.class);

        btnSaveTask.setOnClickListener(v -> {
            String title = txtTitle.getText().toString();
            String description = txtDescription.getText().toString();

            // Get the text from the TextView
            String startDateTimeString = txtStartDateTime.getText().toString();
            String endDateTimeString = txtEndDateTime.getText().toString();

            // Parse the strings into Date objects
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // Adjust the format according to your input
            Date startDateTime = null;
            Date endDateTime = null;
            try {
                startDateTime = sdf.parse(startDateTimeString);
                endDateTime = sdf.parse(endDateTimeString);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(AddTask.this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get category from spinner
            TaskCategory category = (TaskCategory) spinnerCategory.getSelectedItem();

            // Create Task object
            Task newTask = new Task(title, description, startDateTime, endDateTime, TaskStatus.PENDING, user, category);

            // Make API call to create the task
            taskApi.createTask(newTask).enqueue(new Callback<Task>() {
                @Override
                public void onResponse(Call<Task> call, Response<Task> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddTask.this, "Task created successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the current activity
                    } else {
                        Toast.makeText(AddTask.this, "Failed to create task", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Task> call, Throwable t) {
                    Toast.makeText(AddTask.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
