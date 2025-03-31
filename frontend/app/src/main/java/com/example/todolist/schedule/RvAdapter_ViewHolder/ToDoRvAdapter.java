package com.example.todolist.schedule.RvAdapter_ViewHolder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.auth.network.RetrofitClient;
import com.example.todolist.schedule.Class.ToDoClass;
import com.example.todolist.schedule.Service.ToDoService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoRvAdapter extends RecyclerView.Adapter<ToDoViewHolder> {
    ArrayList<ToDoClass> data;

    //if - No ToDoData
    TextView tv_noToDo;
    ImageView sad_image;

    public ToDoRvAdapter(ArrayList<ToDoClass> data, TextView tv_noToDo, ImageView sad_image) {
        this.data = data;
        this.tv_noToDo = tv_noToDo;
        this.sad_image = sad_image;
    }

    public void UpdateData(ArrayList<ToDoClass> data){
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_todo_basket,parent,false);
        return new ToDoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        holder.tv_todo_content.setText(data.get(position).getTodo_content());
        holder.tv_todo_importance.setText(data.get(position).getImportance()+"");

        //onclick - holder
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long unFinish_ToDo_id = data.get(holder.getAdapterPosition()).getTodo_id();

                //Retrofit, Service
//                Retrofit retrofit_t = new Retrofit.Builder()
//                        .baseUrl("http://10.0.2.2:8080")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
                ToDoService service_t = RetrofitClient.getClient().create(ToDoService.class);

                //Call - Edit
                Call<Integer> call = service_t.editToDoData(unFinish_ToDo_id);
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.isSuccessful()){
                            data.remove(holder.getLayoutPosition());
                            UpdateData(data);
                            Toast.makeText(view.getContext(), "달성", Toast.LENGTH_SHORT).show();
                            if (data.isEmpty()){
                                tv_noToDo.setVisibility(View.VISIBLE);
                                sad_image.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.v("onFailure_ToDoAdapter",t.getMessage());

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
