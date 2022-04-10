package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment
{
    GridView gv;
    GridLayoutAdapter gladp;
    Intent intent;
    ViewGroup vg;
    Button btnPE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        vg = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        gv = vg.findViewById(R.id.grid);

        String[] titles = {"Arms","Belly","Chest","Thigh","Back","Full Body"};
        int[] imgs ={R.drawable.wk_arms,R.drawable.wk_belly,R.drawable.wk_chest,R.drawable.wk_thigh,R.drawable.wk_back,R.drawable.wk_fullbody};

        gladp = new GridLayoutAdapter(getActivity(), imgs, titles);
        gv.setAdapter(gladp);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent in = new Intent(getActivity(), ActivityExercisesByCategory.class);
                if(titles[i].equals("Full Body"))
                {
                    in.putExtra("cateN","FullBody");
                }
                else {
                    in.putExtra("cateN", titles[i]);
                }
                startActivity(in);


            }
        });
        btnPE = (Button) vg.findViewById(R.id.btnPE);
        btnPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                displayWindow();
            }
        });
        return vg;
    }
    private void displayWindow()
    {

        ViewGroup viewGroup = vg.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_pe, viewGroup, false);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        Button ok = (Button) dialogView.findViewById(R.id.buttonOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
