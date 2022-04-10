package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class IntroActivity extends AppCompatActivity {

    public static ViewPager vp;
    IntroViewpagerAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();

        vp = findViewById(R.id.vp);
        adp = new IntroViewpagerAdapter(this);
        vp.setAdapter(adp);



    }
}