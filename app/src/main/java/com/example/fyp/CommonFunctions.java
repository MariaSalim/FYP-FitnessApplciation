package com.example.fyp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;

public class CommonFunctions
{
    public static void AD(Context c, String t, String Message)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(c, R.style.alertDialog);

        builder.setTitle(t);
        builder.setMessage(Message);
        builder.setIcon(R.drawable.ic_alert);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        //Dismiss once everything is OK.
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }
    public static final String PREF_USER_NAME= "User";
    public static final String PREF_Intro= "Intro";
    public static final String PREF_Notf_Switch_State= "SwitchState";
    private static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    public static void setSwitchState(Context ctx, String state)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString(PREF_Notf_Switch_State, state);
        editor.commit();
    }

    public static String getSwitchState(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_Notf_Switch_State, "");
    }
    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    public static void setIntro(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString(PREF_Intro, "Clicked");
        editor.commit();
    }

    public static String getIntro(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_Intro, "");
    }
    public static void clearCredentials(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
    public static Double calBmi(Double h, Double w)
    {
        h = h/100;
        /*Formula: weight (kg) / [height (m)]2*/
        Double b;

        b = w/ Math.pow(h, 2.0);

        return b;
    }
    public static String round(Double d)
    {
        String s= String.format("%.1f", d);

        return s;
    }

}
