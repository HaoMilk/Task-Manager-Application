package com.example.taskmanagerfrontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerfrontend.R;
import com.bumptech.glide.Glide;
import com.example.taskmanagerfrontend.model.TaskCategory;
import com.example.taskmanagerfrontend.ListTask;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<TaskCategory> categories;

    public static int categoryId ;

    public CategoryAdapter(Context context, List<TaskCategory> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        TaskCategory category = categories.get(position);

        // Set category name
        holder.projectTitle.setText(category.getName());

        // Set task count
        String taskCountText = category.getTaskCount() + " Tasks";
        holder.taskCount.setText(taskCountText);

        // Load image if available
        if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(category.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(holder.projectIcon);
        } else {
            holder.projectIcon.setImageResource(R.drawable.ic_placeholder);  // Fallback image
        }

        // Set click listener for category item
        holder.itemView.setOnClickListener(v -> {
            // Lưu ID của category vào biến static
            categoryId = category.getId();

            // Chuyển sang màn hình ListTask
            Intent intent = new Intent(context, ListTask.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView projectIcon;
        TextView projectTitle, taskCount;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            projectIcon = itemView.findViewById(R.id.project_icon);
            projectTitle = itemView.findViewById(R.id.project_title);
            taskCount = itemView.findViewById(R.id.task_count);
        }
    }
}
