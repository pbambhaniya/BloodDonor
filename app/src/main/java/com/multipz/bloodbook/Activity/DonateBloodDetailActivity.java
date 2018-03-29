package com.multipz.bloodbook.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Constant_method;
import com.multipz.bloodbook.Utils.MyAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonateBloodDetailActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    CircleImageView img_profile;
    ImageView img_location;
    TextView txt_blood_type, txt_name, txt_location_name, txt_phone_no, txt_address;
    Button btn_accept;
    Context context;
    LinearLayout btnback;
    private String param, bloodReqID, ReqID, userID, area;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_donate_blood_detail);
        context = this;

        ref();
        init();
    }

    private void ref() {
        img_profile = (CircleImageView) findViewById(R.id.img_profile);
        img_location = (ImageView) findViewById(R.id.img_location);
        txt_blood_type = (TextView) findViewById(R.id.txt_blood_type);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_location_name = (TextView) findViewById(R.id.txt_location_name);
        txt_phone_no = (TextView) findViewById(R.id.txt_phone_no);
        txt_address = (TextView) findViewById(R.id.txt_address);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        btnback = (LinearLayout) findViewById(R.id.btnback);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void init() {
        area = getIntent().getStringExtra("area");

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txt_name.setText(getIntent().getStringExtra("uername"));
        txt_blood_type.setText(getIntent().getStringExtra("bloodtype"));
        txt_phone_no.setText(getIntent().getStringExtra("contactno"));
        txt_location_name.setText(getIntent().getStringExtra("address") + "," + area + "," + getIntent().getStringExtra("location"));
        //txt_address.setText(getIntent().getStringExtra("address"));
        Glide.with(context).load(getIntent().getStringExtra("img")).into(img_profile);
        bloodReqID = getIntent().getStringExtra("bloodReqID");
        ReqID = getIntent().getStringExtra("ReqID");
        userID = getIntent().getStringExtra("userID");

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popUp();
            }
        });
    }

    public void popUp() {
        LayoutInflater inflater = this.getLayoutInflater();
        View c = inflater.inflate(R.layout.save_later_popup, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.cancel);

        ((TextView)c.findViewById(R.id.head_desc)).setText("Sure you want to accept this blood request?");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDonateBloodView();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    private void GetDonateBloodView() {
        JSONObject objFinal = new JSONObject();
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("ActionStatus", "2");
            object.put("UserId", userID);
            object.put("BloodRequestId", bloodReqID);
            objFinal.put("Json", object);
            param = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.REACT_ON_NOTIFICATION, DonateBloodDetailActivity.this, param, Config.API_REACT_ON_NOTIFICATION);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {

        int IsSuccess;
        String Message, txtresponse;
        if (flag == Config.API_REACT_ON_NOTIFICATION) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                IsSuccess = jsonObject.getInt("IsSuccess");
                Message = jsonObject.getString("Message");
                if (IsSuccess == 1) {
                    pd.dismiss();
                    Intent i = new Intent(DonateBloodDetailActivity.this, DrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } else if (IsSuccess == 0) {
                    pd.dismiss();
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
