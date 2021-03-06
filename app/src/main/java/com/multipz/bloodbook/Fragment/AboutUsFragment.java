package com.multipz.bloodbook.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.AppController;
import com.multipz.bloodbook.Utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.GraphRequest.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {
    private WebView wb_about;
    private ProgressDialog pDialog;

    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        getActivity().setTitle("About Us");
        reference(view);
        init();
        return view;
    }

    private void init() {

        WebSettings settings = wb_about.getSettings();
        settings.setJavaScriptEnabled(true);
        wb_about.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        getPageContent();
    }

    private void getPageContent() {

// Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = Config.TermsAndPrivacyPolicy;

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.hide();
                String Message;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int IsSuccess = jsonObject.getInt("IsSuccess");
                    if (IsSuccess == 1) {
                        Message = jsonObject.getString("Message");
                        JSONArray jsonArray = jsonObject.getJSONArray("Response");
                        for (int i1 = 0; i1 < jsonArray.length(); i1++) {
                            JSONObject c = jsonArray.getJSONObject(i1);
                            String pageid = c.getString("PageId");
                            String PageName = c.getString("PageName");
                            String PageContent = c.getString("PageContent");

                            wb_about.loadData(String.format("%s", PageContent), "text/html", "UTF-8");
                        }
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
                String param = "{\"PageName\":\"A\"}";
                params.put("Json", param);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void reference(View view) {
        wb_about = (WebView) view.findViewById(R.id.wb_about);
    }
}
