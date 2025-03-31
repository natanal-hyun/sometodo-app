package com.example.todolist.schedule.RvAdapter_ViewHolder;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.auth.network.RetrofitClient;
import com.example.todolist.schedule.Activity.EditActivity;
import com.example.todolist.schedule.Class.ScheduleClass;
import com.example.todolist.schedule.Service.ScheduleService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    ArrayList<ScheduleClass> data;

    String holder_search_category,holder_search_content;

    ScheduleService service;

    public SearchViewAdapter(ArrayList<ScheduleClass> data) {
        this.data = data;

    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_search_basket,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        ScheduleClass schedule = data.get(position);
        holder.tv_date.setText(schedule.getYear()+"년 "+schedule.getMonth()+"월 "+schedule.getDay()+"일 ");
        holder.tv_category.setText(schedule.getCategory());
        holder.tv_content.setText(schedule.getContent());

        //onclick
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder_search_category = holder.tv_category.getText().toString();
                holder_search_content = holder.tv_content.getText().toString();

                //AlertDialog 생성 및 설정
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                View dialogView = inflater.inflate(R.layout.dialog_main,null);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();


                //TextView - In Dialog
                TextView tv_dialog_content = dialogView.findViewById(R.id.tv_d_content);
                TextView tv_dialog_category = dialogView.findViewById(R.id.tv_d_category);
                tv_dialog_content.setText(holder_search_content);
                tv_dialog_category.setText(holder_search_category);


                //Button - In Dialog
                Button Btn_dialog_edit = dialogView.findViewById(R.id.edit_button);
                Button Btn_dialog_delete = dialogView.findViewById(R.id.delete_button);
                Button Btn_dialog_cancel = dialogView.findViewById(R.id.cancel_button);


                //onclick - EDIT
                Btn_dialog_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), EditActivity.class);

                        intent.putExtra("schedule",schedule);

                        view.getContext().startActivity(intent);
                        dialog.dismiss();

                    }
                });
                //onclick - DELETE
                Btn_dialog_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Retrofit, Service
//                        Retrofit retrofit = new Retrofit.Builder()
//                                .baseUrl("http://10.0.2.2:8080")
//                                .addConverterFactory(GsonConverterFactory.create())
//                                .build();
                        service = RetrofitClient.getClient().create(ScheduleService.class);

                        //Call
                        Call<Integer> call = service.deleteSchedule(schedule.getId());
                        call.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(view.getContext(), "성공", Toast.LENGTH_SHORT).show();
                                    data.remove(holder.getAdapterPosition());
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(view.getContext(), "실패", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Log.v("onFailure",t.getMessage());
                            }
                        });


                    }
                });
                //onclick - cancel
                Btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
