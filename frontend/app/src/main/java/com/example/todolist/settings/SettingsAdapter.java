package com.example.todolist.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.auth.activity.LoginActivity;
import com.example.todolist.auth.network.RetrofitClient;
import com.example.todolist.auth.token.TokenManager;
import com.example.todolist.settings.activity.NotificationActivity;
import com.example.todolist.settings.activity.PasswordChangeActivity;
import com.example.todolist.settings.activity.ProfileEditActivity;
import com.example.todolist.settings.network.SettingsApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    private final List<SettingsItem> settingsList;
    private final Context context;

    public SettingsAdapter(List<SettingsItem> settingsList, Context context) {
        this.settingsList = settingsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingsItem item = settingsList.get(position);
        holder.icon.setImageResource(item.getIconResId());
        holder.title.setText(item.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = null;
            switch (item.getTitle()) {
                case "프로필 수정":
                    intent = new Intent(context, ProfileEditActivity.class);
                    break;
                case "비밀번호 변경":
                    intent = new Intent(context, PasswordChangeActivity.class);
                    break;
                case "푸시 알림 설정":
                    intent = new Intent(context, NotificationActivity.class);
                    break;
                case "로그아웃":
                    logout();
                    break;
            }
            if (intent != null) context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.settingsIcon);
            title = itemView.findViewById(R.id.settingsTitle);
        }
    }

    private void logout() {
        // API 요청 실행
        SettingsApiService apiService = RetrofitClient.getClient().create(SettingsApiService.class);
        Call<Void> call = apiService.logout();

        call.enqueue(new Callback<Void>() {
            // 요청이 성공한 경우
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 로그아웃 처리됨
                    // 클라이언트에서 토큰 삭제 처리
                    TokenManager.clearTokens();

                    // 로그아웃 후 UI 변경 (예: 로그인 화면으로 리디렉션)
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 백스택 제거
                    context.startActivity(intent);
                    ((Activity) context).finish();

                    Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "로그아웃 실패", Toast.LENGTH_SHORT).show();
                }
            }

            // 요청 실패한 경우
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
}