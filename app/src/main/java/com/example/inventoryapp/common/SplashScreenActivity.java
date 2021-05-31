package com.example.inventoryapp.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryapp.R;
import com.example.inventoryapp.activities.DashboardActivity;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.storage.SharedPrefManager;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView imageViewLogo;
    private Animation slideAnimation;
    private static int SLIDE_TIMER = 4000;

    SharedPreferences sharedPreferencesOnboardingScreen;

    AppDatabase room_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageViewLogo = findViewById(R.id.SplashScreenImage);
        slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide);
        imageViewLogo.startAnimation(slideAnimation);

        room_db = AppDatabase.getDbInstance(this.getApplicationContext());


        //check if user already inserted
        /*if (room_db.userDao().countAllUsers() == 0) {
//            room_db.userDao().deleteAllUsers();

            String password = "12345678";
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setFirstname("Admin");
            user.setLastname("Admin");
            user.setPhoto("NULL");
            user.setStatus("active");
            user.setPassword(hashedPassword);
            room_db.userDao().insertUser(user);
        }


        Log.e("user_count", "Count: " + room_db.userDao().countAllUsers() + " name: " + room_db.userDao().findByUserEmail("admin@gmail.com").getFirstname());

*/
        // $2a$12$US00g/uMhoSBm.HiuieBjeMtoN69SN.GE25fCpldebzkryUyopws6
//        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
        // result.verified == true

//        if (result.verified){
//            Log.e("e","Verified");
//        }else{
//            Log.e("e","not verified");
//        }

//        //delay
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
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
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                    } else {
                        if (room_db.userDao().countAllUsers() > 0) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
                            finish();
                        }
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