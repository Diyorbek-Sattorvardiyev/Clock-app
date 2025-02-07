package com.example.soatapp;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BudilnikFragment extends Fragment {

    private TextView alarmTimeTextView;
    private TextView currentTimeText;
    private TimePicker timePicker;

    public static Integer alarmIdentifier = 1;
    private final Handler handler = new Handler();

    private final Runnable ticker = new Runnable() {
        @Override
        public void run() {
            // Update the current time text view
            Calendar currentTime = Calendar.getInstance();
            updateCurrentTimeTextView(currentTime);

            // Post the ticker to run again after a delay of 1000ms (1 second)
            handler.postDelayed(this, 1000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budilnik, container, false);

        // Initialize Views
        alarmTimeTextView = view.findViewById(R.id.alarmTimeTextView);
        currentTimeText = view.findViewById(R.id.currentTimeText);
        timePicker = view.findViewById(R.id.timePicker);
        Button setAlarmButton = view.findViewById(R.id.setAlarmButton);
        Button cancelAlarmButton = view.findViewById(R.id.cancelAlarmButton);

        // Check exact alarm permissions for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !requireContext().getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
        }

        updateOldAlarmOnReopen();

        setAlarmButton.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
            calendar.set(Calendar.SECOND, 0); // Set seconds to 0

            if (calendar.after(now)) {
                setAlarm(calendar.getTimeInMillis());
                updateAlarmTextView(calendar);
                Log.d("BudilnikFragment", "Alarm set for: " + formatTime(calendar));
            } else {
                Toast.makeText(requireContext(), "O'tgan vaqt uchun signalni o'rnatib bo'lmaydi.", Toast.LENGTH_SHORT).show();
                Log.d("BudilnikFragment", "Attempted to set an alarm for the past. Operation cancelled.");
            }
        });

        cancelAlarmButton.setOnClickListener(v -> cancelAlarm());

        return view;
    }

    private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(getContext().ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmIdentifier, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        // Save alarm set time to SharedPreferences
        requireContext().getSharedPreferences(requireContext().getPackageName() + "AlarmOpenSourceApp", getContext().MODE_PRIVATE)
                .edit()
                .putLong("alarmTime" + alarmIdentifier, timeInMillis)
                .apply();
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmIdentifier, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(getContext().ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        // Clear saved alarm time from SharedPreferences
        requireContext().getSharedPreferences(requireContext().getPackageName() + "AlarmOpenSourceApp", getContext().MODE_PRIVATE)
                .edit()
                .remove("alarmTime" + alarmIdentifier)
                .apply();

        alarmTimeTextView.setText("Signal o‘rnatilmagan");
    }

    private void updateOldAlarmOnReopen() {
        // Check if there is a saved alarm time and display it
        long savedAlarmTime = requireContext().getSharedPreferences(requireContext().getPackageName() + "AlarmOpenSourceApp", getContext().MODE_PRIVATE).getLong("alarmTime" + alarmIdentifier, 0);

        if (savedAlarmTime != 0) {
            Calendar alarmTime = Calendar.getInstance();
            alarmTime.setTimeInMillis(savedAlarmTime);
            updateAlarmTextView(alarmTime);
        }
    }

    private void updateCurrentTimeTextView(Calendar calendar) {
        String alarmText = "Joriy vaqt: " + formatTime(calendar);
        currentTimeText.setText(alarmText);
    }

    private void updateAlarmTextView(Calendar calendar) {
        String alarmText = "Signal o'rnatilgan: " + formatTime(calendar);
        alarmTimeTextView.setText(alarmText);
    }

    static String formatTime(Calendar calendar) {
        String timeFormat = "hh:mm:ss a"; // Example "03:00 PM"
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return sdf.format(calendar.getTime());
    }

    @Override
    public void onResume() {
        super.onResume();
        ticker.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(ticker);
    }
}
