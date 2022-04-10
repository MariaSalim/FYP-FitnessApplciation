package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ActivityNavigationPane extends AppCompatActivity {

    BottomNavigationView nav_bar;

    HomeFragment hf;
    DashboardFragment df;
    ProfileFragment pf;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_pane);

        getSupportActionBar().hide();

        hf = new HomeFragment();
        df = new DashboardFragment();
        pf = new ProfileFragment();

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.navfrag, hf);
        fragmentTransaction.commit();

        nav_bar = (BottomNavigationView) findViewById(R.id.navi);

        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        fragmentManager = getSupportFragmentManager();

                        fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.navfrag, hf);

                        fragmentTransaction.addToBackStack(hf.toString());
                        fragmentTransaction.commit();

                        return true;
                    }
                    case R.id.navigation_dashboard:
                    {
                        fragmentManager = getSupportFragmentManager();

                        fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.navfrag, df);

                        fragmentTransaction.addToBackStack(df.toString());
                        fragmentTransaction.commit();

                        return true;
                    }
                    case R.id.navigation_profile:
                    {
                        fragmentManager = getSupportFragmentManager();

                        fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.navfrag, pf);

                        fragmentTransaction.addToBackStack(pf.toString());
                        fragmentTransaction.commit();
                    }
                    default: {
                        return false;
                    }
                }
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
    }
    //Return from Contact us
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(ActivityNavigationPane.this,"Email Sent", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ActivityNavigationPane.this,"Error Sending Email", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(ActivityNavigationPane.this,"Feedback Sent", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ActivityNavigationPane.this,"Error Sending Feedback", Toast.LENGTH_LONG).show();
            }
        }
    }

}