package com.multipz.bloodbook.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.bloodbook.Adapter.DonateBloodAdapter;
import com.multipz.bloodbook.Model.DonateBloodModel;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.AppController;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.ItemClickListener;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonateBloodActivity extends AppCompatActivity implements ItemClickListener {


    private LinearLayout btnback;
    private RecyclerView rv_list_donate;
    ProgressDialog pDialog;
    ArrayList<DonateBloodModel> userList;
    private DonateBloodAdapter adapter;
    Context context;
    Shared shared;

    /***********************************Donate Blood 2****************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_donate_blood);

        context = this;
        shared = new Shared(context);
        reference();
        userList = new ArrayList<>();
        init();

    }

    private void reference() {
        btnback = (LinearLayout) findViewById(R.id.btnback);
        rv_list_donate = (RecyclerView) findViewById(R.id.rv_list_donate);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getAPiCall();
    }

    private void getAPiCall() {
        String tag_string_req = "string_req";

        String url = Config.DoneateBloodList;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

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

                        if (jsonArray.length() > 0) {
                            for (int i1 = 0; i1 < jsonArray.length(); i1++) {
                                JSONObject c = jsonArray.getJSONObject(i1);
                                DonateBloodModel donersModel = new DonateBloodModel();
                                donersModel.setId(c.getString("Id"));

                                donersModel.setBloodReqId(c.getString("BloodReqId"));
                                donersModel.setRequesterId(c.getString("RequesterId"));
                                donersModel.setUserId(c.getString("UserId"));
                                donersModel.setUserName(c.getString("UserName"));
                                donersModel.setContactNo(c.getString("ContactNo"));
                                donersModel.setCity(c.getString("City"));
                                donersModel.setArea(c.getString("Area"));
                                donersModel.setUserImage(c.getString("UserImage"));
                                donersModel.setBloodType(c.getString("BloodType"));
                                donersModel.setActionDate(c.getString("ActionDate"));
                                donersModel.setActionStatus(c.getString("ActionStatus"));
                                donersModel.setStatus(c.getString("Status"));
                                donersModel.setAddress(c.getString("Address"));
                                donersModel.setPincode(c.getString("Pincode"));

                                donersModel.setIsRequetClear(c.getString("IsRequetClear"));
                                userList.add(donersModel);
//                            shared.putString(Config.Result, new Gson().toJson(userList));
                            }
                            adapter = new DonateBloodAdapter(context, userList);
                            rv_list_donate.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            rv_list_donate.setAdapter(adapter);
                            adapter.setClickListener(DonateBloodActivity.this);
                        } else {
                            popUpAlert();
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

    public void popUpAlert() {
        LayoutInflater inflater = DonateBloodActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_alert, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(DonateBloodActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void itemClicked(View View, int position) {
        DonateBloodModel model = userList.get(position);
        Intent intent = new Intent(DonateBloodActivity.this, DonateBloodDetailActivity.class);
        intent.putExtra("uername", model.getUserName());
        intent.putExtra("bloodtype", model.getBloodType());
        intent.putExtra("contactno", model.getContactNo());
        intent.putExtra("location", model.getCity());
        intent.putExtra("area", model.getArea());
        intent.putExtra("address", model.getAddress());
        intent.putExtra("bloodReqID", model.getBloodReqId());
        intent.putExtra("ReqID", model.getRequesterId());
        intent.putExtra("userID", shared.getUserId());
        intent.putExtra("img", Config.Img + "" + model.getUserImage());
        startActivity(intent);

    }


}
