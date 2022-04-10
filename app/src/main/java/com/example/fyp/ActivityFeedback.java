package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ActivityFeedback extends AppCompatActivity {

    EditText edmsg;
    String msg;
    String server_url="http://192.168.100.188/happy_fit/Feedback.php";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().hide();

        edmsg = (EditText) findViewById(R.id.edmsg);
    }
    public void giveFdbk(View v)
    {
        msg = edmsg.getText().toString();

        if(msg.isEmpty())
        {
            edmsg.setError("Please fill this Field");

            CommonFunctions.AD(ActivityFeedback.this,"Empty Fields","Please Fill All The Fields");
        }
        else
        {
            intoDB();
        }
    }
    private void intoDB()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.trim();

                Log.d("resp", "onResponse: "+resp);
                Log.d("compare", "onResponse: "+"t".equals(resp));
                if ("t".equals(resp))
                {
                    Intent reti = new Intent(ActivityFeedback.this,ActivityNavigationPane.class);
                    setResult(Activity.RESULT_OK,reti);
                    finish();
                    startActivity(reti);
                } else {
                    CommonFunctions.AD(ActivityFeedback.this, "", "Error Occured! Couldn't send Feedback");
                }
            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(ActivityFeedback.this,"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("un", CommonFunctions.getUserName(ActivityFeedback.this));
                Params.put("msg",msg);

                return Params;

            }
        };

        DBConnection.getInstance(ActivityFeedback.this).addTorequestque(stringRequest);
    }
}