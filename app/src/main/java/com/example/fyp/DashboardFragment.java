package com.example.fyp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment
{
    LineChart lc;
    ArrayList<Entry> linelist;
    LineDataSet lds;
    LineData ldata;

    String server_url1="http://192.168.100.188/happy_fit/getWeight.php";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, null);

        linelist = new ArrayList<Entry>();

        lc = (LineChart) vg.findViewById(R.id.lc);

        getWeight();

        return vg;
    }
    private void configureLineChart()
    {
        lds = new LineDataSet(linelist,"Progress");
        ldata = new LineData(lds);
        lc.setData(ldata);

        //Formatting

        lc.getXAxis().setDrawGridLines(false);
        lc.getAxisRight().setDrawGridLines(false);
        lc.getAxisLeft().setDrawGridLines(false);
        lc.setDrawBorders(false);
        lc.setBorderColor(getResources().getColor(R.color.textclr));
        lc.getXAxis().setTextColor(getResources().getColor(R.color.textclr));
        lc.getAxisLeft().setTextColor(getResources().getColor(R.color.textclr));
        lc.getAxisRight().setTextColor(getResources().getColor(R.color.textclr));
        lc.getLegend().setEnabled(false);
        lc.getDescription().setText(getString(R.string.weiProgress));
        lc.getDescription().setTextColor(getResources().getColor(R.color.textclr));
        lc.getDescription().setTextSize(12);

        //lds
        lds.setColor(getResources().getColor(R.color.introbg)); //Line Color
        lds.setCircleColor(getResources().getColor(R.color.introbg));
        lds.setValueTextColor(getResources().getColor(R.color.textclr)); //Values Over Line
        lds.setValueTextSize(17);
        lds.setFillColor(getResources().getColor(R.color.introbg));

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
                String temp[]= resp.split(",");
                Log.d("TAGlen", "onResponse: "+temp.length);
                for(int i=0;i<temp.length;i++)
                {
                    Log.d("Array", "onCreate: "+temp[i]);
                    linelist.add(new Entry(i+1,Float.parseFloat(temp[i])));
                }
                for(int i=0;i<linelist.size();i++)
                {
                    Log.d("Array", "onCreate: "+linelist.get(i));
                }
                configureLineChart();
            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                CommonFunctions.AD(getActivity(),"Something Went wrong","");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();

                Params.put("un", CommonFunctions.getUserName(getActivity()));
                return Params;

            }
        };

        DBConnection.getInstance(getActivity()).addTorequestque(stringRequest);
    }

}
