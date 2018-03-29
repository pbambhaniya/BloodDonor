package com.multipz.bloodbook.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RelativeLayout;

import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Application;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cv_req_blood, cv_donate_blood, cv_current_blood, cv_save_blood, cv_active_donor, cv_news, cv_my_donate_history, cv_total_donate_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        ref();
    }

    private void ref() {
        cv_req_blood = (CardView) findViewById(R.id.cv_req_blood);
        cv_donate_blood = (CardView) findViewById(R.id.cv_donate_blood);
        cv_current_blood = (CardView) findViewById(R.id.cv_current_blood);
        cv_save_blood = (CardView) findViewById(R.id.cv_save_blood);
        cv_active_donor = (CardView) findViewById(R.id.cv_active_donor);
        cv_news = (CardView) findViewById(R.id.cv_news);
        cv_my_donate_history = (CardView) findViewById(R.id.cv_my_donate_history);
        cv_total_donate_history = (CardView) findViewById(R.id.cv_total_donate_history);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        cv_req_blood.setOnClickListener(this);
        cv_donate_blood.setOnClickListener(this);
        cv_save_blood.setOnClickListener(this);
        cv_my_donate_history.setOnClickListener(this);
        cv_current_blood.setOnClickListener(this);
    }

    private void init() {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_req_blood) {
            Intent i = new Intent(DashboardActivity.this, RequestBloodActivity.class);
            startActivity(i);

        } else if (view.getId() == R.id.cv_donate_blood) {
            Intent i = new Intent(DashboardActivity.this, DonateBloodActivity.class);
            startActivity(i);

        } else if (view.getId() == R.id.cv_current_blood) {
            Intent i = new Intent(DashboardActivity.this, CurrentBloodRequestActivity.class);
            startActivity(i);
        } else if (view.getId() == R.id.cv_save_blood) {
            Intent i = new Intent(DashboardActivity.this, SaveBloodRequestActivity.class);
            startActivity(i);
        } else if (view.getId() == R.id.cv_active_donor) {

        } else if (view.getId() == R.id.cv_news) {

        } else if (view.getId() == R.id.cv_my_donate_history) {
            Intent i = new Intent(DashboardActivity.this, MyDonationHistory.class);
            startActivity(i);
        } else if (view.getId() == R.id.cv_total_donate_history) {

        }
    }
}