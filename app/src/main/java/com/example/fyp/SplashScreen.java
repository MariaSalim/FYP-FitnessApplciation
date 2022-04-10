package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity
{
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private FirebaseAuth mAuth;

    Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run()
            {

                mainIntent = new Intent(SplashScreen.this, IntroActivity.class);

                if(CommonFunctions.getIntro(SplashScreen.this).equals("Clicked"))
                {
                    mainIntent = new Intent(SplashScreen.this,LoginActivity.class);
                }
                if(CommonFunctions.getUserName(SplashScreen.this).length()>0)
                {
                    mainIntent = new Intent(SplashScreen.this,ActivityNavigationPane.class);
                }


                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}