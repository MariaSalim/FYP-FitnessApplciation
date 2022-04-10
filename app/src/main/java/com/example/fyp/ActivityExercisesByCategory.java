package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityExercisesByCategory extends AppCompatActivity {

    String serverurl ="http://192.168.100.188/happy_fit/getExercisesByCategory.php";
    String serverurl1 ="http://192.168.100.188/happy_fit/getVideoURL.php";
    ArrayList<String> source;
    ListView l;
    ArrayAdapter<String> adp;
    String CateName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_by_category);
        getSupportActionBar().hide();

        source=new ArrayList<String>();

        l= findViewById(R.id.lvexbcate);

        Intent in = getIntent();
        CateName = in.getStringExtra("cateN");

        getExercisesByCategory(CateName);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                getVideoID(String.valueOf(l.getItemAtPosition(position)));
            }
        });


    }
    private void getExercisesByCategory(String wc)
    {
        Log.d("Function Check", "getExercises: ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                String resp = response.trim();
                Log.d("Function Check", "getExercises: "+resp);
                String temp[]=resp.split(",");

                for(int i=0;i<temp.length;i++)
                {
                    source.add(temp[i]);
                    Log.d("Function Check", "getExercises: "+source.get(i));

                    CommonFunctions.AD(ActivityExercisesByCategory.this,"","Choose an Exercise to Continue");

                    adapterTolv();
                }



            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(ActivityExercisesByCategory.this,"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("wc",wc);

                return Params;

            }
        };
        DBConnection.getInstance(ActivityExercisesByCategory.this).addTorequestque(stringRequest);
    }
    private void adapterTolv()
    {
        adp = new ArrayAdapter<String>(ActivityExercisesByCategory.this,android.R.layout.simple_list_item_1,source){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(getResources().getColor(R.color.textclr));
                tv.setBackgroundColor(getResources().getColor(R.color.introbg));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tv.setTypeface(getResources().getFont(R.font.raleway_medium));
                }
                tv.setTextSize(18);

                // Generate ListView Item using TextView
                return view;
            }
        };

        l.setAdapter(adp);
    }
    private void getVideoID(String wk)
    {
        Log.d("Function Check", "getExercises: ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                String resp = response.trim();
                Log.d("Function Check", "getExercises: "+resp);

                Intent intent = new Intent(ActivityExercisesByCategory.this, ActivityVideoInYoutube.class);
                intent.putExtra("vid",resp);
                startActivity(intent);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(ActivityExercisesByCategory.this,"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("wk",wk);

                return Params;

            }
        };
        DBConnection.getInstance(ActivityExercisesByCategory.this).addTorequestque(stringRequest);
    }
}