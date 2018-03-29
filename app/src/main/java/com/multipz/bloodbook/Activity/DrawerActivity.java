package com.multipz.bloodbook.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.multipz.bloodbook.Fragment.AboutUsFragment;
import com.multipz.bloodbook.Fragment.BloodTypeCompabilityFragment;
import com.multipz.bloodbook.Fragment.ChangePassword;
import com.multipz.bloodbook.Fragment.ContactUsFragment;
import com.multipz.bloodbook.Fragment.DeshboardFragment;
import com.multipz.bloodbook.Fragment.DonateMoneyFragment;
import com.multipz.bloodbook.Fragment.FeedBackFragment;
import com.multipz.bloodbook.Fragment.PrivacyFragment;
import com.multipz.bloodbook.Fragment.TermsConditionFragment;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.AppController;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class DrawerActivity extends AppCompatActivity {
    LinearLayout lnr_home, lnr_blood_type, lnr_donate_money, lnr_terms_condition, lnr_privacy, lnr_contact_us, lnr_about_us, lnr_log_out, lnr_change_password;
    private NavigationView navigationView;
    DrawerLayout drawer;
    FragmentTransaction transaction;
    private Shared shared;
    private Context context;
    private CircleImageView imageuserr;
    private TextView txtUsername, txtAddress;
    private ImageView select_edit_profile;
    private String Area, city, State, Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        context = this;
        shared = new Shared(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // navigationView.setNavigationItemSelectedListener(this);

        imageuserr = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imageuserr);
        txtUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtUsername);
        txtAddress = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtAddress);
        /*String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", "" + token);*/

        select_edit_profile = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.select_edit_profile);
        String img = Config.Img + "" + shared.getUserImage();
        Glide.with(context).load(img).error(getResources().getDrawable(R.drawable.ic_user_plas)).into(imageuserr);
        txtUsername.setText(shared.getUserName());
        txtAddress.setText(shared.getAddress() + "," + shared.getArea() + "," + shared.getCity() + "," + shared.getState());

        lnr_home = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.lnr_home);
        lnr_blood_type = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.lnr_blood_type);
        lnr_donate_money = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.lnr_donate_money);
        lnr_terms_condition = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.lnr_terms_condition);
        lnr_privacy = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.lnr_privacy);
        lnr_contact_us = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.lnr_contact_us);
        lnr_about_us = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.lnr_about_us);
        lnr_log_out = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.lnr_log_out);
        lnr_change_password = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.lnr_change_pass);
        //lnr_change_password = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.lnr_change_password);


        select_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DrawerActivity.this, ProfileActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new DeshboardFragment()).commit();
            }
        });
        lnr_blood_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new BloodTypeCompabilityFragment()).commit();
            }
        });
        lnr_donate_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new DonateMoneyFragment()).commit();
            }
        });
        lnr_terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new TermsConditionFragment()).commit();
            }
        });


        lnr_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new ChangePassword()).commit();
            }
        });
        lnr_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new PrivacyFragment()).commit();
            }
        });
        lnr_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new ContactUsFragment()).commit();
            }
        });
        lnr_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new AboutUsFragment()).commit();
            }
        });


        navigationView.getHeaderView(0).findViewById(R.id.lnr_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new FeedBackFragment()).commit();
            }
        });

        navigationView.getHeaderView(0).findViewById(R.id.lnr_share_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Blood Donor Pro");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n via Blood Donor Pro";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

       /* lnr_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new AboutUsFragment()).commit();
            }
        });*/

        lnr_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                getAPiCall();

            }
        });

      /*  imageuserr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DrawerActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });*/

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new DeshboardFragment()).commit();

        ratingPopUp();
    }

    private void ratingPopUp() {
        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(0) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setMessage("If you find Blood Donor Pro useful, would you mind taking a moment to rate it? It won't take more than a minute. Thanks for your support!")
                .setTitle("Blood Donor Pro")
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(DrawerActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                popFinished();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void getAPiCall() {

// Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = Config.Logout;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Message, Response;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int IsSuccess = jsonObject.getInt("IsSuccess");
                    Message = jsonObject.getString("Message");
                    Response = jsonObject.getString("Response");
                    if (IsSuccess == 1) {
                        popUpAlert();
//                        Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();

                    } else if (IsSuccess == 0) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            popFinished();
            // super.onBackPressed();

        }
    }


    private void popFinished() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DrawerActivity.this);
        alertDialog.setTitle("Blood Donor Pro");
        alertDialog.setMessage("Are you sure you want to exit?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    public void popUpAlert() {
        LayoutInflater inflater = DrawerActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.logout_dialog, null);

        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                shared.setlogin(false);
                shared.logout();
                Intent i = new Intent(DrawerActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
