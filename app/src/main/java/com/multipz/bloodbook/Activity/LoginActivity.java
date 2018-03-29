package com.multipz.bloodbook.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Constant_method;
import com.multipz.bloodbook.Utils.MyAsyncTask;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {

    private EditText et_username, et_password;
    private TextView txt_forgot_pass, txt_facebook, txt_sign_up;
    private Button btn_login;
    private RelativeLayout rel_facebook;
    private String username, password;
    private String param;
    private Context context;
    private Shared shared;
    private LoginButton fb_login;
    private CallbackManager callbackManager;
    private String EmailID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        context = this;
        shared = new Shared(context);
        ref();
        init();


    }

    private void ref() {

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        txt_forgot_pass = (TextView) findViewById(R.id.txt_forgot_pass);
        txt_sign_up = (TextView) findViewById(R.id.txt_sign_up);
        btn_login = (Button) findViewById(R.id.btn_login);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }


    private void init() {

        callbackManager = CallbackManager.Factory.create();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_username.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email ID", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(et_username.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Email ID", Toast.LENGTH_SHORT).show();
                } else if (et_password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                } else {
                    username = et_username.getText().toString();
                    password = et_password.getText().toString();
                    login(username, password);
                }

            }
        });
        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });
        txt_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpForgetPassword();

            }
        });


    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void popUpForgetPassword() {
        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_forgetpassword, null);
        final EditText edtemailId = (EditText) c.findViewById(R.id.edtemailId);
        Button btnsearh_request_blood = (Button) c.findViewById(R.id.btnsearh_request_blood);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        btnsearh_request_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtemailId.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email ID", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(edtemailId.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Email ID", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    shared.setSearchType("2");
                    EmailID = edtemailId.getText().toString();
                    getParamForgetPassword(EmailID);
                }
            }
        });
        dialog.show();

    }

    private void getParamForgetPassword(String emailID) {
        param = "{\"EmailId\":\"" + emailID + "\"}";
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.ForgetPassword, LoginActivity.this, param, Config.API_GET_FORGET_PASSWORD);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void login(String username, String password) {
        JSONObject objFinal = new JSONObject();
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserName", username);
            object.put("Password", password);
            object.put("Token", FirebaseInstanceId.getInstance().getToken());
            object.put("Device", "A");
            objFinal.put("Json", object);
            param = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.VERIFY_USER, LoginActivity.this, param, Config.API_VERIFY_USER);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        int IsSuccess;
        String Message;
        if (flag == Config.API_VERIFY_USER) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                IsSuccess = jsonObject.getInt("IsSuccess");
                Message = jsonObject.getString("Message");
                if (IsSuccess == 1) {
                    pd.dismiss();
                    JSONArray Responsea = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < Responsea.length(); i++) {
                        JSONObject o = Responsea.getJSONObject(i);
                        shared.setUserId(o.getString("Id"));
                        shared.setUserName(o.getString("UserName"));
                        shared.setContactNo(o.getString("ContactNo"));
                        shared.setEmail(o.getString("Email"));
                        shared.setBirthDate(o.getString("BirthDate"));
                        shared.setUserImage(o.getString("UserImage"));
                        shared.setBloodId(o.getString("BloodId"));
                        shared.setArea(o.getString("Area"));
                        shared.setAddress(o.getString("Address"));
                        shared.setCity(o.getString("City"));
                        shared.setState(o.getString("State"));
                        shared.setPincode(o.getString("Pincode"));
                    }
                    shared.setlogin(true);
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (IsSuccess == 0) {
                    pd.dismiss();
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                }
                Log.e("Responce", jsonObject.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (flag == Config.API_GET_FORGET_PASSWORD) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                IsSuccess = jsonObject.getInt("IsSuccess");
                Message = jsonObject.getString("Message");
                if (IsSuccess == 0) {
                    pd.dismiss();
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                } else if (IsSuccess == 1) {
                    pd.dismiss();
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                } else if (IsSuccess == 2) {
                    pd.dismiss();
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
