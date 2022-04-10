package com.example.fyp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class ProfileFragment extends Fragment
{
    TextView tv;
    String[] lvItems = new String[]
            {
                    "Update Weight", "FAQs", "Feedback","Contact Us","Logout"
            };
    ArrayAdapter<String> adp;
    Switch swnotf;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.fragment_profile, null);
        ListView l = (ListView) vg.findViewById(R.id.lv_acct);

        tv = (TextView) vg.findViewById(R.id.tvname);
        tv.setText("Welcome back, "+CommonFunctions.getUserName(getActivity()));

        swnotf = (Switch) vg.findViewById(R.id.swnotf);

        if(CommonFunctions.getSwitchState(getActivity()).equals("ON"))
        {
            swnotf.setChecked(true);
        }


        swnotf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b)
                {
                    Log.d("DEBUG", "onCheckedChanged: "+ "ON");
                    CommonFunctions.setSwitchState(getContext(),"ON");
                    NotificationHelper.scheduleRepeatingElapsedNotification(getContext());
                    NotificationHelper.enableBootReceiver(getContext());
                }
                else
                {
                    Log.d("DEBUG", "onCheckedChanged: "+"OFF");
                    CommonFunctions.setSwitchState(getContext(),"OFF");
                    NotificationHelper.cancelAlarmElapsed();
                    NotificationHelper.disableBootReceiver(getContext());
                }
            }
        });

        adp = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lvItems){
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

        l.setAdapter(adp);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(position == 0)
                {
                    updWeight();
                }
                else if(position == 1)
                {
                    faqs();
                }
                else if(position == 2)
                {
                    feedback();
                }
                else if (position == 3) {
                    //Contact Us
                    contUS();
                }
                else if(position == 4)
                {
                    logout();
                }
            }
        });

        return vg;
    }
    //Return from Contact us
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(getActivity(),"Email Sent", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(),"Error Sending Email", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(getActivity(),"Feedback Sent", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(),"Error Sending Feedback", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void updWeight()
    {
        Intent i = new Intent(getActivity(), ActivityUpdateWeight.class);
        startActivity(i);
    }
    private void contUS()
    {
        Intent i = new Intent(getActivity(), ActivityContactUs.class);
        startActivityForResult(i, 1);
    }
    private void feedback()
    {
        Intent i = new Intent(getActivity(), ActivityFeedback.class);
        startActivityForResult(i, 2);
    }
    private void faqs()
    {
        Intent i = new Intent(getActivity(), ActivityFAQs.class);
        startActivity(i);
    }
    private void logout()
    {
        CommonFunctions.clearCredentials(getActivity());
        CommonFunctions.setIntro(getActivity());
        startActivity(new Intent(getActivity(),LoginActivity.class));
    }

}
