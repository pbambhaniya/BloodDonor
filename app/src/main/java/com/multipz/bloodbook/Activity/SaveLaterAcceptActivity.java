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
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class SaveLaterAcceptActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {

    private CircleImageView img_profile;
    private Context context;
    private TextView txt_blood_type, txt_name, txt_location_name, txt_moobile_no;
    private LinearLayout btnback;
    private String param, bloodReqID, UserID;
    private Button btn_accept_save_later, btn_decline_save_later;
    private Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_save_later_accept);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void reference() {
        btnback = (LinearLayout) findViewById(R.id.btnback);

        img_profile = (CircleImageView) findViewById(R.id.img_profile);
        txt_blood_type = (TextView) findViewById(R.id.txt_blood_type);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_location_name = (TextView) findViewById(R.id.txt_location_name);
        txt_moobile_no = (TextView) findViewById(R.id.txt_moobile_no);
        btn_accept_save_later = (Button) findViewById(R.id.btn_accept_save_later);
        btn_decline_save_later = (Button) findViewById(R.id.btn_decline_save_later);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txt_name.setText(getIntent().getStringExtra("username"));
        txt_blood_type.setText(getIntent().getStringExtra("bloodtype"));
        txt_moobile_no.setText(getIntent().getStringExtra("mobileno"));
        txt_location_name.setText(getIntent().getStringExtra("address"));
        Glide.with(context).load(getIntent().getStringExtra("img")).into(img_profile);

        bloodReqID = getIntent().getStringExtra("bloodReqID");
        UserID = getIntent().getStringExtra("UserID");
        btn_accept_save_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpAccept();
            }
        });

        btn_decline_save_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDecline();
            }
        });
    }

    private void GetDonateBloodView(String actionStatus) {
        JSONObject objFinal = new JSONObject();
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("ActionStatus", actionStatus);
            object.put("UserId", shared.getUserId());
            object.put("BloodRequestId", bloodReqID);
            objFinal.put("Json", object);
            param = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.REACT_ON_NOTIFICATION, SaveLaterAcceptActivity.this, param, Config.API_REACT_ON_NOTIFICATION);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }*/

    }

    public void popUpAccept() {
        LayoutInflater inflater = SaveLaterAcceptActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.save_later_popup, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.cancel);

        ((TextView)c.findViewById(R.id.head_desc)).setText("Sure you want to accept this blood request?");

        AlertDialog.Builder builder = new AlertDialog.Builder(SaveLaterAcceptActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDonateBloodView("2");
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


    public void popUpDecline() {
        LayoutInflater inflater = SaveLaterAcceptActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.save_later_popup, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(SaveLaterAcceptActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDonateBloodView("3");
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
                    txtresponse = jsonObject.getString("Response");
                    if (txtresponse.matches("2")) {
                        String msg = "Are you sure want to Accept blood donate request ?";
                        popUpReactOnNotifiaction(msg);
                    } else if (txtresponse.matches("3")) {
                        Intent i = new Intent(SaveLaterAcceptActivity.this, DrawerActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                } else if (IsSuccess == 0) {
                    pd.dismiss();
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void popUpReactOnNotifiaction(String msg) {
        LayoutInflater inflater = SaveLaterAcceptActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_request_accept, null);
        TextView txtMSG = (TextView) c.findViewById(R.id.txtMSG);
        Button btn_ok = (Button) c.findViewById(R.id.btn_ok);

        AlertDialog.Builder builder = new AlertDialog.Builder(SaveLaterAcceptActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        txtMSG.setText(msg);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SaveLaterAcceptActivity.this, DrawerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

    }
}

