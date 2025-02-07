package com.example.soatapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class TaymerFragment extends Fragment {

    private Spinner hourSpinner, minuteSpinner, secondSpinner;
    private Button startButton, pauseButton, cancelButton;
    private TextView timeRemainingText;
    private CountDownTimer countDownTimer;
    private long timeInMillis;
    private long timeLeftInMillis; // To'xtatib qolgan vaqt
    private boolean isRunning = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taymer, container, false);

        // Initialize views
        hourSpinner = view.findViewById(R.id.hourSpinner);
        minuteSpinner = view.findViewById(R.id.minuteSpinner);
        secondSpinner = view.findViewById(R.id.secondSpinner);
        startButton = view.findViewById(R.id.startButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        timeRemainingText = view.findViewById(R.id.timeRemainingText);

        // Initialize buttons visibility
        pauseButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);

        // Set listeners for buttons
        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        cancelButton.setOnClickListener(v -> cancelTimer());

        return view;
    }

    private void startTimer() {
        // Get time from the spinner
        int hours = Integer.parseInt(hourSpinner.getSelectedItem().toString());
        int minutes = Integer.parseInt(minuteSpinner.getSelectedItem().toString());
        int seconds = Integer.parseInt(secondSpinner.getSelectedItem().toString());

        // Convert to milliseconds
        timeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;

        if (timeInMillis > 0) {
            // Start countdown timer
            startCountDown(timeInMillis);
        } else {
            Toast.makeText(getActivity(), "Iltimos, vaqtni tanlang", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCountDown(long startTime) {
        countDownTimer = new CountDownTimer(startTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                timeRemainingText.setText(formatTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                timeRemainingText.setText("Vaqt tugadi!");
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                isRunning = false;
            }
        }.start();

        startButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        isRunning = true;
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isRunning = false;
            startButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.VISIBLE);
        }
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timeRemainingText.setText("00:00:00");
            startButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            isRunning = false;
        }
    }

    private String formatTime(long millis) {
        int hours = (int) (millis / 3600000);
        int minutes = (int) ((millis % 3600000) / 60000);
        int seconds = (int) ((millis % 60000) / 1000);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Start timer from where it was paused
    private void resumeTimer() {
        startCountDown(timeLeftInMillis); // Continue from the remaining time
    }
}
