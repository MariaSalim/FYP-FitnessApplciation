package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ActivityContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().hide();

        ImageView ivbtn= (ImageView) findViewById(R.id.sendem);

        EditText edsubj = (EditText) findViewById(R.id.edsubj);
        EditText edbody = (EditText) findViewById(R.id.edcusmsg);

        Intent emaili = new Intent(Intent.ACTION_SEND);

        ivbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String to="maimoonakhan099@gmail.com";
                String to2="mariasalim.pk@gmail.com";

                String subject=edsubj.getText().toString();
                String message=edbody.getText().toString();

                if(subject.isEmpty() || message.isEmpty()) {
                    if(subject.isEmpty())
                    {
                        edsubj.setError("Please Fill This Field");
                    }
                    if(message.isEmpty())
                    {
                        edbody.setError("Please Fill This Field");
                    }
                    CommonFunctions.AD(ActivityContactUs.this,"EMPTY FIELDS","Please Fill All The Fields");
                }
                else
                {
                    emaili.setData(Uri.parse("mailto:"));
                    emaili.putExtra(Intent.EXTRA_EMAIL, new String[]{to, to2});
                    emaili.putExtra(Intent.EXTRA_SUBJECT, CommonFunctions.getUserName(ActivityContactUs.this)+": "+subject);
                    emaili.putExtra(Intent.EXTRA_TEXT, message);

                    emaili.setType("message/rfc822");

                    startActivity(Intent.createChooser(emaili, "Choose Email client :"));

                    emailSent();
                }
            }
        });

    }
    private void emailSent()
    {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}