package com.multipz.bloodbook.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Shared;

/**
 * Created by Admin on 04-12-2017.
 */

public class BirthdayActivity extends AppCompatActivity {

    Context context;
    Shared shared;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.noti_birthday);

        context = this;
        shared = new Shared(context);

        ((TextView) findViewById(R.id.txt_name)).setText(shared.getUserName());
        Glide.with(context).load(Config.Img + getIntent().getStringExtra("img")).error(getResources().getDrawable(R.drawable.cake)).into((ImageView) findViewById(R.id.img));

        findViewById(R.id.btnback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
