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
import android.widget.Toast;

import com.multipz.bloodbook.Adapter.GetRequestAcceptList;
import com.multipz.bloodbook.Adapter.TotalHistoryAdapter;
import com.multipz.bloodbook.Model.DonateBloodModel;
import com.multipz.bloodbook.Model.TotalHistoryModel;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Constant_method;
import com.multipz.bloodbook.Utils.ItemClickListener;
import com.multipz.bloodbook.Utils.MyAsyncTask;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetRequestAcceptedListActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface, ItemClickListener {

    private LinearLayout btnback;
    private RecyclerView listview_request_Accept_detail;
    private String param;
    private Context context;
    private Shared shared;
    private ArrayList<DonateBloodModel> list;
    private GetRequestAcceptList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_get_request_accepted_list);
        context = this;
        shared = new Shared(context);
        list = new ArrayList<>();
        reference();
        init();
    }

    private void init() {
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getRequestAcceptList();

    }

    private void getRequestAcceptList() {
        param = "{\"UserId\":\"" + shared.getUserId() + "\"}";
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.GetRequestAccepterList, GetRequestAcceptedListActivity.this, param, Config.API_GetRequestAccepterList);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void reference() {
        btnback = (LinearLayout) findViewById(R.id.btnback);
        listview_request_Accept_detail = (RecyclerView) findViewById(R.id.listview_request_Accept_detail);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {

        int IsSuccess;
        String Message;
        if (flag == Config.API_GetRequestAccepterList) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                IsSuccess = jsonObject.getInt("IsSuccess");
                if (IsSuccess == 1) {
                    pd.dismiss();
                    Message = jsonObject.getString("Message");
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    if (jsonArray.length() > 0) {
                        for (int i1 = 0; i1 < jsonArray.length(); i1++) {
                            JSONObject c = jsonArray.getJSONObject(i1);
                            DonateBloodModel model = new DonateBloodModel();
                            model.setId(c.getString("Id"));
                            model.setBloodReqId(c.getString("BloodReqId"));
                            model.setRequesterId(c.getString("RequesterId"));
                            model.setUserId(c.getString("UserId"));
                            model.setUserName(c.getString("UserName"));
                            model.setContactNo(c.getString("ContactNo"));
                            model.setCity(c.getString("City"));
                            model.setArea(c.getString("Area"));
                            model.setAddress(c.getString("Address"));
                            model.setPincode(c.getString("Pincode"));
                            model.setUserImage(c.getString("UserImage"));
                            model.setBloodType(c.getString("BloodType"));
                            model.setActionDate(c.getString("ActionDate"));
                            model.setStatus(c.getString("Status"));
                            model.setIsRequetClear(c.getString("IsRequetClear"));
                            list.add(model);
                        }
                        adapter = new GetRequestAcceptList(context, list);
                        listview_request_Accept_detail.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        listview_request_Accept_detail.setAdapter(adapter);
                        adapter.setClickListener(this);
                    } else {
                        popUpAlert();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void popUpAlert() {
        LayoutInflater inflater = GetRequestAcceptedListActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_alert, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(GetRequestAcceptedListActivity.this);
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
        DonateBloodModel model = list.get(position);
        Intent i = new Intent(GetRequestAcceptedListActivity.this, RequestAcceptChattingActivity.class);
        i.putExtra("username", model.getUserName());
        i.putExtra("address", model.getAddress() + "," + model.getArea() + "," + model.getCity() + "," + model.getPincode());
        i.putExtra("cno", model.getContactNo());
        i.putExtra("image", model.getUserImage());
        i.putExtra("reqid", model.getRequesterId());
        i.putExtra("BloodReqID", model.getBloodReqId());
        i.putExtra("bloodtype", model.getBloodType());
        i.putExtra("city", model.getCity());

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
