package com.example.taskmanagerfrontend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskmanagerfrontend.adapter.TaskAdapter;
import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.TaskApi;
import com.example.taskmanagerfrontend.model.Task;
import com.example.taskmanagerfrontend.model.TaskStatus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTask extends AppCompatActivity {
    private TextView txtStartDateTime, txtEndDateTime, txtTitle, txtDescription;
    private Spinner spinnerStatus;
    private Button btnUpdateTask;
    private TaskApi taskApi = ApiClient.getRetrofitInstance().create(TaskApi.class);
    private final SimpleDateFormat uiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private Task currentTask; // Giữ thông tin task đang chỉnh sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_task);

        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnUpdateTask = findViewById(R.id.btnUpdateTask);
        txtStartDateTime = findViewById(R.id.txtStartDateTime);
        txtEndDateTime = findViewById(R.id.txtEndDateTime);

        // Set padding cho view nếu cần thiết
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tải task từ API
        loadTaskDetails();

        // Hiển thị bộ chọn ngày
        txtStartDateTime.setOnClickListener(v -> showDatePicker(txtStartDateTime));
        txtEndDateTime.setOnClickListener(v -> showDatePicker(txtEndDateTime));

        // Cập nhật task
        btnUpdateTask.setOnClickListener(v -> updateTask());
    }

    // Hàm tải chi tiết task từ API
    private void loadTaskDetails() {
        int taskId = TaskAdapter.taskId; // Lấy ID task từ TaskAdapter

        taskApi.getTaskById(taskId).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentTask = response.body();
                    // Cập nhật giao diện với dữ liệu task
                    txtTitle.setText(currentTask.getTitle());
                    txtDescription.setText(currentTask.getDescription());
                    txtStartDateTime.setText(currentTask.getStartDatetime());
                    txtEndDateTime.setText(currentTask.getEndDatetime());

                    // Set trạng thái vào spinner
                    ArrayAdapter<TaskStatus> statusAdapter = new ArrayAdapter<>(UpdateTask.this, android.R.layout.simple_spinner_item, TaskStatus.values());
                    statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatus.setAdapter(statusAdapter);

                    // Set trạng thái hiện tại vào spinner
                    spinnerStatus.setSelection(currentTask.getStatus().ordinal());
                } else {
                    Toast.makeText(UpdateTask.this, "Không thể tải task", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(UpdateTask.this, "Lỗi khi tải task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm cập nhật task
    private void updateTask() {
        String title = txtTitle.getText().toString().trim();
        String description = txtDescription.getText().toString().trim();
        String startDateStr = txtStartDateTime.getText().toString();
        String endDateStr = txtEndDateTime.getText().toString();

        // Kiểm tra các trường thông tin
        if (title.isEmpty() || description.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin task
        currentTask.setTitle(title);
        currentTask.setDescription(description);
        currentTask.setStartDatetime(startDateStr);
        currentTask.setEndDatetime(endDateStr);
        currentTask.setStatus((TaskStatus) spinnerStatus.getSelectedItem());

        // Gọi API cập nhật task
        taskApi.updateTask(currentTask.getId(), currentTask).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateTask.this, "Cập nhật task thành công", Toast.LENGTH_SHORT).show();

                    // Quay lại ListTask và làm mới dữ liệu
                    Intent intent = new Intent(UpdateTask.this, ListTask.class);
                    startActivity(intent);
                    finish(); // Kết thúc màn hình hiện tại (UpdateTask)
                } else {
                    Toast.makeText(UpdateTask.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(UpdateTask.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hiển thị DatePicker cho trường ngày
    private void showDatePicker(TextView targetTextView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                UpdateTask.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay );
                    targetTextView.setText(date);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}
