package com.example.taskmanagerfrontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerfrontend.R;
import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.TaskApi;
import com.example.taskmanagerfrontend.model.Task;
import com.example.taskmanagerfrontend.UpdateTask;  // Import activity UpdateTask

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    public static int taskId;

    private TaskApi taskApi = ApiClient.getRetrofitInstance().create(TaskApi.class);

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate row_task layout for each task item
        View view = LayoutInflater.from(context).inflate(R.layout.row_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        // Set task data to respective views
        holder.titleTask.setText(task.getTitle());
        holder.descriptionTask.setText(task.getDescription());

        // Set OnClickListener for item click
        holder.itemView.setOnClickListener(v -> {
            // Save task ID as static variable
            taskId = task.getId(); // Assuming Task has an 'id' field

            // Show dialog with delete and update options
            showOptionsDialog(task);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void showOptionsDialog(Task task) {
        // Create and show an AlertDialog with "Update" and "Delete" options
        new AlertDialog.Builder(context)
                .setTitle("Choose Action")
                .setItems(new CharSequence[]{"Update", "Delete"}, (dialog, which) -> {
                    if (which == 0) {
                        // Handle update: start UpdateTask activity
                        Intent intent = new Intent(context, UpdateTask.class);
                        context.startActivity(intent);
                    } else if (which == 1) {
                        // Handle delete: call delete API
                        deleteTask(task.getId());
                    }
                })
                .show();
    }

    private void deleteTask(int taskId) {
        taskApi.deleteTask(taskId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Show success message and remove task from the list
                    Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                    // Remove the task from the list and notify adapter
                    taskList.removeIf(task -> task.getId() == taskId);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTask, descriptionTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            titleTask = itemView.findViewById(R.id.title_task);
            descriptionTask = itemView.findViewById(R.id.description_task);
        }
    }
}
