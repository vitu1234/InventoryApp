package com.example.inventoryapp.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryapp.R;
import com.example.inventoryapp.activities.AccountDirectActivity;
import com.example.inventoryapp.activities.DashboardActivity;
import com.example.inventoryapp.storage.SharedPrefManager;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView imageViewLogo;
    private Animation slideAnimation;
    private static int SLIDE_TIMER = 4000;

    SharedPreferences sharedPreferencesOnboardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageViewLogo = findViewById(R.id.SplashScreenImage);
        slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide);
        imageViewLogo.startAnimation(slideAnimation);


//        //delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferencesOnboardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);

                boolean isFirstTimeUser = sharedPreferencesOnboardingScreen.getBoolean("firstTime", true);
                if (isFirstTimeUser) {

                    SharedPreferences.Editor editor = sharedPreferencesOnboardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    //start the next actitivty after 5 seconds
                    startActivity(new Intent(getApplicationContext(), OnBoardingActivity.class));
                    finish();
                } else {
                    SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
                    if (sharedPrefManager.isLoggedIn()) {
                        if (!(sharedPrefManager.getAccountType() == null)) {
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            finish();

                        } else {
                            startActivity(new Intent(getApplicationContext(), AccountDirectActivity.class));
                            finish();
                        }

                    } else {
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                    }

                }


            }
        }, SLIDE_TIMER);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(getApplicationContext(),OnBoardingActivity.class));
//                finish();
//            }
//        },4000);

    }
}