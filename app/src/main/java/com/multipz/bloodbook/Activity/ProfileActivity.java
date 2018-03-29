package com.multipz.bloodbook.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.AppController;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.MyAsyncTask;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Context context;
    private Shared shared;
    private TextView txtbloodGrouptype, txtProfileUsername, txtAddress, txtContactNo, txtTotalDonation, txtLastDonation, txtCity, txtEdit;
    private CircleImageView imgprofile;
    private LinearLayout layoutLastDonate, btnbackProfile;
    private String UserImage;
    private String City, Address, Pincode, Area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
        context = this;
        shared = new Shared(context);


        reference();
        init();


    }

    private void init() {
        getAPiCall();
        btnbackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, DrawerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
                i.putExtra("username", txtProfileUsername.getText().toString());
                i.putExtra("address", txtAddress.getText().toString());
                i.putExtra("City", City);
                i.putExtra("Address", Address);
                i.putExtra("Pincode", Pincode);
                i.putExtra("cno", txtContactNo.getText().toString());
                i.putExtra("image", UserImage);


                startActivity(i);
            }
        });
    }

    private void reference() {
        txtbloodGrouptype = (TextView) findViewById(R.id.txtbloodGrouptype);
        txtProfileUsername = (TextView) findViewById(R.id.txtProfileUsername);

        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtContactNo = (TextView) findViewById(R.id.txtContactNo);
        txtTotalDonation = (TextView) findViewById(R.id.txtTotalDonation);
        txtLastDonation = (TextView) findViewById(R.id.txtLastDonation);
        layoutLastDonate = (LinearLayout) findViewById(R.id.layoutLastDonate);
        imgprofile = (CircleImageView) findViewById(R.id.imgprofile);
        txtEdit = (TextView) findViewById(R.id.txtEdit);
        btnbackProfile = (LinearLayout) findViewById(R.id.btnbackProfile);

    }

    private void getAPiCall() {
        String tag_string_req = "string_req";

        String url = Config.GetUserDetail;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");


        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String Message;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int IsSuccess = jsonObject.getInt("IsSuccess");
                    if (IsSuccess == 1) {
                        Message = jsonObject.getString("Message");
                        JSONArray jsonArray = jsonObject.getJSONArray("Response");
                        for (int i1 = 0; i1 < jsonArray.length(); i1++) {
                            JSONObject c = jsonArray.getJSONObject(i1);
                            String UserId = c.getString("UserId");
                            String UserName = c.getString("UserName");
                            String ContactNo = c.getString("ContactNo");
                            City = c.getString("City");
                            Address = c.getString("Address");
                            Pincode = c.getString("Pincode");
                            String BloodType = c.getString("BloodType");
                            UserImage = c.getString("UserImage");
                            String LastDonatedDate = c.getString("LastDonatedDate");
                            if (LastDonatedDate.contentEquals("") | LastDonatedDate.contentEquals(" ")) {
                                layoutLastDonate.setVisibility(View.GONE);
                            } else {
                                layoutLastDonate.setVisibility(View.VISIBLE);
                            }

                            String TotalDonations = c.getString("TotalDonations");
                            txtProfileUsername.setText(UserName);
                            txtContactNo.setText(ContactNo);
                            txtAddress.setText(Address + ", " + c.getString("Area") + ", " + City + ", " + c.getString("Pincode"));
                            txtTotalDonation.setText(TotalDonations);
                            txtLastDonation.setText(LastDonatedDate);
                            txtbloodGrouptype.setText(BloodType);
                            LoadImageToView();


////                            shared.putString(Config.Result, new Gson().toJson(userList));
//
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"UserId\":\"" + shared.getUserId() + "\"}";
                params.put("Json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void LoadImageToView() {
        Glide.with(context).load(Config.Img + UserImage).error(getResources().getDrawable(R.drawable.ic_user_plas)).into(imgprofile);
    }

}
