package com.multipz.bloodbook.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.bloodbook.Activity.DrawerActivity;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.AppController;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassword extends Fragment {
    EditText et_old_password, et_newpassword, et_current_pass;
    Button btn_accept;
    ProgressDialog pDialog;
    Context context;
    Shared shared;
    private String password, currentpassword;

    public ChangePassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        context = getActivity();
        shared = new Shared(context);
        ref(view);
        init();
        getActivity().setTitle("Change Password");
        return view;
    }

    private void ref(View view) {
        et_current_pass = (EditText) view.findViewById(R.id.et_current_pass);
        et_old_password = (EditText) view.findViewById(R.id.et_old_password);
        et_newpassword = (EditText) view.findViewById(R.id.et_newpassword);
        btn_accept = (Button) view.findViewById(R.id.btn_set_password);
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));
    }

    private void init() {
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpassword = et_current_pass.getText().toString();
                String OldPassword = et_old_password.getText().toString();
                String NewPassword = et_newpassword.getText().toString();

                if (et_current_pass.getText().toString().contentEquals("")) {
                    Toast.makeText(context, "Enter Current password", Toast.LENGTH_SHORT).show();
                } else if (et_old_password.getText().toString().contentEquals("")) {
                    Toast.makeText(context, "Enter Old Password", Toast.LENGTH_SHORT).show();
                } else if (et_newpassword.getText().toString().contentEquals("")) {
                    Toast.makeText(context, "Enter New password", Toast.LENGTH_SHORT).show();
                } else if (et_old_password.getText().toString().contentEquals(et_newpassword.getText().toString())) {
                    password = et_old_password.getText().toString();
                    getAPiCall();
                } else {
                    Toast.makeText(context, "Confirm Password does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getAPiCall() {
        String tag_string_req = "string_req";

        String url = Config.ChangePassword;
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String Message, Response;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int IsSuccess = jsonObject.getInt("IsSuccess");
                    Message = jsonObject.getString("Message");
                    if (IsSuccess == 1) {

                        Response = jsonObject.getString("Response");

                        et_current_pass.setText("");
                        et_old_password.setText("");
                        et_newpassword.setText("");
                        Intent intent = new Intent(getActivity(), DrawerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();

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
                String param = "{\"UserId\":\"" + shared.getUserId() + "\",\"OldPassword\":\"" + currentpassword + "\",\"NewPassword\":\"" + password + "\"}";
                params.put("Json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
