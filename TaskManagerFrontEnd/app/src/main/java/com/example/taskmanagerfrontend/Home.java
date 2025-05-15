package com.example.taskmanagerfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import com.example.taskmanagerfrontend.adapter.CategoryAdapter;
import com.example.taskmanagerfrontend.model.TaskCategory;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;  // Chỉ khai báo ở đây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Gán recyclerView sau khi setContentView
        recyclerView = findViewById(R.id.recyclerViewCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo và gán adapter cho recyclerView
        List<TaskCategory> categories = getCategories();
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        recyclerView.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        FloatingActionButton addTask = findViewById(R.id.fabAdd);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.item_1) {
                    if (Home.this instanceof Home) {
                        return false;
                    } else {
                        Intent intent = new Intent(Home.this, Home.class);
                        startActivity(intent);
                        return true;
                    }
                } else if (item.getItemId() == R.id.item_2) {
                    Intent intent = new Intent(Home.this, SearchTask.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.item_3) {
                    Intent intent = new Intent(Home.this, TaskStatusChart.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.item_4) {
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

    private List<TaskCategory> getCategories() {
        List<TaskCategory> list = new ArrayList<>();
        list.add(new TaskCategory(1, "Work", 10, "https://example.com/work_icon.png"));
        list.add(new TaskCategory(2, "Personal", 5,"https://example.com/personal_icon.png"));
        list.add(new TaskCategory(3, "Shopping", 3,""));
        return list;
    }
}
