package com.example.fitnessproject.ui.sleep;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null && manager.getNotificationChannel("sleep_goal_channel") == null) {
                NotificationChannel channel = new NotificationChannel(
                        "sleep_goal_channel",
                        "Sleep Goal Notifications",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Notification channel for sleep reminders");

                manager.createNotificationChannel(channel);
                Log.d("MyApplication", "Notification channel created successfully.");
            } else {
                Log.d("MyApplication", "Notification channel already exists.");
            }
        }
    }
}
