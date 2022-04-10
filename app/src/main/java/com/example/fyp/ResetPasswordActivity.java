package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ResetPasswordActivity extends AppCompatActivity {

    String newPwd="",code="",pin="", usern="",ph="";
    PendingIntent pi;
    SmsManager sms;
    Button btnnxt, btnver;
    EditText ednp, edcode, edusern;
    String server_url2 = "http://192.168.100.188/happy_fit/getPhNo.php";

    private static final int PERMISSIONS_REQUEST_SEND_SMS = 1;

    String server_url3 = "http://192.168.100.188/happy_fit/updatePwd.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getSupportActionBar().hide();

        Intent intent=new Intent(this,ResetPasswordActivity.class);

        pi=PendingIntent.getActivity(this, 0, intent,0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        sms=SmsManager.getDefault();

        Random random = new Random();
        String randomPIN = String.format("%04d", random.nextInt(10000));
        pin = String.valueOf(randomPIN);


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_SEND_SMS);
        }
       
        ednp = (EditText) findViewById(R.id.ednewPwd);
        edcode = (EditText) findViewById(R.id.edcode);
        edusern = (EditText) findViewById(R.id.edusern);
        btnnxt =(Button) findViewById(R.id.btnnext);
        btnver =(Button) findViewById(R.id.btnver);

    }
    //-------------------- For Resetting Password
    public void updPassword(View v)
    {
        code = edcode.getText().toString();
        newPwd = ednp.getText().toString();

        if(code.isEmpty() || newPwd.isEmpty())
        {
            if(code.isEmpty()) {
                edcode.setError("Please fill this Field");
            }
            if(newPwd.isEmpty())
            {
                ednp.setError("Please fill this Field");
            }
            CommonFunctions.AD(ResetPasswordActivity.this,"Empty Fields","Please Fill All The Fields");
        }
        else if((code.length()<4) ||newPwd.length()<6)
        {
            if(code.length()<4)
            {
                edcode.setError("Code is of 4 Digits");
            }
            if(newPwd.length()<6)
            {
                ednp.setError("Password should be of atleast 6 Characters");
            }
        }
        else {
            if (code.equals(pin)) {
                Log.d("----", "" + code + " " + pin + " " + usern);
                intoDB();
            } else {
                CommonFunctions.AD(ResetPasswordActivity.this, "Incorrect Pin", "");
            }
        }
    }
    private void intoDB()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.trim();

                Log.d("resp", "onResponse: "+resp);
                Log.d("compare", "onResponse: "+"t".equals(resp));
                if ("t".equals(resp))
                {
                    Intent reti = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                    setResult(Activity.RESULT_OK,reti);
                    Toast.makeText(ResetPasswordActivity.this,"Password Updated", Toast.LENGTH_LONG).show();
                    startActivity(reti);
                    finish();
                } else
                {
                    CommonFunctions.AD(ResetPasswordActivity.this, "", "Error Occured! while resetting your password");
                }
            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(ResetPasswordActivity.this,"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("un", usern);
                Params.put("np",newPwd);

                return Params;

            }
        };

        DBConnection.getInstance(ResetPasswordActivity.this).addTorequestque(stringRequest);
    }
    public void getphNo(View v)
    {
        ednp.setVisibility(View.VISIBLE);
        edcode.setVisibility(View.VISIBLE);
        edusern.setVisibility(View.GONE);
        btnnxt.setVisibility(View.GONE);
        btnver.setVisibility(View.VISIBLE);

        usern = edusern.getText().toString();

        if(!usern.isEmpty()) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String resp = response.trim();

                    Log.d("resp", "onResponse: " + resp);
                    Log.d("compare", "onResponse: " + "t".equals(resp));
                    if (resp != null) {
                        sms.sendTextMessage(resp, null, "Your Verification Code is " + pin, pi, null);

                        CommonFunctions.AD(ResetPasswordActivity.this,"Verification Code","Your Verification code has been sent to "+resp);
                        ph = resp;
                    } else {
                        CommonFunctions.AD(ResetPasswordActivity.this, "Error Occured!", "");
                    }

                }
            }

                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CommonFunctions.AD(ResetPasswordActivity.this, "Something Went wrong", "");
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> Params = new HashMap<String, String>();

                    Params.put("un", usern);
                    return Params;

                }
            };

            DBConnection.getInstance(ResetPasswordActivity.this).addTorequestque(stringRequest);
        }
        else
        {
            CommonFunctions.AD(ResetPasswordActivity.this,"Empty Field","You cannot Leave the Fields Empty");
            edusern.setError("Please Fill this Field");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ednp.setVisibility(View.GONE);
        edcode.setVisibility(View.GONE);
        edusern.setVisibility(View.VISIBLE);
        btnnxt.setVisibility(View.VISIBLE);
        btnver.setVisibility(View.GONE);

    }

}