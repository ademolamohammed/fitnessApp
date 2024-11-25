package com.example.fitnessproject.ui.sleep;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessproject.databinding.FragmentSleepBinding;

import java.util.Calendar;

public class SleepFragment extends Fragment {

    private FragmentSleepBinding binding;
    private TextView sleepTimeTextView;
    private Button setSleepTimeButton;
    private SharedPreferences sharedPreferences;
    private ActivityResultLauncher<String> notificationPermissionLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SleepViewModel sleepViewModel =
                new ViewModelProvider(this).get(SleepViewModel.class);

        binding = FragmentSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sleepTimeTextView = binding.sleepTimeTextView;
        setSleepTimeButton = binding.setSleepTimeButton;

        sharedPreferences = requireContext().getSharedPreferences("SleepGoalPrefs", Context.MODE_PRIVATE);

        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(getContext(), "Notification permission granted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Notification permission is required for sleep reminders.", Toast.LENGTH_LONG).show();
                    }
                }
        );

        String savedTime = sharedPreferences.getString("sleep_goal_time", "Not Set");
        sleepTimeTextView.setText("Sleep Goal Time: " + savedTime);

        setSleepTimeButton.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                        != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    requestNotificationPermission();
                    return;
                }
            }

            showTimePicker();
        });

        return root;
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) -> {
            String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
            sleepTimeTextView.setText("Sleep Goal Time: " + selectedTime);

            sharedPreferences.edit().putString("sleep_goal_time", selectedTime).apply();

            setSleepReminder(selectedHour, selectedMinute);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void setSleepReminder(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Log.d("SleepFragment", "Alarm set for: " + calendar.getTime());

        Intent intent = new Intent(requireContext(), SleepReminderReceiver.class);

        int pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntentFlags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, pendingIntentFlags);

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d("SleepFragment", "Alarm successfully set for: " + calendar.getTime());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestNotificationPermission() {
        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
    }

    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}