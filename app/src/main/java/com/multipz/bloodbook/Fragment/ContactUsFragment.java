package com.multipz.bloodbook.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.bloodbook.Activity.DrawerActivity;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.AppController;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Constant_method;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.GraphRequest.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    LinearLayout btnbackEdit;
    EditText edit_email, edit_message;
    RelativeLayout rel_submit;
    Context context;
    Shared shared;
    String email, des;
    ProgressDialog pDialog;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        getActivity().setTitle("Contact Us");
        context = getActivity();
        shared = new Shared(context);
        ref(view);
        init();
        return view;
    }

    private void ref(View view) {
        btnbackEdit = (LinearLayout) view.findViewById(R.id.btnbackEdit);
        edit_email = (EditText) view.findViewById(R.id.edit_email);
        edit_message = (EditText) view.findViewById(R.id.edit_message);
        rel_submit = (RelativeLayout) view.findViewById(R.id.rel_submit);
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));


    }

    private void init() {

        rel_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edit_email.getText().toString();
                String message = edit_message.getText().toString();

                if (email.contentEquals("")) {
                    Toast.makeText(getActivity(), "Enter Email Id", Toast.LENGTH_SHORT).show();
                } else if (message.contentEquals("")) {
                    Toast.makeText(getActivity(), "Enter Description", Toast.LENGTH_SHORT).show();
                } else {
                    if (Constant_method.checkConn(context)) {
                        getAPiCall();

                    } else {
                        Toast.makeText(context, "Check connection", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    private void getAPiCall() {

// Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = Config.Contactus;
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.hide();
                String Message, Response;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int IsSuccess = jsonObject.getInt("IsSuccess");
                    Message = jsonObject.getString("Message");
                    Response = jsonObject.getString("Response");
                    if (IsSuccess == 1) {
                        edit_email.setText("");
                        edit_message.setText("");
                        Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), DrawerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                email = edit_email.getText().toString();
                des = edit_message.getText().toString();
                String param = "{\"UserId\":\"" + shared.getUserId() + "\",\"Email\":\"" + email + "\",\"Description\":\"" + des + "\"}\n";
                params.put("Json", param);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}


