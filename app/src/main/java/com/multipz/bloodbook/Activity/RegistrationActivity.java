package com.multipz.bloodbook.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.multipz.bloodbook.Adapter.SelectBloodTypeAdapter;
import com.multipz.bloodbook.Model.BloodType;

import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.AppController;
import com.multipz.bloodbook.Utils.Application;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.Constant_method;
import com.multipz.bloodbook.Utils.FilePath;
import com.multipz.bloodbook.Utils.ItemClickListener;
import com.multipz.bloodbook.Utils.MyAsyncTask;
import com.multipz.bloodbook.Utils.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface, ItemClickListener {

    private EditText edt_username, edtpassword, edtconfirmpassword, edtcontactno, edtemailId, edtaddress, edtarea, edtcity, edtstate, edtpincodeno;
    private TextView edtdob;
    private RecyclerView list_item_blood_group;
    private int mMonth, mYear, date;
    private SelectBloodTypeAdapter adapter;
    private Button btnRegister;
    private String param, username, password, con_password, contact_no, dob, email_id, address, area, city, state, pincode, bloodid, images = "";
    private ArrayList<BloodType> bloodgroupType;
    private Context context;
    private Shared shared;
    private CircleImageView imageuser;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private LinearLayout lnr_contact_no, lnr_area, lnr_city, lnr_state, lnr_pincode, btnback;
    private ProgressDialog dialog;
    public static final int MEDIA_TYPE_IMAGE_START = 1;
    private static File mediaFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS_FOR_ALL = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void reference() {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_username.setFocusableInTouchMode(true);
        edt_username.setFocusable(true);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        edtconfirmpassword = (EditText) findViewById(R.id.edtconfirmpassword);
        edtcontactno = (EditText) findViewById(R.id.edtcontactno);
        edtemailId = (EditText) findViewById(R.id.edtemailId);
        imageuser = (CircleImageView) findViewById(R.id.imageuser);
        edtarea = (EditText) findViewById(R.id.edtarea);
        edtaddress = (EditText) findViewById(R.id.edtaddress);
        edtcity = (EditText) findViewById(R.id.edtcity);
        edtstate = (EditText) findViewById(R.id.edtstate);
        edtdob = (TextView) findViewById(R.id.edtdob);
        edtpincodeno = (EditText) findViewById(R.id.edtpincode);
        list_item_blood_group = (RecyclerView) findViewById(R.id.list_item_blood_group);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        lnr_area = (LinearLayout) findViewById(R.id.lnr_area);
        lnr_pincode = (LinearLayout) findViewById(R.id.lnr_pincode);
        lnr_contact_no = (LinearLayout) findViewById(R.id.lnr_contact_no);
        lnr_state = (LinearLayout) findViewById(R.id.lnr_state);
        lnr_city = (LinearLayout) findViewById(R.id.lnr_city);
        btnback = (LinearLayout) findViewById(R.id.btnback);

        Application.setFontDefault((RelativeLayout) findViewById(R.id.main_root));
    }

    private void init() {

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        edt_username.setText(name);


        imageuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        edtdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mMonth = c.get(Calendar.MONTH);
                mYear = c.get(Calendar.YEAR);
                date = c.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        Calendar minAdultAge = new GregorianCalendar();
                        minAdultAge.add(Calendar.YEAR, -10);
                        if (minAdultAge.before(userAge)) {
                            Toast.makeText(RegistrationActivity.this, "Select 10+ Age Group", Toast.LENGTH_SHORT).show();
                        } else {
                            if (monthOfYear > 1 && monthOfYear < 9) {
                                edtdob.setText(year + "-" + "0" + (monthOfYear) + "-" + dayOfMonth);
                            } else {
                                edtdob.setText(year + "-" + (monthOfYear) + "-" + dayOfMonth);
                            }
                        }


                    }
                }, mYear, mMonth, date);

                datePickerDialog.show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edt_username.getText().toString();
                con_password = edtconfirmpassword.getText().toString();
                contact_no = edtcontactno.getText().toString();
                email_id = edtemailId.getText().toString();
                address = edtaddress.getText().toString();
                area = edtarea.getText().toString();
                city = edtcity.getText().toString();
                state = edtstate.getText().toString();
                dob = edtdob.getText().toString();
                pincode = edtpincodeno.getText().toString();

                if (edtpassword.getText().toString().contentEquals(edtconfirmpassword.getText().toString())) {
                    password = edtconfirmpassword.getText().toString();
                } else if (!edtpassword.getText().toString().contentEquals(edtconfirmpassword.getText().toString())) {
                    Toast.makeText(context, "Please Enter Valid Password", Toast.LENGTH_SHORT).show();
                }
                /*if (images == null) {
                    Toast.makeText(RegistrationActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
                } else */
                if (username.toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                } else if (edtpassword.getText().toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else if (edtconfirmpassword.getText().toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Please confirm password", Toast.LENGTH_SHORT).show();
                } else if (password == null) {
                    Toast.makeText(RegistrationActivity.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
                } else if (contact_no.toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter Contact No", Toast.LENGTH_SHORT).show();

                } else if (contact_no.toString().length() <= 9) {
                    Toast.makeText(RegistrationActivity.this, "Please Valid Conatct No", Toast.LENGTH_SHORT).show();

                } else if (dob.toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter Date Of Birth", Toast.LENGTH_SHORT).show();
                } else if (email_id.toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter Email ID", Toast.LENGTH_SHORT).show();

                } else if (!isValidEmail(email_id)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter valid Email ID", Toast.LENGTH_SHORT).show();
                } else if (address.toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter Address", Toast.LENGTH_SHORT).show();

                } else if (area.toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter Area", Toast.LENGTH_SHORT).show();

                } else if (city.toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter City", Toast.LENGTH_SHORT).show();

                } else if (state.toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter State", Toast.LENGTH_SHORT).show();

                } else if (pincode.toString().contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                } else {
                    registrationParam();
                }


            }
        });

       /* String ID = FirebaseInstanceId.getInstance().getToken();
        Log.d("ID", "" + ID);*/

        SelectBloodGrouptype();
    }

    private void registrationParam() {


        JSONObject objFinal = new JSONObject();
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserName", username);
            object.put("ContactNo", contact_no);
            object.put("Email", email_id);
            object.put("Password", password);
            object.put("Address", address);
            object.put("Area", area);
            object.put("City", city);
            object.put("State", state);
            object.put("Pincode", pincode);
            object.put("BirthDate", dob);
            object.put("TokenId", FirebaseInstanceId.getInstance().getToken());
            object.put("SocialType", "Normal");
            object.put("BloodId", shared.getBloodTypeId());
            object.put("Device", "A");
            object.put("UserImage", images);
            objFinal.put("Json", object);
            param = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.SIGN_UP, RegistrationActivity.this, param, Config.API_SIGN_UP);
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
        list_item_blood_group.setLayoutManager(new GridLayoutManager(this, 4));
        list_item_blood_group.setAdapter(adapter);
        adapter.setClickListener(this);

    }


    /********************************************Select Image************************************************************/
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle("Add Profile Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (checkPermission_camera()) {
                        if (checkPermission()) {

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        } else {
                            ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

                        }
                    } else {
                        ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.CAMERA}, 145);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (checkPermission()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        // startActivityForResult(intent, 2);
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 2);
                    } else {
                        ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void LoadImage(String images) {
        shared.setUserImage(images);
        Glide.with(context).load(Config.Img + images).error(getResources().getDrawable(R.drawable.ic_user_plas));
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }


    }

    public boolean checkPermission_camera() {
        int result = ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 144:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            return;
                        }
                    }
                }
                break;

            case 145:
                if (checkPermission()) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else {
                    ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    // challan_img.setImageBitmap(bitmap);
                    String path = Environment.getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix";

                    if (!new File(path).exists()) {
                        new File(path).mkdir();
                    }

                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    final String img = file.getAbsolutePath();
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //   encodedImage = encodeImage(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    imageUpload(img);
                    imageuser.setImageBitmap(BitmapFactory.decodeFile(img));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri contentURI = data.getData();
                if (null != contentURI) {
                    // Get the path from the Uri
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        String path = FilePath.getPath(getApplicationContext(), contentURI);
                        imageUpload(path);
                        Bitmap thumbnail = (BitmapFactory.decodeFile(path));
                        imageuser.setImageBitmap(thumbnail);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

              /*  Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                imageUpload(picturePath);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                imageuser.setImageBitmap(thumbnail);*/

            }
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        encImage = encImage.replaceAll("\n", "");

        return encImage;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        int IsSuccess;
        String Message, Response;
        if (flag == Config.API_SIGN_UP) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                IsSuccess = jsonObject.getInt("IsSuccess");
                Message = jsonObject.getString("Message");
                if (IsSuccess == 1) {
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
                    Intent regIntent = new Intent(RegistrationActivity.this, DrawerActivity.class);
                    regIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(regIntent);
                    Toast.makeText(this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                    pd.dismiss();

                } else if (IsSuccess == 0) {
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                Log.e("Responce", jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void itemClicked(View View, int position) {
        BloodType type = bloodgroupType.get(position);
    }

    /**************************************************end select photo**************************************************************/

    private void imageUpload(final String imagePath) {
        dialog = new ProgressDialog(RegistrationActivity.this);
        dialog.setTitle("Upload Image");
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST,
                Config.BASE_URL + "Authentication/UserImageUpload",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int IsSuccess;
                        String Message;
                        Log.d("Response", response);
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            IsSuccess = jsonObject.getInt("IsSuccess");
                            Message = jsonObject.getString("Message");
                            if (IsSuccess == 1) {
                                images = jsonObject.getString("Response");
                                LoadImage(jsonObject.getString("Response"));
                            } else {
                                images = "";
                                imageuser.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_plas));
                                Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                images = "";
                imageuser.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_plas));
                Toast.makeText(context, "Timeout Error", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> object = new HashMap<String, String>();
                return object;
            }
        };

        smr.addFile("image", imagePath);
        smr.setFixedStreamingMode(true);
        smr.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(smr);
    }
}
