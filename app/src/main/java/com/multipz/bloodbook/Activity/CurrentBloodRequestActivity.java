package com.multipz.bloodbook.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.bloodbook.Adapter.CurrentBloodAdapter;
import com.multipz.bloodbook.Model.CurrentBloodModel;
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

public class CurrentBloodRequestActivity extends AppCompatActivity implements ItemClickListener {


    ProgressDialog pDialog;
    private LinearLayout btnback;
    private RecyclerView listview_current_blood_req;
    public ArrayList<CurrentBloodModel> userList;
    public CurrentBloodAdapter adapter;
    Context context;
    Shared shared;

    /***********************************Current Blood Request 3*****************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_current_blood_request);
        context = this;
        shared = new Shared(context);
        userList = new ArrayList<>();
        reference();
        init();

        getAPiCall();
    }

    private void init() {

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void reference() {
        btnback = (LinearLayout) findViewById(R.id.btnback);
        listview_current_blood_req = (RecyclerView) findViewById(R.id.listview_current_blood_req);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }


    private void getAPiCall() {
        String tag_string_req = "string_req";

        String url = Config.currentblood;
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
                                CurrentBloodModel currentModel = new CurrentBloodModel();
                                currentModel.setId(c.getString("Id"));
                                currentModel.setUserId(c.getString("UserId"));
                                currentModel.setUserName(c.getString("UserName"));
                                currentModel.setContactNo(c.getString("ContactNo"));
                                currentModel.setCity(c.getString("City"));
                                currentModel.setArea(c.getString("Area"));
                                currentModel.setAddress(c.getString("Address"));
                                currentModel.setPincode(c.getString("Pincode"));
                                currentModel.setUserImage(c.getString("UserImage"));
                                currentModel.setBloodType(c.getString("BloodType"));
                                currentModel.setRequireDate(c.getString("RequireDate"));
                                currentModel.setStatus(c.getString("Status"));
                                currentModel.setIsRequetClear(c.getString("IsRequetClear"));
                                userList.add(currentModel);
//                            shared.putString(Config.Result, new Gson().toJson(userList));
                            }
                            adapter = new CurrentBloodAdapter(context, userList);
                            listview_current_blood_req.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            listview_current_blood_req.setAdapter(adapter);
                            adapter.setClickListener(CurrentBloodRequestActivity.this);
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
        LayoutInflater inflater = CurrentBloodRequestActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_alert, null);

        LinearLayout btn_ok = (LinearLayout) c.findViewById(R.id.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(CurrentBloodRequestActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void itemClicked(View View, int position) {
        CurrentBloodModel model = userList.get(position);
        Intent i = new Intent(CurrentBloodRequestActivity.this, RequestAcceptedActivity.class);
        i.putExtra("bloodReqID", model.getId());
        startActivity(i);
    }
}

