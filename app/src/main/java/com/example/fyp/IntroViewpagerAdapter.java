package com.example.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import static android.content.Context.MODE_PRIVATE;

public class IntroViewpagerAdapter extends PagerAdapter
{
    Context ctx;

    public IntroViewpagerAdapter(Context ctx)
    {
        this.ctx=ctx;
    }
    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        LayoutInflater lf = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View v = lf.inflate(R.layout.intro1,container,false);

        ImageView iv = v.findViewById(R.id.IVSliders);

        ImageView i1 = v.findViewById(R.id.ind1);
        ImageView i2 = v.findViewById(R.id.ind2);
        ImageView i3 = v.findViewById(R.id.ind3);

        TextView title = v.findViewById(R.id.Title);

        ImageView next = v.findViewById(R.id.IVnext);
        ImageView back = v.findViewById(R.id.IVback);

        switch(position)
        {
            case 0:
            {
                iv.setImageResource(R.drawable.img_exercise);
                i1.setImageResource(R.drawable.selectedindicator);
                i2.setImageResource(R.drawable.unselectedindicator);
                i3.setImageResource(R.drawable.unselectedindicator);
                title.setText(R.string.exer);
                back.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);

                break;
            }
            case 1:
            {
                iv.setImageResource(R.drawable.img_sleep);
                i1.setImageResource(R.drawable.unselectedindicator);
                i2.setImageResource(R.drawable.selectedindicator);
                i3.setImageResource(R.drawable.unselectedindicator);
                title.setText(R.string.sleep);
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);

                break;
            }
            case 2:
            {
                iv.setImageResource(R.drawable.img_healthy);
                i1.setImageResource(R.drawable.unselectedindicator);
                i2.setImageResource(R.drawable.unselectedindicator);
                i3.setImageResource(R.drawable.selectedindicator);
                title.setText(R.string.fithhapp);
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);

                break;
            }
        }

        Button btn = v.findViewById(R.id.btngs);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CommonFunctions.setIntro(ctx);
                Intent intent = new Intent(ctx, ActivityRegister.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                IntroActivity.vp.setCurrentItem(position+1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntroActivity.vp.setCurrentItem(position-1);
            }
        });

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
    {
        container.removeView((View) object);
    }

}
