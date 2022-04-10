package com.example.fyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridLayoutAdapter extends BaseAdapter
{
    Context context;
    int imgs[];
    String Titles[];
    LayoutInflater inflter;
    public GridLayoutAdapter(Context applicationContext, int[] logos, String[] Titles) {
        this.context = applicationContext;
        this.imgs = logos;
        this.Titles=Titles;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount()
    {
        return Titles.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        view = inflter.inflate(R.layout.category_grid_layout, null); // inflate the layout
        TextView tvt = (TextView) view.findViewById(R.id.cateTitle);
        tvt.setText(Titles[i]);

        ImageView icon = (ImageView) view.findViewById(R.id.ivCateImg);
        icon.setImageResource(imgs[i]);

        return view;
    }
}
