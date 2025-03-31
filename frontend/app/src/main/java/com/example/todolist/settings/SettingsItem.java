package com.example.todolist.settings;

public class SettingsItem {
    private final String title;
    private final int iconResId;

    public SettingsItem(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() { return title; }
    public int getIconResId() { return iconResId; }
}
