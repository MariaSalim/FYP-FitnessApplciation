package com.example.fyp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityUpdateWeight extends AppCompatActivity {

    //EditText edw;
    String wei;
    ImageButton ib;
    ArrayList<String> WeightList;
    String tempW="";
    String server_url="http://192.168.100.188/happy_fit/updateWeight.php";
    String server_url1="http://192.168.100.188/happy_fit/getWeight.php";
    ListView lv;
    AlertDialog builder;

    ArrayAdapter<String> adp;
    View vweighted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_weight);

       getSupportActionBar().hide();

        WeightList=new ArrayList<String>();
        //edw = (EditText) findViewById(R.id.edw);
        ib =(ImageButton) findViewById(R.id.btnaddWeight);

        getWeight();

        adp = new ArrayAdapter<String>(ActivityUpdateWeight.this, android.R.layout.simple_list_item_1,WeightList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(getResources().getColor(R.color.textclr));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tv.setTypeface(getResources().getFont(R.font.raleway_medium));
                }
                tv.setTextSize(18);

                // Generate ListView Item using TextView
                return view;
            }
        };
        //!Weight
        lv = findViewById(R.id.lvweights);
        lv.setAdapter(adp);



    }
    public void updWeight(View v)
    {
        LayoutInflater li = LayoutInflater.from(this);
        vweighted = li.inflate(R.layout.weight_dialog_layout, null);

        builder = new AlertDialog.Builder(ActivityUpdateWeight.this, R.style.Popup_Dialog)
                .setPositiveButton(R.string.Add, null)
                .setNegativeButton(R.string.Cancel, null)
                .create();

        final EditText edwei = (EditText) vweighted.findViewById(R.id.edwei);
        builder.setView(vweighted);
        builder.setTitle(R.string.AddWeight);

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edwei.getText().toString().isEmpty())
                        {
                            edwei.setError("Please fill this Field");

                            CommonFunctions.AD(ActivityUpdateWeight.this,"Empty Fields","Please Fill All The Fields");
                        }
                        else {
                            wei=edwei.getText().toString();
                                intoDB();
                        }
                    }
                });

                final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnDecline.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });

        /* Show the dialog */
        builder.show();
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
                    adp.add(wei);
                    adp.notifyDataSetChanged();
                    builder.dismiss();
                    Snackbar.make(
                            findViewById(R.id.rlsnackbar),
                            R.string.weiadded,
                            Snackbar.LENGTH_SHORT
                    )
                            .setBackgroundTint(getResources().getColor(R.color.introbg))
                            .show();
                } else {
                    CommonFunctions.AD(ActivityUpdateWeight.this, "", "Error Occured! Couldn't Update Weight");
                }
            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(ActivityUpdateWeight.this,"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("un", CommonFunctions.getUserName(ActivityUpdateWeight.this));
                Params.put("w",wei);

                return Params;

            }
        };

        DBConnection.getInstance(ActivityUpdateWeight.this).addTorequestque(stringRequest);
    }
    private void getWeight()
    {
        Log.d("Function Check", "getWeight: ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                String resp = response.trim();

                Log.d("resp", "onResponse: "+resp);
                tempW = resp;
                Log.d("tempw", "onCreate: "+tempW);
                String temp[]= tempW.split(",");
                Log.d("TAGlen", "onResponse: "+temp.length);
                for(int i=0;i<temp.length;i++)
                {
                    Log.d("Array", "onCreate: "+temp[i]);
                    WeightList.add(temp[i]);
                }


            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(ActivityUpdateWeight.this,"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("un", CommonFunctions.getUserName(ActivityUpdateWeight.this));
                return Params;

            }
        };

        DBConnection.getInstance(ActivityUpdateWeight.this).addTorequestque(stringRequest);
    }

}