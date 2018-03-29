package com.multipz.bloodbook.Activity;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.bloodbook.Adapter.TotalHistoryAdapter;
import com.multipz.bloodbook.Model.TotalHistoryModel;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.AppController;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Constant_method;
import com.multipz.bloodbook.Utils.ItemClickListener;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TotalBloodHistory extends AppCompatActivity implements ItemClickListener {
    private LinearLayout btnback;
    private RecyclerView listview_total_blood_history;

    private ProgressDialog pDialog;
    Shared shared;
    Context context;
    private TotalHistoryAdapter adapter;
    private ArrayList<TotalHistoryModel> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_total_blood_history);
        context = this;
        shared = new Shared(context);
        userList = new ArrayList<>();
        reference();
        init();
        getAPiCall();
    }

    private void reference() {
        btnback = (LinearLayout) findViewById(R.id.btnback);
        listview_total_blood_history = (RecyclerView) findViewById(R.id.listview_total_blood_history);
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

        String url = Config.TotalDonateHistory;
        pDialog = new ProgressDialog(TotalBloodHistory.this);
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
                                TotalHistoryModel model = new TotalHistoryModel();
                                model.setBloodRequestedId(c.getString("BloodRequestedId"));
                                model.setRequestedUserId(c.getString("RequestedUserId"));
                                model.setRequestedImage(c.getString("RequestedImage"));
                                model.setRequestedAddress(c.getString("RequestedAddress"));
                                model.setRequestedDate(c.getString("RequestedDate"));
                                model.setRequestedBlood(c.getString("RequestedBlood"));
                                model.setDonetedUserName(c.getString("DonetedUserName"));
                                userList.add(model);

                            }
                            adapter = new TotalHistoryAdapter(context, userList);
                            listview_total_blood_history.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            listview_total_blood_history.setAdapter(adapter);
                            adapter.setClickListener(TotalBloodHistory.this);

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

    @Override
    public void itemClicked(View View, int position) {

    }

    public void popUpAlert() {
        LayoutInflater inflater = TotalBloodHistory.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_alert, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(TotalBloodHistory.this);
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
