package com.multipz.bloodbook.Activity;

import android.Manifest;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.bumptech.glide.Glide;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements ItemClickListener, MyAsyncTask.AsyncInterface {

    private LinearLayout btnbackEdit;
    private Context context;
    private Shared shared;
    private String param, username, email, cno, address, area, city, state, pincode, Address, Pincode;
    public String images = "";

    private EditText et_your_name, et_email, et_mobile_no, et_address, et_area, et_city, et_state, et_pincode;
    private CircleImageView img_profile;
    private Button btn_save;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_profile);
        context = this;
        shared = new Shared(context);

        reference();
        init();

    }

    private void init() {

        Intent i = getIntent();
        username = i.getStringExtra("username");
        address = i.getStringExtra("address");
        city = i.getStringExtra("City");
        cno = i.getStringExtra("cno");
        Address = i.getStringExtra("Address");
        pincode = i.getStringExtra("Pincode");
        images = i.getStringExtra("image");

        Glide.with(context).load(Config.Img + "" + images).error(getResources().getDrawable(R.drawable.ic_user_plas));

        et_your_name.setText(username);
        et_address.setText(shared.getAddress());
        et_city.setText(city);
        et_mobile_no.setText(cno);
        et_pincode.setText(pincode);
        et_area.setText(shared.getArea());
        et_state.setText(shared.getState());
        et_email.setText(shared.getEmail());


        btnbackEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_your_name.getText().toString();
                email = et_email.getText().toString();
                cno = et_mobile_no.getText().toString();
                address = et_address.getText().toString();
                area = et_area.getText().toString();
                city = et_city.getText().toString();
                pincode = et_pincode.getText().toString();
                state = et_state.getText().toString();
               /* if (images.toString().contentEquals("")) {
                    Toast.makeText(EditProfileActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                } else */if (username.toString().contentEquals("")) {
                    Toast.makeText(EditProfileActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                } else if (email.toString().contentEquals("")) {
                    Toast.makeText(EditProfileActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(EditProfileActivity.this, "Please Enter valid Email ID", Toast.LENGTH_SHORT).show();
                } else if (cno.toString().contentEquals("")) {
                    Toast.makeText(EditProfileActivity.this, "Enter Contact No", Toast.LENGTH_SHORT).show();
                } else if (et_mobile_no.getText().toString().length() <= 9) {
                    Toast.makeText(EditProfileActivity.this, "Enter Valid Contact No", Toast.LENGTH_SHORT).show();
                } else if (address.toString().contentEquals("")) {
                    Toast.makeText(EditProfileActivity.this, "Enter Address", Toast.LENGTH_SHORT).show();

                } else if (area.toString().contentEquals("")) {
                    Toast.makeText(EditProfileActivity.this, "Enter Area", Toast.LENGTH_SHORT).show();

                } else if (city.toString().contentEquals("")) {
                    Toast.makeText(EditProfileActivity.this, "Enter City", Toast.LENGTH_SHORT).show();

                } else if (state.toString().contentEquals("")) {
                    Toast.makeText(EditProfileActivity.this, "Enter State", Toast.LENGTH_SHORT).show();

                } else if (pincode.toString().contentEquals("")) {
                    Toast.makeText(EditProfileActivity.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                } else {
                    EditProfile();
                }
            }
        });
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void reference() {
        btnbackEdit = (LinearLayout) findViewById(R.id.btnbackEdit);
        et_your_name = (EditText) findViewById(R.id.et_your_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_mobile_no = (EditText) findViewById(R.id.et_mobile_no);
        et_address = (EditText) findViewById(R.id.et_address);
        et_area = (EditText) findViewById(R.id.et_area);
        et_city = (EditText) findViewById(R.id.et_city);
        et_pincode = (EditText) findViewById(R.id.et_pincode);
        et_state = (EditText) findViewById(R.id.et_state);
        img_profile = (CircleImageView) findViewById(R.id.img_profile);
        btn_save = (Button) findViewById(R.id.btn_save);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    /***********************************************************Select Image****************************************/
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Profile Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (checkPermission()) {
                        if (checkPermission_camera()) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        } else {
                            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA}, 145);
                        }
                    } else {
                        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (checkPermission()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        // startActivityForResult(intent, 2);
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 2);
                    } else {
                        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermission_camera() {
        int result = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA);
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

                } else {

                }
                break;
        }
    }

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
                    img_profile.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

                    imageUpload(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

               /* Uri selectedFileUri = data.getData();
                String selectedFilePath = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    selectedFilePath = FilePath.getPath(this, selectedFileUri);
                }
                imageUpload(selectedFilePath);
                Bitmap thumbnail = (BitmapFactory.decodeFile(selectedFilePath));
                img_profile.setImageBitmap(thumbnail);*/


                Uri contentURI = data.getData();
                if (null != contentURI) {
                    // Get the path from the Uri
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        String path = saveImage(bitmap);
                        imageUpload(path);
                        Bitmap thumbnail = (BitmapFactory.decodeFile(path));
                        img_profile.setImageBitmap(thumbnail);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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

    /******************************************************************************************/


    private void EditProfile() {

        JSONObject objFinal = new JSONObject();
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserId", shared.getUserId());
            object.put("Address", address);
            object.put("UserImage", images);
            object.put("ContactNo", cno);
            object.put("UserName", username);
            object.put("Area", area);
            object.put("City", city);
            object.put("State", state);
            object.put("Pincode", pincode);
            object.put("Email", email);
            objFinal.put("Json", object);
            param = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.EditUser, EditProfileActivity.this, param, Config.API_EditUser);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }


    }


    /****************************************************************************************/


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

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void LoadImage(String images) {
        shared.setUserImage(images);
        Glide.with(context).load(Config.Img + images).error(getResources().getDrawable(R.drawable.ic_user_plas));
    }

    private void imageUpload(final String imagePath) {
        dialog = new ProgressDialog(EditProfileActivity.this);
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
                                img_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_plas));
                                Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                images = "";
                img_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_plas));
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void itemClicked(View View, int position) {

    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        int IsSuccess;
        String Message, Response, Area = null, city = null, State = null, Address = null;
        if (flag == Config.API_EditUser) {
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
                    Toast.makeText(context, "" + Message, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
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
}
