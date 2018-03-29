package com.multipz.bloodbook.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multipz.bloodbook.Chatting.ChattingActivity;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAcceptChattingActivity extends AppCompatActivity {

    private CircleImageView img_profile;
    private TextView txt_blood_type, txt_name, txt_city, txt_phone_no, txt_address;
    private Button btn_Call, btn_chat;
    private String username, address, cno, image, bloodtype, city, reqid, BloodReqID, area;
    private Context context;
    private LinearLayout btnback;
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_request_accept_chatting);
        context = this;
        reference();
        init();

    }

    private void reference() {
        img_profile = (CircleImageView) findViewById(R.id.img_profile);
        txt_blood_type = (TextView) findViewById(R.id.txt_blood_type);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_city = (TextView) findViewById(R.id.txt_city);
        txt_phone_no = (TextView) findViewById(R.id.txt_phone_no);
        txt_address = (TextView) findViewById(R.id.txt_address);
        btn_Call = (Button) findViewById(R.id.btn_Call);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        btnback = (LinearLayout) findViewById(R.id.btnback);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        username = getIntent().getStringExtra("username");
        address = getIntent().getStringExtra("address");
        reqid = getIntent().getStringExtra("reqid");
        cno = getIntent().getStringExtra("cno");
        image = getIntent().getStringExtra("image");
        bloodtype = getIntent().getStringExtra("bloodtype");
        city = getIntent().getStringExtra("city");
        area = getIntent().getStringExtra("area");
        BloodReqID = getIntent().getStringExtra("BloodReqID");
        txt_name.setText(username);
        // txt_address.setText(address);
        txt_city.setText(address);
        txt_phone_no.setText(cno);
        txt_blood_type.setText(bloodtype);
        Glide.with(context).load(Config.Img + "" + image).into(img_profile);
        img = Config.Img + "" + image;
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = "tel:" + cno;
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse(number));
                if (ActivityCompat.checkSelfPermission(RequestAcceptChattingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    startActivity(callIntent);
                    return;
                } else {
                    startActivity(callIntent);
                }


            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestAcceptChattingActivity.this, ChattingActivity.class);
                intent.putExtra("img", img);
                intent.putExtra("reqid", reqid);
                intent.putExtra("BloodReqID", BloodReqID);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    public void popupChat() {
        LayoutInflater inflater = RequestAcceptChattingActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_chat, null);

        Button btn_ok = (Button) c.findViewById(R.id.btn_ok);
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestAcceptChattingActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
