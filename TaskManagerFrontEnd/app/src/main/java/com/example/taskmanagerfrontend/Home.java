package com.example.taskmanagerfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        FloatingActionButton addTask = findViewById(R.id.fabAdd);
        // Thiết lập sự kiện click cho các item trong BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Thay thế switch case bằng if-else
                if (item.getItemId() == R.id.item_1) {
                    if (Home.this instanceof Home) {
                        return false; // Không thực hiện gì nếu đã ở Home
                    } else {
                        // Nếu không phải Home, bắt đầu lại Home
                        Intent intent = new Intent(Home.this, Home.class);
                        startActivity(intent);
                        return true;
                    }
                } else if (item.getItemId() == R.id.item_2) {
                    // Xử lý khi item "Calendar" được chọn
                    Intent intent = new Intent(Home.this, SearchTask.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.item_3) {
                    // Xử lý khi item "Report" được chọn
                    Intent intent = new Intent(Home.this, TaskStatusChart.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.item_4) {
                    // Xử lý khi item "Account" được chọn
                    Intent intent = new Intent(Home.this, Account.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AddTask.class);
                startActivity(intent);
            }
        });
    }
}
