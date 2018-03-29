package com.multipz.bloodbook.Fragment;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multipz.bloodbook.Activity.SaveLaterAcceptActivity;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Admin on 02-12-2017.
 */

public class FeedBackFragment extends Fragment {

    int CurrentId = 0;

    ImageView imgHappy, imgCurious, imgMeh, imgConfused, imgUpset;
    TextView txtHappy, txtCurious, txtMeh, txtConfused, txtUpset;

    Button btn;
    EditText edt;

    Shared shared;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);

        getActivity().setTitle("Feedback");

        shared = new Shared(getActivity());

        imgHappy = rootView.findViewById(R.id.img_happy);
        imgCurious = rootView.findViewById(R.id.img_curious);
        imgMeh = rootView.findViewById(R.id.img_meh);
        imgConfused = rootView.findViewById(R.id.img_confused);
        imgUpset = rootView.findViewById(R.id.img_upset);

        txtHappy = rootView.findViewById(R.id.txt_happy);
        txtConfused = rootView.findViewById(R.id.txt_confused);
        txtCurious = rootView.findViewById(R.id.txt_curious);
        txtMeh = rootView.findViewById(R.id.txt_meh);
        txtUpset = rootView.findViewById(R.id.txt_upset);

        edt = rootView.findViewById(R.id.edtMessage);
        btn = rootView.findViewById(R.id.btnSubmit);

        disableAll();

        imgHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableAll();
                setCurrentAsSelected(imgHappy);
                CurrentId = 1;
            }
        });

        imgCurious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableAll();
                setCurrentAsSelected(imgCurious);
                CurrentId = 2;

            }
        });

        imgMeh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableAll();
                setCurrentAsSelected(imgMeh);
                CurrentId = 3;
            }
        });

        imgConfused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableAll();
                setCurrentAsSelected(imgConfused);
                CurrentId = 4;
            }
        });

        imgUpset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableAll();
                setCurrentAsSelected(imgUpset);
                CurrentId = 5;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CurrentId == 0) {
                    popUp("Please select a smiley that best describes how you feel about the app");
                    return;
                }
                if (edt.getText().toString().contentEquals("")) {
                    popUp("Please write a message");
                    return;
                }

                new SendFeedBack().execute(edt.getText().toString(), Config.Feedback);
            }
        });

        return rootView;

    }

    class SendFeedBack extends AsyncTask<String, Integer, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(getActivity(), "Wait", "Loading");
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserId", shared.getUserId());
                jsonObject.put("FeelingId", CurrentId + "");
                jsonObject.put("Message", strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            FormBody.Builder formBuilder = new FormBody.Builder().add("json", jsonObject.toString());
            RequestBody formBody = formBuilder.build();
            Request request = new Request.Builder().url(strings[1]).post(formBody).build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            JSONObject jsonObject = null;
            try {
                edt.setText("");
                disableAll();
                jsonObject = new JSONObject(s);
                int IsSuccess = jsonObject.getInt("IsSuccess");
                String Message = jsonObject.getString("Message");
                popUp(Message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void popUp(String message) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View c = inflater.inflate(R.layout.save_later_popup, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.cancel);

        ((TextView) c.findViewById(R.id.head_desc)).setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(c);

        final AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        cancel.setVisibility(View.GONE);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void setCurrentAsSelected(ImageView img) {

        switch (img.getId()) {
            case R.id.img_happy:
                imgHappy.setImageDrawable(getResources().getDrawable(R.drawable.happy));
                txtHappy.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.img_curious:
                imgCurious.setImageDrawable(getResources().getDrawable(R.drawable.curious));
                txtCurious.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.img_meh:
                imgMeh.setImageDrawable(getResources().getDrawable(R.drawable.meh));
                txtMeh.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.img_confused:
                imgConfused.setImageDrawable(getResources().getDrawable(R.drawable.confused_select));
                txtConfused.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.img_upset:
                imgUpset.setImageDrawable(getResources().getDrawable(R.drawable.upset));
                txtUpset.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }


    void disableAll() {

        imgHappy.setImageDrawable(getResources().getDrawable(R.drawable.happy_unselect));
        imgCurious.setImageDrawable(getResources().getDrawable(R.drawable.curious_unselect));
        imgMeh.setImageDrawable(getResources().getDrawable(R.drawable.meh_unselect));
        imgConfused.setImageDrawable(getResources().getDrawable(R.drawable.confused));
        imgUpset.setImageDrawable(getResources().getDrawable(R.drawable.upset_unselect));

        txtHappy.setTextColor(getResources().getColor(R.color.Text_color));
        txtConfused.setTextColor(getResources().getColor(R.color.Text_color));
        txtCurious.setTextColor(getResources().getColor(R.color.Text_color));
        txtMeh.setTextColor(getResources().getColor(R.color.Text_color));
        txtUpset.setTextColor(getResources().getColor(R.color.Text_color));
    }

}
