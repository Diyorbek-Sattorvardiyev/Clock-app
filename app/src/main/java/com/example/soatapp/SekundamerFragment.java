package com.example.soatapp;



import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SekundamerFragment extends Fragment {

    private Button startButton, lapButton, clearButton;
    private TextView timerText;
    private ListView lapListView;
    private Handler handler;
    private Timer timer;
    private long startTime, timeInMillis, lapTime;
    private boolean isRunning;
    private ArrayList<String> lapTimes;
    private LapAdapter lapAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sekundamer, container, false);

        // Initialize views
        startButton = view.findViewById(R.id.startButton);
        lapButton = view.findViewById(R.id.lapButton);
        clearButton = view.findViewById(R.id.clearButton);
        timerText = view.findViewById(R.id.timerText);
        lapListView = view.findViewById(R.id.lapListView);

        // Initialize variables
        handler = new Handler();
        lapTimes = new ArrayList<>();
        lapAdapter = new LapAdapter(getActivity(), lapTimes);
        lapListView.setAdapter(lapAdapter);

        // Initialize Timer
        timer = new Timer();

        startButton.setOnClickListener(v -> toggleTimer());
        lapButton.setOnClickListener(v -> recordLap());
        clearButton.setOnClickListener(v -> clearData());

        return view;
    }

    private void toggleTimer() {
        if (isRunning) {
            // Stop the timer
            timer.cancel();
            isRunning = false;
            startButton.setText("Start");
        } else {
            // Start the timer
            startTime = System.currentTimeMillis() - timeInMillis;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    timeInMillis = System.currentTimeMillis() - startTime;
                    handler.post(() -> updateTimerDisplay());
                }
            }, 0, 10);
            isRunning = true;
            startButton.setText("Stop");
        }
    }

    private void updateTimerDisplay() {
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        long milliseconds = timeInMillis % 1000;

        String timeString = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
        timerText.setText(timeString);
    }

    private void recordLap() {
        if (isRunning) {
            long lapInMillis = timeInMillis - lapTime;
            lapTime = timeInMillis;

            // Format the lap time
            long seconds = lapInMillis / 1000;
            long minutes = seconds / 60;
            seconds = seconds % 60;
            long milliseconds = lapInMillis % 1000;

            String lapTimeString = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
            lapTimes.add("Aylana: " + lapTimeString);

            // Update the ListView
            lapAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "Sekundomerni boshlang", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearData() {
        // Reset all variables and UI elements
        timeInMillis = 0;
        lapTime = 0;
        lapTimes.clear();
        lapAdapter.notifyDataSetChanged();
        timerText.setText("00:00:000");

        if (isRunning) {
            timer.cancel();
            isRunning = false;
            startButton.setText("Start");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRunning) {
            timer.cancel();
        }
    }
}