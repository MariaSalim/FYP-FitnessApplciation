package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.Permission;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    String em,resp;
    ImageView ivgoogle;
    private GoogleSignInClient gsc;
    private static final int SIGNIN_REQUEST_CODE = 1;
    EditText edun, edpwd;
    CircularProgressButton anmbtn;

    String unamePattern = "\\w+", un, pwd,server_url = "http://192.168.100.188/happy_fit/login.php";
    String server_Url1 = "http://192.168.100.188/happy_fit/getEmail.php";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        ivgoogle =(ImageView) findViewById(R.id.googleiv);
        edun = (EditText) findViewById(R.id.edlun);
        edpwd = (EditText) findViewById(R.id.edlpwd);

        anmbtn = (CircularProgressButton) findViewById(R.id.cirLoginButton);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.clientID))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, GSO);

        ivgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intt = gsc.getSignInIntent();
                startActivityForResult(intt, SIGNIN_REQUEST_CODE);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("T0AG", "onActivityResult: ");
        if(requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(LoginActivity.this,"Password Updated", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(LoginActivity.this,"Error Updating Password", Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode == SIGNIN_REQUEST_CODE)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(acc);
            }
            catch(ApiException e)
            {
                e.printStackTrace();
            }

        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acc)
    {
        AuthCredential cred = GoogleAuthProvider.getCredential(acc.getIdToken(),null);
        mAuth.signInWithCredential(cred).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser usr = mAuth.getCurrentUser();
                    GoogleSignInAccount sia = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                    if(sia!=null)
                    {
                        em = sia.getEmail();
                        goolgleUn();
                        edun.setText(resp);
                    }
                }
                else
                {
                    CommonFunctions.AD(LoginActivity.this, "AuthenticationFailed","");
                }

            }
        });
    }
    public void MoveToRegister(View View){
        startActivity(new Intent(this,ActivityRegister.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }
    public void loggedIn(View view)
    {
        un = edun.getText().toString();
        pwd = edpwd.getText().toString();

        if(un.isEmpty() || pwd.isEmpty())
        {
            if(un.isEmpty())
            {
                edun.setError("Please fill this Field");
            }
            if(pwd.isEmpty())
            {
                edpwd.setError("Please fill this Field");
            }
            CommonFunctions.AD(LoginActivity.this,"Empty Fields","Please Fill All The Fields");
        }
        else if(!(un.matches(unamePattern)))
        {
            edun.setError("Enter a Vald UserName");
        }
        else
        {
            anmbtn.startAnimation();
            Authenticate();
        }

    }
    private void Authenticate()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.trim();

                Log.d("resp", "onResponse: "+resp);
                Log.d("compare", "onResponse: "+"t".equals(resp));
                if ("t".equals(resp))
                {
                    anmbtn.revertAnimation();
                    Intent i = new Intent(LoginActivity.this,ActivityNavigationPane.class);
                    CommonFunctions.setUserName(LoginActivity.this,un);
                    startActivity(i);
                } else {
                    anmbtn.revertAnimation();
                    CommonFunctions.AD(LoginActivity.this, "", "Incorrect UserName or Password or Your Account Approval Request is Pending");
                }
            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(LoginActivity.this,"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("un", un);
                Params.put("pwd",pwd);

                return Params;

            }
        };

        DBConnection.getInstance(LoginActivity.this).addTorequestque(stringRequest);
    }
    private void goolgleUn()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_Url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                resp = response.trim();
            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(LoginActivity.this,"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("em", em);
                return Params;

            }
        };

        DBConnection.getInstance(LoginActivity.this).addTorequestque(stringRequest);
    }
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
    }
    public void resetpwd(View v)
    {
        Intent inte = new Intent(LoginActivity.this,ResetPasswordActivity.class);
        startActivityForResult(inte,1);
        finish();
    }

}