package com.multipz.bloodbook.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.bloodbook.Adapter.ActiveDonersAdapter;
import com.multipz.bloodbook.Adapter.ActiveDonersFilterAdapter;
import com.multipz.bloodbook.Model.ActiveDonersModel;
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

public class ActiveDonersActivity extends AppCompatActivity implements ItemClickListener {

    Context context;
    private LinearLayout btnback;
    ProgressDialog pDialog;
    private RecyclerView listview_all_active_donors, rv_active_donor;
    private ActiveDonersAdapter adapter;
    ArrayList<ActiveDonersModel> userList, tempList;
    ActiveDonersFilterAdapter adapters;
    Shared shared;
    private ArrayList<String> bloodgroupType;

    /***********************************Active Donors Request 5*****************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_active_doners);
        context = this;
        shared = new Shared(this);
        userList = new ArrayList<>();
        tempList = new ArrayList<>();
        reference();
        init();

        getAPiCall();
    }

    private void getAPiCall() {
        String tag_string_req = "string_req";

        String url = Config.GetActiveDonnerList;
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
                                ActiveDonersModel donersModel = new ActiveDonersModel();
                                donersModel.setId(c.getString(("Id")));
                                donersModel.setUserName(c.getString("UserName"));
                                donersModel.setContactNo(c.getString("ContactNo"));
                                donersModel.setCity(c.getString("City"));
                                donersModel.setArea(c.getString("Area"));
                                donersModel.setPincode(c.getString("Pincode"));
                                donersModel.setEmail(c.getString("Email"));
                                donersModel.setUserImage(Config.Img + c.getString("UserImage"));
                                donersModel.setBloodType(c.getString("BloodType"));
                                donersModel.setAddress(c.getString("Address"));

                                donersModel.setToken(c.getString("Token"));
                                userList.add(donersModel);
//                            shared.putString(Config.Result, new Gson().toJson(userList));
                            }
                            filter("All");
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
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public void filter(String type) {
        tempList.clear();
        if (type.contentEquals("All")) {
            tempList = (ArrayList<ActiveDonersModel>) userList.clone();
        } else {
            for (ActiveDonersModel d : userList) {
                if (d.getBloodType().contentEquals(type)) {
                    tempList.add(d);
                }
            }
        }
        adapters = new ActiveDonersFilterAdapter(context, tempList);
        rv_active_donor.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_active_donor.setAdapter(adapters);
        //adapters.notifyDataSetChanged();
    }

    private void init() {
        SelectBloodGrouptype();
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void reference() {
        btnback = (LinearLayout) findViewById(R.id.btnback);
        listview_all_active_donors = (RecyclerView) findViewById(R.id.listview_all_active_donors);
        rv_active_donor = (RecyclerView) findViewById(R.id.rv_active_donor);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void SelectBloodGrouptype() {
        bloodgroupType = new ArrayList<String>();
        bloodgroupType.add("All");
        bloodgroupType.add("A+");
        bloodgroupType.add("A-");
        bloodgroupType.add("B+");
        bloodgroupType.add("B-");
        bloodgroupType.add("AB+");
        bloodgroupType.add("AB-");
        bloodgroupType.add("O+");
        bloodgroupType.add("O-");
        adapter = new ActiveDonersAdapter(getApplicationContext(), bloodgroupType);
        listview_all_active_donors.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        listview_all_active_donors.setAdapter(adapter);
        adapter.setClickListener(this);

    }

    @Override
    public void itemClicked(View View, int position) {
        filter(bloodgroupType.get(position));
    }

    public void popUpAlert() {
        LayoutInflater inflater = ActiveDonersActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_alert, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActiveDonersActivity.this);
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

}
