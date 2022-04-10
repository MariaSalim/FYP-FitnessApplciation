package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ActivityFAQs extends AppCompatActivity
{

    private ExpandableListView expandableListView;
    private ExpandableLVAdapter expandableListViewAdapter;
    private List<String> listDataGroup;
    private HashMap<String, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_qs);
        getSupportActionBar().hide();


        expandableListView = findViewById(R.id.elv);

        // initializing the list of groups
        listDataGroup = new ArrayList<>();
        // initializing the list of child
        listDataChild = new HashMap<>();
        // initializing the adapter object
        expandableListViewAdapter = new ExpandableLVAdapter(this, listDataGroup, listDataChild);
        // setting list adapter
        expandableListView.setAdapter(expandableListViewAdapter);

    // preparing list data
        initListData();
    }


    private void initListData() {
        List<String> arrayQs = new ArrayList<>();
        String[] arr = getResources().getStringArray(R.array.qsFAQS);


        // Adding group data

        listDataGroup.add(arr[0]);
        listDataGroup.add(arr[1]);
        listDataGroup.add(arr[2]);
        listDataGroup.add(arr[3]);
        listDataGroup.add(arr[4]);


        // array of strings
        String[] array;

        List<String> a1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.AnsFAQs);
        for (String item : array) {
            a1.add(item);
        }

        List<String> a2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.AnsFAQs1);
        for (String item : array) {
            a2.add(item);
        }

        List<String> a3 = new ArrayList<>();
        array = getResources().getStringArray(R.array.AnsFAQs2);
        for (String item : array) {
            a3.add(item);
        }

        List<String> a4 = new ArrayList<>();
        array = getResources().getStringArray(R.array.AnsFAQs3);
        for (String item : array) {
            a4.add(item);
        }
        List<String> a5 = new ArrayList<>();
        array = getResources().getStringArray(R.array.AnsFAQs4);
        for (String item : array) {
            a5.add(item);
        }

        // Adding child data
        listDataChild.put(listDataGroup.get(0), a1);
        listDataChild.put(listDataGroup.get(1), a2);
        listDataChild.put(listDataGroup.get(2), a3);
        listDataChild.put(listDataGroup.get(3), a4);
        listDataChild.put(listDataGroup.get(4), a5);
        // notify the adapter
        expandableListViewAdapter.notifyDataSetChanged();
    }

}