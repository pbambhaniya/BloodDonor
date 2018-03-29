package com.multipz.bloodbook.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReceiveNotificationActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {

    private LinearLayout btnback;
    private CircleImageView img_profile;
    private TextView txt_blood_type, txt_name, txt_location_name, txtCno;
    private Button btn_accept, btnDecline, btn_notnow;
    private String ContactNo, UserName, RequestId, Address, Message, UserImage, UserId, BloodType;
    private Context context;
    private Shared shared;
    private String param;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_receive_notification);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void init() {

        Intent i = getIntent();
        ContactNo = i.getStringExtra("ContactNo");
        UserName = i.getStringExtra("UserName");
        RequestId = i.getStringExtra("RequestId");
        Address = i.getStringExtra("Address");
        Message = i.getStringExtra("Message");
        UserImage = i.getStringExtra("UserImage");
        UserId = i.getStringExtra("UserId");
        BloodType = i.getStringExtra("BloodType");


        txt_name.setText(UserName);
        txtCno.setText(ContactNo);
        txt_location_name.setText(Address);
        txt_name.setText(UserName);
        txt_blood_type.setText(BloodType);
        Glide.with(context).load(Config.Img + "" + UserImage).into(img_profile);


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReactOnNoitification("2");
            }
        });
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReactOnNoitification("3");
            }
        });
        btn_notnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReactOnNoitification("4");
                Intent i = new Intent(ReceiveNotificationActivity.this, DrawerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void reference() {
        img_profile = (CircleImageView) findViewById(R.id.img_profile);
        txt_blood_type = (TextView) findViewById(R.id.txt_blood_type);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_location_name = (TextView) findViewById(R.id.txt_location_name);
        txtCno = (TextView) findViewById(R.id.txtCno);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        btnDecline = (Button) findViewById(R.id.btnDecline);
        btn_notnow = (Button) findViewById(R.id.btn_notnow);
        btnback = (LinearLayout) findViewById(R.id.btnback);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }


    private void ReactOnNoitification(String ActionStatus) {
        JSONObject objFinal = new JSONObject();
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("ActionStatus", ActionStatus);
            object.put("UserId", shared.getUserId());
            object.put("BloodRequestId", RequestId);
            objFinal.put("Json", object);
            param = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.REACT_ON_NOTIFICATION, ReceiveNotificationActivity.this, param, Config.API_REACT_ON_NOTIFICATION);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void popUpReactOnNotifiaction(String msg) {
        LayoutInflater inflater = ReceiveNotificationActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_request_accept, null);
        TextView txtMSG = (TextView) c.findViewById(R.id.txtMSG);
        Button btn_ok = (Button) c.findViewById(R.id.btn_ok);

        AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveNotificationActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        txtMSG.setText(msg);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReceiveNotificationActivity.this, DrawerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
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
                    pd.dismiss();
                    txtresponse = jsonObject.getString("Response");
                    if (txtresponse.matches("2")) {
                        String msg = "Are you sure want to Accept blood donate request ?";
                        popUpReactOnNotifiaction(msg);
                    } else if (txtresponse.matches("3")) {
                        String msg = "Save you Later";
                        popUpReactOnNotifiaction(msg);
                    } else if (txtresponse.matches("4")) {
                        Intent i = new Intent(ReceiveNotificationActivity.this, DrawerActivity.class);
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
}
