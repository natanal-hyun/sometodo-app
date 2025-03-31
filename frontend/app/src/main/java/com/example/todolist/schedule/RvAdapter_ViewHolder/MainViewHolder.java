package com.example.todolist.schedule.RvAdapter_ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

public class MainViewHolder extends RecyclerView.ViewHolder {
    TextView tv_content,tv_category;
    LinearLayout layout;

    public MainViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_category = itemView.findViewById(R.id.tv_category);
        tv_content = itemView.findViewById(R.id.tv_content);
        layout = itemView.findViewById(R.id.bar3);
    }
}
