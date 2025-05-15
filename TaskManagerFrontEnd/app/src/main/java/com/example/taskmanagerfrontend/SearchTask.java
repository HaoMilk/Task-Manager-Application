package com.example.taskmanagerfrontend;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.taskmanagerfrontend.adapter.TaskAdapter;
import com.example.taskmanagerfrontend.model.Task;

public class SearchTask extends AppCompatActivity {

    private RecyclerView recyclerViewSearchTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewSearchTasks = findViewById(R.id.recyclerViewSearchTasks);
        recyclerViewSearchTasks.setLayoutManager(new LinearLayoutManager(this));

        taskList = getSampleTasks();

        taskAdapter = new TaskAdapter(this, taskList);
        recyclerViewSearchTasks.setAdapter(taskAdapter);
    }

    private List<Task> getSampleTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Buy groceries", "Buy milk, eggs and bread"));
        tasks.add(new Task("Finish project", "Complete the task manager app"));
        tasks.add(new Task("Workout", "Go to the gym at 6pm"));
        tasks.add(new Task("Call Mom", "Weekly call"));
        return tasks;
    }
}
