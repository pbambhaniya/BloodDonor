package com.multipz.bloodbook.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.bloodbook.Activity.DonateMoney;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.AppController;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Constant_method;
import com.multipz.bloodbook.Utils.MyAsyncTask;
import com.multipz.bloodbook.Utils.Shared;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonateMoneyFragment extends Fragment implements PaymentResultListener, MyAsyncTask.AsyncInterface {


    private static final String TAG = DonateMoney.class.getSimpleName();
    Button button;
    TextView displayInteger;
    int count = 1;
    private ImageView btnminus, btnplus;
    private Shared shared;
    private Context context;
    private String param;
    private String amount;
    private ProgressDialog pDialog;
    int maxclicks = 5;
    int minClick = 1;

    public DonateMoneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donate_money, container, false);
        Checkout.preload(getActivity());
        getActivity().setTitle("Donate Money");
        context = this.getActivity();
        shared = new Shared(context);
        button = (Button) view.findViewById(R.id.btn_pay);
        reference(view);
        init();
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = displayInteger.getText().toString();

                startPayment();
            }
        });

        return view;
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(getActivity(), "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            getParamForDonateMoney(amount, "");


        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(getActivity(), "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }

    }

    private void getParamForDonateMoney(final String amount, final String TransactionId) {
        String tag_string_req = "string_req";

        String url = Config.TotalDonateHistory;
        pDialog = new ProgressDialog(getActivity());
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
                    Message = jsonObject.getString("Message");
                    if (IsSuccess == 1) {
                        Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
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
                JSONObject object = new JSONObject();
                try {
                    object.put("UserId", shared.getUserId());
                    object.put("TransactionId", "");
                    object.put("Amount", amount);
                    param = object.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("Json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final DonateMoneyFragment activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(displayInteger.getText().toString()) * 100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "multipz.paresh@gmail.com");
            preFill.put("contact", "8758689113");

            options.put("prefill", preFill);

            co.open(getActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    private void reference(View view) {
        btnminus = (ImageView) view.findViewById(R.id.btnminus);
        btnplus = (ImageView) view.findViewById(R.id.btnplus);
        displayInteger = (TextView) view.findViewById(R.id.txtCurrancy);
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));

    }

    private void init() {
        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseInteger();
            }
        });
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseInteger();
            }
        });
    }


    public void increaseInteger() {

        if (count == maxclicks) {
            btnplus.setEnabled(false);
        } else if (count <= 5) {
            btnplus.setEnabled(true);
            count = count + 1;
            display(count);
        } else {
            btnplus.setEnabled(true);
        }

    }

    public void decreaseInteger() {

        if (count == minClick) {
            btnminus.setEnabled(false);
        } else if (count >= 1) {
            btnminus.setEnabled(true);
            count = count - 1;
            display(count);
        } else {
            btnminus.setEnabled(true);

        }

    }

    private void display(int number) {

        displayInteger.setText("" + number);
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        int IsSuccess;
        String Message, txtresponse;
        if (flag == Config.API_REACT_ON_NOTIFICATION) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                IsSuccess = jsonObject.getInt("IsSuccess");
                Message = jsonObject.getString("Message");
                if (IsSuccess == 1) {
                    pd.dismiss();
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}

