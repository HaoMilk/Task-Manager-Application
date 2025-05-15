package com.example.taskmanagerfrontend;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.example.taskmanagerfrontend.model.GetStatus;
import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.TaskApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskStatusChart extends AppCompatActivity {

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Bật chế độ Edge-to-Edge để sử dụng toàn bộ màn hình
        setContentView(R.layout.activity_task_status_chart);

        pieChart = findViewById(R.id.PieChart); // Kết nối PieChart từ XML

        // Gọi API để lấy danh sách trạng thái task
        TaskApi taskApi = ApiClient.getRetrofitInstance().create(TaskApi.class);
        Call<List<GetStatus>> call = taskApi.getTaskStatuses();

        call.enqueue(new Callback<List<GetStatus>>() {
            @Override
            public void onResponse(Call<List<GetStatus>> call, Response<List<GetStatus>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GetStatus> taskStatuses = response.body();

                    // Tạo danh sách PieEntry từ dữ liệu API
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    for (GetStatus taskStatus : taskStatuses) {
                        entries.add(new PieEntry(taskStatus.getCount(), taskStatus.getStatus()));
                    }

                    // Tạo dữ liệu cho PieChart
                    PieDataSet dataSet = new PieDataSet(entries, "Task Status Distribution");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Màu sắc cho biểu đồ
                    dataSet.setValueTextColor(getResources().getColor(android.R.color.white)); // Màu chữ
                    dataSet.setValueTextSize(12f); // Kích thước chữ

                    // Tạo PieData
                    PieData data = new PieData(dataSet);

                    // Cấu hình PieChart
                    pieChart.setData(data);
                    pieChart.setUsePercentValues(true); // Hiển thị tỷ lệ phần trăm
                    pieChart.getDescription().setEnabled(false); // Tắt mô tả
                    pieChart.animateY(1000); // Hiệu ứng khi vẽ biểu đồ
                } else {
                    Toast.makeText(TaskStatusChart.this, "Failed to load task statuses", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetStatus>> call, Throwable t) {
                Toast.makeText(TaskStatusChart.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Đảm bảo PieChart có padding phù hợp với viền hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
