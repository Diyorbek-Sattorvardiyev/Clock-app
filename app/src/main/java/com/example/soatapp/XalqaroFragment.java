package com.example.soatapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class XalqaroFragment extends Fragment {

    private TextView uzbekistanTime, usaTime, ukTime, germanyTime, japanTime, chinaTime, russiaTime, indiaTime, brazilTime, australiaTime;
    private Handler handler = new Handler();
    private Runnable updateTimeRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xalqaro, container, false);

        // Initialize TextViews for displaying time for each country
        uzbekistanTime = view.findViewById(R.id.uzbekistanTime);
        usaTime = view.findViewById(R.id.usaTime);
        ukTime = view.findViewById(R.id.ukTime);
        germanyTime = view.findViewById(R.id.germanyTime);
        japanTime = view.findViewById(R.id.japanTime);
        chinaTime = view.findViewById(R.id.chinaTime);
        russiaTime = view.findViewById(R.id.russiaTime);
        indiaTime = view.findViewById(R.id.indiaTime);
        brazilTime = view.findViewById(R.id.brazilTime);
        australiaTime = view.findViewById(R.id.australiaTime);

        // Update times for all countries when the fragment is created
        updateTimes();

        // Start updating the times every second
        startUpdatingTime();

        return view;
    }

    private void startUpdatingTime() {
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                // Update the times every second
                updateTimes();
                // Repeat the task after 1 second (1000 milliseconds)
                handler.postDelayed(this, 1000);
            }
        };

        // Start the first run
        handler.post(updateTimeRunnable);
    }

    private void updateTimes() {
        // Define the time zones for each country
        String[] timeZones = {
                "Asia/Tashkent", // Uzbekistan
                "America/New_York", // USA
                "Europe/London", // UK
                "Europe/Berlin", // Germany
                "Asia/Tokyo", // Japan
                "Asia/Shanghai", // China
                "Europe/Moscow", // Russia
                "Asia/Kolkata", // India
                "America/Sao_Paulo", // Brazil
                "Australia/Sydney" // Australia
        };

        // Update the time for each country
        uzbekistanTime.setText(getTimeForCountry(timeZones[0], "O'zbekiston"));
        usaTime.setText(getTimeForCountry(timeZones[1], "USA"));
        ukTime.setText(getTimeForCountry(timeZones[2], "UK"));
        germanyTime.setText(getTimeForCountry(timeZones[3], "Germany"));
        japanTime.setText(getTimeForCountry(timeZones[4], "Japan"));
        chinaTime.setText(getTimeForCountry(timeZones[5], "China"));
        russiaTime.setText(getTimeForCountry(timeZones[6], "Russia"));
        indiaTime.setText(getTimeForCountry(timeZones[7], "India"));
        brazilTime.setText(getTimeForCountry(timeZones[8], "Brazil"));
        australiaTime.setText(getTimeForCountry(timeZones[9], "Australia"));
    }

    // Helper method to get the current time in a specific country
    private String getTimeForCountry(String timeZoneId, String countryName) {
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(timeZone);
        String currentTime = sdf.format(new Date());

        return countryName + ": " + currentTime;
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stop updating time when the fragment is stopped
        if (handler != null && updateTimeRunnable != null) {
            handler.removeCallbacks(updateTimeRunnable);
        }
    }
}
