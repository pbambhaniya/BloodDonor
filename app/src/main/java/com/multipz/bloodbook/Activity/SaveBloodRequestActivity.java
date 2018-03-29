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
import com.multipz.bloodbook.Adapter.SaveBloodAdapter;
import com.multipz.bloodbook.Model.SaveBloodModel;
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

public class SaveBloodRequestActivity extends AppCompatActivity implements ItemClickListener {

    ProgressDialog pDialog;
    private LinearLayout btnback;
    private RecyclerView rv_save_blood;
    ArrayList<SaveBloodModel> userList;
    public SaveBloodAdapter adapter;
    Context context;
    Shared shared;

    /***********************************Save Blood Request 4*****************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_save_blood_request);
        context = this;
        shared = new Shared(context);
        userList = new ArrayList<>();
        referene();
        init();
        getAPiCall();
    }

    private void referene() {
        btnback = (LinearLayout) findViewById(R.id.btnback);
        rv_save_blood = (RecyclerView) findViewById(R.id.listview_request_later);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void init() {

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getAPiCall() {
        String tag_string_req = "string_req";

        String url = Config.SaveBlood;
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
                                SaveBloodModel saveModel = new SaveBloodModel();
                                saveModel.setId(c.getString("Id"));
                                saveModel.setBloodReqId(c.getString("BloodReqId"));
                                saveModel.setUserId(c.getString("UserId"));
                                saveModel.setUserName(c.getString("UserName"));
                                saveModel.setContactNo(c.getString("ContactNo"));
                                saveModel.setCity(c.getString("City"));
                                saveModel.setUserImage(c.getString("UserImage"));
                                saveModel.setBloodType(c.getString("BloodType"));
                                saveModel.setStatus(c.getString("Status"));
                                saveModel.setArea(c.getString("Area"));
                                saveModel.setAddress(c.getString("Address"));
                                saveModel.setPincode(c.getString("Pincode"));
                                saveModel.setIsRequetClear(c.getString("IsRequetClear"));
                                userList.add(saveModel);
//                            shared.putString(Config.Result, new Gson().toJson(userList));
                            }
                            adapter = new SaveBloodAdapter(context, userList);
                            rv_save_blood.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            rv_save_blood.setAdapter(adapter);
                            adapter.setClickListener(SaveBloodRequestActivity.this);
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
        LayoutInflater inflater = SaveBloodRequestActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_alert, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(SaveBloodRequestActivity.this);
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
        SaveBloodModel model = userList.get(position);
        Intent intent = new Intent(SaveBloodRequestActivity.this, SaveLaterAcceptActivity.class);
        intent.putExtra("username", model.getUserName());
        intent.putExtra("mobileno", model.getContactNo());
        intent.putExtra("bloodtype", model.getBloodType());
        intent.putExtra("address", model.getAddress() + "," + model.getArea() + "," + model.getCity() + "," + model.getPincode());
        intent.putExtra("bloodReqID", model.getBloodReqId());
        intent.putExtra("UserID", model.getUserId());
        intent.putExtra("img", Config.Img + "" + model.getUserImage());
        startActivity(intent);
    }
}
