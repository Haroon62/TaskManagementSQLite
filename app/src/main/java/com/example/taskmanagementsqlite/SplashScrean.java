package com.example.taskmanagementsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScrean extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String DB_INITIALIZED_KEY = "db_initialized";
    LottieAnimationView animationView;
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
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // Hide the navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        animationView = findViewById(R.id.lottieAnimationView);
        animationView.playAnimation();
        animationView.setSpeed(1.5f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity
                Intent intent = new Intent(SplashScrean.this, login.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}