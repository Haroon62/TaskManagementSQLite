package com.example.taskmanagementsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScrean extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String DB_INITIALIZED_KEY = "db_initialized";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screan);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean dbInitialized = prefs.getBoolean(DB_INITIALIZED_KEY, false);
//
//        if (!dbInitialized) {
//            // Initialize the database
//            DBHelper dbHelper = new DBHelper(this);
//           // dbHelper.initializeCategoriesAndPriorities();
//
//            // Set the flag to indicate that initialization has been done
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean(DB_INITIALIZED_KEY, true);
//            editor.apply();
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity
                Intent intent = new Intent(SplashScrean.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}