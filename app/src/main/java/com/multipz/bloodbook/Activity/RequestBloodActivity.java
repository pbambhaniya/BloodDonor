package com.multipz.bloodbook.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.bloodbook.Adapter.RequestBloodAdapter;
import com.multipz.bloodbook.Adapter.SelectBloodTypeAdapter;
import com.multipz.bloodbook.Model.BloodRequestModelClass;
import com.multipz.bloodbook.Model.BloodType;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Constant_method;
import com.multipz.bloodbook.Utils.MyAsyncTask;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class RequestBloodActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface, LocationListener {

    private LinearLayout toolbar, btnback;
    private TextView txtnotifyAll;
    private RecyclerView listview_request_blood, listviewSearch_req_blood;
    private Button btnsearh_request_blood;
    private SelectBloodTypeAdapter adapter;
    private RadioGroup radioGroupSearch_req_blood;
    private RadioButton radiobtnMyAddress, radiobtnPincode, radiobtnMyLocation;
    private ArrayList<BloodType> bloodgroupType;
    private ArrayList<BloodRequestModelClass> listBloodRequest;
    private RequestBloodAdapter requestBloodAdapter;
    private LinearLayout cardList;
    private String param, pincode;
    private Context context;
    private Shared shared;
    LocationManager locationManager;
    String provider;
    private String latitute = "", longitute = "";
    private RadioButton rb;
    private LinearLayout layoutNotifyAll;
    private int NotifyAllValue = 0;

    /***********************************Request Blood 1****************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_request_blood);
        context = this;
        shared = new Shared(context);
        reference();
        init();

    }

    private void reference() {
        layoutNotifyAll = (LinearLayout) findViewById(R.id.layoutNotifyAll);
        toolbar = (LinearLayout) findViewById(R.id.toolbar);
        btnback = (LinearLayout) findViewById(R.id.btnback);
        txtnotifyAll = (TextView) findViewById(R.id.txtnotifyAll);
        listview_request_blood = (RecyclerView) findViewById(R.id.listview_request_blood);
        btnsearh_request_blood = (Button) findViewById(R.id.btnsearch_request_blood);
        radioGroupSearch_req_blood = (RadioGroup) findViewById(R.id.radioGroupSearch_req_blood);
        radiobtnMyAddress = (RadioButton) findViewById(R.id.radiobtnMyAddress);
        radiobtnPincode = (RadioButton) findViewById(R.id.radiobtnPincode);
        radiobtnMyLocation = (RadioButton) findViewById(R.id.radiobtnMyLocation);
        cardList = (LinearLayout) findViewById(R.id.cardList);
        listviewSearch_req_blood = (RecyclerView) findViewById(R.id.listviewSearch_req_blood);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void init() {
        //getLatitudeLOngitute();

        SelectBloodGrouptype();

        radioGroupSearch_req_blood.clearCheck();
        radioGroupSearch_req_blood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    //Toast.makeText(RequestBloodActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    if (rb.getText().toString().matches("My Address")) {
                        shared.setSearchType("1");
                    } else if (rb.getText().toString().matches("Pincode")) {
                        //popUpCouponCode();
                    } else if (rb.getText().toString().trim().matches("My Location")) {
                        shared.setSearchType("3");
                        if (!checkPermissionLocation()) {
                            ActivityCompat.requestPermissions(RequestBloodActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 144);
                        }
                    }
                }
            }
        });

        findViewById(R.id.radiobtnPincode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpCouponCode();
            }
        });

        btnsearh_request_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radioGroupSearch_req_blood.getCheckedRadioButtonId() != -1) {
                    NotifyAllValue = 0;
                    getParamForActivieDonors();
                } else {
                    Toast.makeText(context, "Select Search Type", Toast.LENGTH_SHORT).show();
                }


            }
        });

        txtnotifyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radioGroupSearch_req_blood.getCheckedRadioButtonId() != -1) {
                    NotifyAllValue = 1;
                    getParamForActivieDonors();

                } else {
                    Toast.makeText(context, "Select Search Type", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getLatitudeLOngitute();
    }

    public boolean checkPermissionLocation() {
        int result = ContextCompat.checkSelfPermission(RequestBloodActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == 144) {
            if (grantResults.length == 1 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED) {

                // Success Stuff here

            } else {
                Toast.makeText(context, "Need permission for location search", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void getLatitudeLOngitute() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);
        if (provider != null && !provider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 15000, 1, this);
            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "No Location found, Please open your GPS", Toast.LENGTH_SHORT).show();
        }
    }

    private void getParamForActivieDonors() {

        JSONObject objFinal = new JSONObject();
        JSONObject object = null;

        if (shared.getSearchType().contentEquals("1")) {
            try {
                object = new JSONObject();
                object.put("BloodType", shared.getBloodTypeId());
                object.put("UserId", shared.getUserId());
                object.put("SearchType", shared.getSearchType());
                object.put("Pincode", "");
                object.put("Lat", "");
                object.put("Lng", "");
                if (NotifyAllValue == 1) {
                    object.put("SearchFor", 1);
                } else {
                    object.put("SearchFor", 0);
                }

                param = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (shared.getSearchType().contentEquals("2")) {
            try {
                object = new JSONObject();
                object.put("BloodType", shared.getBloodTypeId());
                object.put("UserId", shared.getUserId());
                object.put("SearchType", shared.getSearchType());
                object.put("Pincode", pincode);
                object.put("Lat", "");
                object.put("Lng", "");
                if (NotifyAllValue == 1) {
                    object.put("SearchFor", 1);
                } else {
                    object.put("SearchFor", 0);
                }
                param = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (shared.getSearchType().contentEquals("3")) {

            try {
                object = new JSONObject();
                object.put("BloodType", shared.getBloodTypeId());
                object.put("UserId", shared.getUserId());
                object.put("SearchType", shared.getSearchType());
                object.put("Pincode", "");
                object.put("Lat", latitute);
                object.put("Lng", longitute);
                if (NotifyAllValue == 1) {
                    object.put("SearchFor", 1);
                } else {
                    object.put("SearchFor", 0);
                }
                param = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BLOOD_REQUEST, RequestBloodActivity.this, param, Config.API_BLOOD_REQUEST);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void SelectBloodGrouptype() {
        bloodgroupType = new ArrayList<BloodType>();

        bloodgroupType.add(new BloodType(1, "A+"));
        bloodgroupType.add(new BloodType(3, "B+"));
        bloodgroupType.add(new BloodType(7, "AB+"));
        bloodgroupType.add(new BloodType(5, "O+"));
        bloodgroupType.add(new BloodType(2, "A-"));
        bloodgroupType.add(new BloodType(4, "B-"));
        bloodgroupType.add(new BloodType(8, "AB-"));
        bloodgroupType.add(new BloodType(6, "O-"));

        adapter = new SelectBloodTypeAdapter(getApplicationContext(), bloodgroupType);
        listview_request_blood.setLayoutManager(new GridLayoutManager(this, 4));
        listview_request_blood.setAdapter(adapter);

    }

    public void popUpCouponCode() {
        LayoutInflater inflater = RequestBloodActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.seach_pincode_req_blood, null);
        final EditText edtSearch_pincode = (EditText) c.findViewById(R.id.edtSearch_pincode);
        Button btnsearh_request_blood = (Button) c.findViewById(R.id.btnsearh_request_blood);

        AlertDialog.Builder builder = new AlertDialog.Builder(RequestBloodActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        c.findViewById(R.id.btncancel_request_blood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnsearh_request_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtSearch_pincode.getText().toString().equals("")) {
                    Toast.makeText(RequestBloodActivity.this, "Please Enter CouponCode", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    shared.setSearchType("2");
                    pincode = edtSearch_pincode.getText().toString();
                    getParamForActivieDonors();
                }
            }
        });

    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {

        int IsSuccess;
        String Message;
        if (flag == Config.API_BLOOD_REQUEST) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                IsSuccess = jsonObject.getInt("IsSuccess");
                Message = jsonObject.getString("Message");
                if (IsSuccess == 1) {
                    cardList.setVisibility(View.VISIBLE);
                    pd.dismiss();
                    JSONArray array = jsonObject.getJSONArray("Response");
                    listBloodRequest = new ArrayList<>();
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            BloodRequestModelClass modelClass = new BloodRequestModelClass();
                            modelClass.setId(o.getString("Id"));
                            modelClass.setUserName(o.getString("UserName"));
                            modelClass.setContactNo(o.getString("ContactNo"));
                            modelClass.setCity(o.getString("City"));
                            modelClass.setArea(o.getString("Area"));
                            modelClass.setPincode(o.getString("Pincode"));
                            modelClass.setEmail(o.getString("Email"));
                            modelClass.setAddress(o.getString("Address"));
                            modelClass.setUserImage(o.getString("UserImage"));
                            modelClass.setBloodType(o.getString("BloodType"));
                            modelClass.setToken(o.getString("Token"));
                            modelClass.setDistance(o.getString("Distance"));
                            listBloodRequest.add(modelClass);
                        }
                        requestBloodAdapter = new RequestBloodAdapter(getApplicationContext(), listBloodRequest);
                        listviewSearch_req_blood.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,
                                false));
                        listviewSearch_req_blood.setAdapter(requestBloodAdapter);
                        layoutNotifyAll.setVisibility(View.VISIBLE);

                        if (NotifyAllValue == 1) {
                            NotifyAll();
                        }

                    } else {
                        layoutNotifyAll.setVisibility(View.GONE);
                        requestBloodAdapter = new RequestBloodAdapter(getApplicationContext(), listBloodRequest);
                        listviewSearch_req_blood.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,
                                false));
                        listviewSearch_req_blood.setAdapter(requestBloodAdapter);
                        popUpAlert();
                    }

                } else {
                    pd.dismiss();
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                }
                Log.e("Responce", jsonObject.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void popUpAlert() {
        LayoutInflater inflater = RequestBloodActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_alert, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestBloodActivity.this);
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

    public void NotifyAll() {
        LayoutInflater inflater = RequestBloodActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.notification_dialog, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestBloodActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        latitute = "" + location.getLatitude();
        longitute = "" + location.getLongitude();


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
