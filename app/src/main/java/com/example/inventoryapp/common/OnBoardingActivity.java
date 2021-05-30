package com.example.inventoryapp.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.inventoryapp.R;
import com.example.inventoryapp.adapters.onboard.SliderAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    Button buttonGetStarted, btnNxt, btnSkip;

    SliderAdapter sliderAdapter;
    int current_position;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_on_boarding);

        //        ?hooks
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        buttonGetStarted = findViewById(R.id.get_started_btn);
        btnNxt = findViewById(R.id.next_btn);

        //slider fill in
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDots(0);

        viewPager.addOnPageChangeListener(onPageChangeListener);

    }

    //create the textview for the dots on the left side
    @SuppressLint("ResourceAsColor")
    private void addDots(int position) {
        TextView[] dots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);


        }

        if (position == 2) {
            buttonGetStarted.setVisibility(View.VISIBLE);
            btnNxt.setVisibility(View.INVISIBLE);
        } else {
            buttonGetStarted.setVisibility(View.INVISIBLE);
            btnNxt.setVisibility(View.VISIBLE);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.blue_200));
        }


    }

    public void skip(View view) {


        startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
        finish();

    }

    public void next(View view) {
        viewPager.setCurrentItem(current_position + 1);
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            current_position = position;
            addDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}