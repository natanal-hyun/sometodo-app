package com.example.todolist.schedule.RvAdapter_ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

public class ToDoFinishViewHolder extends RecyclerView.ViewHolder {
    TextView tv_finish_content,tv_finish_importance;
    LinearLayout layout;

    public ToDoFinishViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_finish_content = itemView.findViewById(R.id.tv_finish_content);
        tv_finish_importance = itemView.findViewById(R.id.tv_finish_importance);
        layout = itemView.findViewById(R.id.bar6);
    }
}
