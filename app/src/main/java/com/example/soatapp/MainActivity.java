package com.example.soatapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new BudilnikFragment()).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;


                    if (item.getItemId() == R.id.budilnik) {
                        selectedFragment = new BudilnikFragment();
                    } else if (item.getItemId() == R.id.xalqaroSoat) {
                        selectedFragment = new XalqaroFragment();
                    } else if (item.getItemId() == R.id.sekundamer) {
                        selectedFragment = new SekundamerFragment();
                    } else if (item.getItemId() == R.id.taymer) {
                        selectedFragment = new TaymerFragment();
                    } else if (item.getItemId() == R.id.about) {
                        selectedFragment = new AboutFragment();
                    }


                    if (selectedFragment != null) {


                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }

                    return true;
                }
            };
}