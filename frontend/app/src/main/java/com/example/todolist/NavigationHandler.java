package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.example.todolist.activity.GroupActivity;
import com.example.todolist.finance.FinanceActivity;
import com.example.todolist.schedule.Activity.ScheduleActivity;
import com.example.todolist.settings.activity.SettingsActivity;

public class NavigationHandler {
    public static boolean handleNavigation(Context context, MenuItem item) {
        Intent intent = null;
        int itemId = item.getItemId(); // ID를 가져옴

        if (itemId == R.id.navigation_calendar) {
            if (!(context instanceof ScheduleActivity)) {
                intent = new Intent(context, ScheduleActivity.class);
            }
        } else if (itemId == R.id.navigation_wallet) {
            if (!(context instanceof FinanceActivity)) {
                intent = new Intent(context, FinanceActivity.class);
            }
        } else if (itemId == R.id.navigation_group) {
            if (!(context instanceof GroupActivity)) {
                intent = new Intent(context, GroupActivity.class);
            }
        } else if (itemId == R.id.navigation_settings) {
            if (!(context instanceof SettingsActivity)) {
                intent = new Intent(context, SettingsActivity.class);
            }
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 기존 액티비티 유지
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0); // ✅ 애니메이션 제거

            return true;
        }
        return false;
    }
}
