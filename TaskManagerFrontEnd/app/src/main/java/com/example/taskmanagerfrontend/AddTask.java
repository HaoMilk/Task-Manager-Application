package com.example.taskmanagerfrontend;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.AuthApi;
import com.example.taskmanagerfrontend.api.TaskApi;
import com.example.taskmanagerfrontend.api.TaskCategoryApi;
import com.example.taskmanagerfrontend.model.Task;
import com.example.taskmanagerfrontend.model.TaskCategory;
import com.example.taskmanagerfrontend.model.TaskStatus;
import com.example.taskmanagerfrontend.model.User;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTask extends AppCompatActivity {
    private TextView txtStartDateTime, txtEndDateTime;
    private Spinner spinnerCategory;
    private Button btnAddTask;
    private EditText txtTitle, txtDescription;
    private Date selectedStartDate;
    private Date selectedEndDate;

    Long userId = Long.parseLong(Login.UserID);
    private AuthApi authApi = ApiClient.getRetrofitInstance().create(AuthApi.class);
    TaskApi taskApi = ApiClient.getRetrofitInstance().create(TaskApi.class);
    private final SimpleDateFormat uiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private TaskCategoryApi taskCategoryApi = ApiClient.getRetrofitInstance().create(TaskCategoryApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);

        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAddTask = findViewById(R.id.btnAddTask);
        txtStartDateTime = findViewById(R.id.txtStartDateTime);
        txtEndDateTime = findViewById(R.id.txtEndDateTime);

        // Gắn sự kiện click
        txtStartDateTime.setOnClickListener(v -> showDatePicker(txtStartDateTime));
        txtEndDateTime.setOnClickListener(v -> showDatePicker(txtEndDateTime));

        // Set padding cho view nếu cần thiết
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load categories từ API
        loadCategories();

        btnAddTask.setOnClickListener(v -> {
            String title = txtTitle.getText().toString().trim();
            String description = txtDescription.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy ngày và chuyển đổi thành String
            String startDateStr = txtStartDateTime.getText().toString();
            String endDateStr = txtEndDateTime.getText().toString();

            TaskCategory selectedCategory = (TaskCategory) spinnerCategory.getSelectedItem();
            User user = new User();
            user.setId(userId);

            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setStatus(TaskStatus.PENDING);
            task.setUser(user);
            task.setCategory(selectedCategory); // Sử dụng category đã chọn từ Spinner

            try {
                // Chuyển đổi ngày từ String sang Date, rồi lại thành String
                Date startDate = uiFormat.parse(startDateStr);
                Date endDate = uiFormat.parse(endDateStr);

                // Chuyển lại thành String để lưu vào Task
                task.setStartDatetime(uiFormat.format(startDate));
                task.setEndDatetime(uiFormat.format(endDate));

                Log.d("AddTask", "Task: " + task.getTitle() + ", Start: " + task.getStartDatetime());
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(AddTask.this, "Định dạng ngày không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi API
            taskApi.createTask(task).enqueue(new Callback<Task>() {
                @Override
                public void onResponse(Call<Task> call, Response<Task> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(AddTask.this, "Tạo task thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        try {
                            String error = response.errorBody() != null ? response.errorBody().string() : "Không có nội dung lỗi";
                            Log.e("AddTask", "Lỗi từ server: " + error);

                            // Nếu backend trả về JSON có trường "message"
                            String message = "Tạo thất bại";
                            try {
                                JSONObject json = new JSONObject(error);
                                message = json.optString("message", message);
                            } catch (Exception e) {
                                Log.e("AddTask", "Không phải JSON hoặc không có trường message", e);
                            }

                            Toast.makeText(AddTask.this, "Lỗi: " + message, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("AddTask", "Lỗi khi đọc errorBody", e);
                            Toast.makeText(AddTask.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Task> call, Throwable t) {
                    Toast.makeText(AddTask.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("AddTask", "Lỗi mạng", t);
                }
            });
        });
    }

    // Hàm tải danh sách categories từ API
    private void loadCategories() {
        taskCategoryApi.getAllCategories().enqueue(new Callback<List<TaskCategory>>() {
            @Override
            public void onResponse(Call<List<TaskCategory>> call, Response<List<TaskCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskCategory> categories = response.body();

                    // Tạo ArrayAdapter custom để hiển thị tên của category
                    ArrayAdapter<TaskCategory> adapter = new ArrayAdapter<TaskCategory>(AddTask.this,
                            android.R.layout.simple_spinner_item, categories) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            TextView view = (TextView) super.getView(position, convertView, parent);
                            view.setText(categories.get(position).getName()); // Hiển thị tên category
                            return view;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                            view.setText(categories.get(position).getName()); // Hiển thị tên category trong danh sách
                            return view;
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                } else {
                    Toast.makeText(AddTask.this, "Không thể tải danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TaskCategory>> call, Throwable t) {
                Toast.makeText(AddTask.this, "Lỗi khi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker(TextView targetTextView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddTask.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay );
                    targetTextView.setText(date);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}
