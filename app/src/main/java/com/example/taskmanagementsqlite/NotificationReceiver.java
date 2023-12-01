package com.example.taskmanagementsqlite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        // Handle the notification here, e.g., show a notification using NotificationManager
        String taskName = intent.getStringExtra("TASK_NAME");

        // Customize the notification content as needed
        NotificationUtils.showNotification(context, "Task Reminder", "Deadline for task: " + taskName);
    }
}

