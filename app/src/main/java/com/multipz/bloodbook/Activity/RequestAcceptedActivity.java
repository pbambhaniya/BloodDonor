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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.bloodbook.Adapter.CurrentBloodAdapter;
import com.multipz.bloodbook.Adapter.RequestAcceptDonerListAdapter;
import com.multipz.bloodbook.Model.CurrentBloodModel;
import com.multipz.bloodbook.Model.RequestAcceptModelClass;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.AppController;
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
import java.util.HashMap;
import java.util.Map;


public class RequestAcceptedActivity extends AppCompatActivity implements ItemClickListener, MyAsyncTask.AsyncInterface {

    private LinearLayout btnback;
    private RecyclerView listview_requestaccepted;
    private ProgressDialog pDialog;
    private RequestAcceptDonerListAdapter adapter;
    private ArrayList<RequestAcceptModelClass> userList;
    private Shared shared;
    private Context context;
    private String bloodReqID;
    private String param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_request_accepted);
        context = this;
        shared = new Shared(context);
        userList = new ArrayList<>();
        reference();
        init();

    }

    private void init() {
        bloodReqID = getIntent().getStringExtra("bloodReqID");
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void reference() {
        btnback = (LinearLayout) findViewById(R.id.btnback);
        listview_requestaccepted = (RecyclerView) findViewById(R.id.listview_requestaccepted);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        getAPiCall();
    }


    private void getAPiCall() {
        String tag_string_req = "string_req";

        String url = Config.getRequestAcceptedDonerList;
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
                                RequestAcceptModelClass currentModel = new RequestAcceptModelClass();
                                currentModel.setId(c.getString("BloodReqId"));
                                currentModel.setUserId(c.getString("UserId"));
                                currentModel.setAccepterId(c.getString("AccepterId"));
                                currentModel.setUserName(c.getString("UserName"));
                                currentModel.setContactNo(c.getString("ContactNo"));
                                currentModel.setCity(c.getString("City"));
                                currentModel.setArea(c.getString("Area"));
                                currentModel.setAddress(c.getString("Address"));
                                currentModel.setPincode(c.getString("Pincode"));
                                currentModel.setUserImage(c.getString("UserImage"));
                                currentModel.setBloodType(c.getString("BloodType"));
                                currentModel.setActionStatus(c.getString("ActionStatus"));
                                currentModel.setStatus(c.getString("Status"));
                                currentModel.setIsRequetClear(c.getString("IsRequetClear"));
                                userList.add(currentModel);
//                            shared.putString(Config.Result, new Gson().toJson(userList));
                            }
                            adapter = new RequestAcceptDonerListAdapter(context, userList);
                            listview_requestaccepted.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            listview_requestaccepted.setAdapter(adapter);
                            adapter.setClickListener(RequestAcceptedActivity.this);
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
                String param = "{\"RequestBloodId\":\"" + bloodReqID + "\"}";
                params.put("Json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void popUpAlert() {
        LayoutInflater inflater = RequestAcceptedActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_alert, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestAcceptedActivity.this);
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
    public void itemClicked(View view, int position) {
        RequestAcceptModelClass model = userList.get(position);
        if (view.getId() == R.id.btnDonated) {
            String AccepterId = model.getAccepterId();
            String ReqID = model.getId();
            PopupDonate(AccepterId, ReqID);


        } else {
            Intent i = new Intent(RequestAcceptedActivity.this, RequestAcceptChattingActivity.class);
            i.putExtra("username", model.getUserName());
            i.putExtra("address", model.getAddress());
            i.putExtra("area", model.getArea());
            i.putExtra("reqid", model.getAccepterId());
            i.putExtra("BloodReqID", model.getId());
            i.putExtra("cno", model.getContactNo());
            i.putExtra("image", model.getUserImage());
            i.putExtra("bloodtype", model.getBloodType());
            i.putExtra("city", model.getCity());

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }


    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        int IsSuccess;
        String Message, txtresponse;
        if (flag == Config.API_ReactOnSuccefullyBloodDonation) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                IsSuccess = jsonObject.getInt("IsSuccess");
                Message = jsonObject.getString("Message");
                if (IsSuccess == 1) {
                    pd.dismiss();
                    Intent i = new Intent(RequestAcceptedActivity.this, DrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } else if (IsSuccess == 0) {
                    pd.dismiss();
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public void PopupDonate(final String AccepterId, final String ReqID) {
        LayoutInflater inflater = RequestAcceptedActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.req_donor_popup, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestAcceptedActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetDonateBloodView(AccepterId, ReqID);
                dialog.dismiss();


            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    private void GetDonateBloodView(String AccepterId, String ReqID) {
        JSONObject objFinal = new JSONObject();
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("ActionStatus", "5");
            object.put("BloodRequestId", ReqID);
            object.put("AccepterId", AccepterId);

            objFinal.put("Json", object);
            param = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.ReactOnSuccefullyBloodDonation, RequestAcceptedActivity.this, param, Config.API_ReactOnSuccefullyBloodDonation);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }

    }
}
