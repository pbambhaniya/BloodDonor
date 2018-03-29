package com.multipz.bloodbook.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Application;

public class DonateMoney extends AppCompatActivity {

    int count = 0;
    private ImageView btnminus, btnplus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_donate_money);
        reference();
        init();
    }

    private void reference() {
        btnminus = (ImageView) findViewById(R.id.btnminus);
        btnplus = (ImageView) findViewById(R.id.btnplus);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void init() {
        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseInteger();
            }
        });
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseInteger();
            }
        });
    }


    public void increaseInteger() {
        count = count + 1;
        if (count > 5) {
            display(count);
        }

    }

    public void decreaseInteger() {
        count = count - 1;
        if (count > 1) {
            display(count);
        }

    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(R.id.txtCurrancy);
        displayInteger.setText("" + number);
    }
}
