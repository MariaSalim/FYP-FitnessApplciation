package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class ActivityRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CallbackManager cbm;
    EditText edname, edun, edph, edpwd;
    String n,un,gclick="",ph,pwd,em="",flag="f",server_url = "http://192.168.100.188/happy_fit/Register.php",server_url1 = "http://192.168.100.188/happy_fit/UserNameForEmail.php";
    CircularProgressButton btn;
    String bday,fclick="";

    ImageView ivgoogle;
    LoginButton lbfacebook;
    private GoogleSignInClient gsc;
    private static final int SIGNIN_REQUEST_CODE = 1;
    private static final String EMAIL = "email";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();   //hide Action Bar

        cbm = CallbackManager.Factory.create();

        ivgoogle =(ImageView) findViewById(R.id.googleiv);
        lbfacebook=(LoginButton) findViewById(R.id.facebooklb);

        lbfacebook.setPermissions(Arrays.asList(EMAIL));

        // Callback registration
        lbfacebook.registerCallback(cbm, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                CommonFunctions.AD(ActivityRegister.this,"Authentication Failed","Couldn't Login");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("TAG", "onError: "+exception.getMessage());
            }
        });

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        //Reference of EditTexts

        btn = (CircularProgressButton) findViewById(R.id.cirRegisterButton);
        edname = (EditText) findViewById(R.id.edName);
        edun = (EditText) findViewById(R.id.edUN);
        edph = (EditText) findViewById(R.id.edPh);
        edpwd = (EditText) findViewById(R.id.edpwd);

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

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                n = edname.getText().toString();
                un =edun.getText().toString();
                ph=edph.getText().toString();
                pwd = edpwd.getText().toString();

                String unamePattern = "\\w+";

                if(n.isEmpty() || un.isEmpty() || ph.isEmpty() || pwd.isEmpty())
                {
                    if(n.isEmpty())
                    {
                        edname.setError("You cannot leave this field Empty");
                    }
                    if(un.isEmpty())
                    {
                        edun.setError("You cannot leave this field Empty");
                    }
                    if(ph.isEmpty())
                    {
                        edph.setError("You cannot leave this field Empty");
                    }
                    if(pwd.isEmpty())
                    {
                        edpwd.setError("You cannot leave this field Empty");
                    }
                    CommonFunctions.AD(ActivityRegister.this,"Empty Fields","Please Fill All The Fields");

                }
                else if(!(un.matches(unamePattern)) || pwd.length()<6 || ph.length()<11)// || ChkUserName())
                {

                    if(!(un.matches(unamePattern)))
                    {
                        edun.setError("You can Only use Letters, Digits, and underscore");
                    }
                    if(ph.length()<11)
                    {
                        edph.setError("Enter a valid Phone Number");
                    }
/*
                    if(ChkUserName())
                    {
                        e2.setError("UserName Already Exists!");
                        Functions.AD(getContext(), "UserName Already Exists!", "");
                        return;
                    }
*/
                    if(pwd.length()<6)
                    {
                        edpwd.setError("Your Password should be of Atleast 6 Characters");
                    }

                    CommonFunctions.AD(ActivityRegister.this, "", "Please Fill All Fields Correctly");

                }
                else
                {
                    btn.startAnimation();
                    toInfo();
                    if(gclick.equals("yes") || fclick.equals("yes"))
                    {
                        unemail();
                    }
                }
            }
        });



    }
    public void MoveToLogin(View View){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
    }

    private void toInfo()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.trim();

                Log.d("resp", "onResponse: "+resp);
                Log.d("compare", "onResponse: "+"t".equals(resp));
                if ("t".equals(resp))
                {
                    btn.revertAnimation();
                    CommonFunctions.AD(ActivityRegister.this, "Registered", "Your Account Has Been Created");
                    Intent i = new Intent(ActivityRegister.this,ActivityMeasurements.class);
                    CommonFunctions.setUserName(ActivityRegister.this,un);
                    startActivity(i);
                } else
                    {
                        btn.revertAnimation();
                    CommonFunctions.AD(ActivityRegister.this, " 404", "An Error Occured While Creating Your Account");
                }
            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(ActivityRegister.this,"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("n", n);
                Params.put("un", un);
                Params.put("ph", ph);
                Params.put("pwd",pwd);

                return Params;

            }
        };

        DBConnection.getInstance(ActivityRegister.this).addTorequestque(stringRequest);
    }

    private void unemail()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.trim();

                Log.d("resp", "onResponse: "+resp);
                Log.d("compare", "onResponse: "+"t".equals(resp));
                if ("t".equals(resp))
                {

                } else {
                    CommonFunctions.AD(ActivityRegister.this, " 404", "An Error Occured While Creating Your Account");
                }
            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(ActivityRegister.this,"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("em", em);
                Params.put("un", un);

                return Params;

            }
        };

        DBConnection.getInstance(ActivityRegister.this).addTorequestque(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        cbm.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("T0AG", "onActivityResult: ");
        if(requestCode == SIGNIN_REQUEST_CODE)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(acc);
            }
            catch(ApiException e)
            {

            }
            GoogleSignInResult res = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("TAG", "onActivityResult: ");
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
                    GoogleSignInAccount sia = GoogleSignIn.getLastSignedInAccount(ActivityRegister.this);
                    if(sia!=null)
                    {
                        em = sia.getEmail();
                        gclick="yes";
                        edname.setText(sia.getDisplayName());
                    }
                }
                else
                {
                    CommonFunctions.AD(ActivityRegister.this, "AuthenticationFailed","");
                }

            }
        });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            em = user.getEmail();
                            Log.d("TAG1222222", "onComplete: "+em);
                            edph.setText(user.getPhoneNumber());
                            Log.d("TAG1222222", "onComplete: "+user.getPhoneNumber());
                            edname.setText(user.getDisplayName());
                            fclick="yes";
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(ActivityRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
