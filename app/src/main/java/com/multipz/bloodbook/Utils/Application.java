package com.multipz.bloodbook.Utils;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Application extends android.app.Application {

    public static Shared shared;
    public static Typeface fonttitilliumRegular, fonttitilliumBold;
    public static Typeface FontCoral;


    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Regular.ttf");
        fonttitilliumRegular = Typeface.createFromAsset(getResources().getAssets(), "fonts/Roboto-Regular.ttf");
        shared = new Shared(this);
    }

    public static void setFontDefault(ViewGroup group) {
        setFont(group, Application.fonttitilliumRegular);
    }

    public static void setFont(ViewGroup group, Typeface font) {
        int count = 0;
        count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView)
                ((TextView) v).setTypeface(font);
            else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }
}
