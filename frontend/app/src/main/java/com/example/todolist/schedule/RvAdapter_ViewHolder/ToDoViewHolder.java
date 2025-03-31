package com.example.todolist.schedule.RvAdapter_ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

public class ToDoViewHolder extends RecyclerView.ViewHolder {
    TextView tv_todo_content,tv_todo_importance;
    LinearLayout layout;

    public ToDoViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_todo_content = itemView.findViewById(R.id.tv_todo_content);
        tv_todo_importance = itemView.findViewById(R.id.tv_todo_importance);
        layout = itemView.findViewById(R.id.bar2);
    }
}
