package com.example.ebetmo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class sign_up extends AppCompatActivity {
    EditText name, contact, email, password, confirm, bDate, address;
    Button SignUp;
    ImageView profile;
    String Verification = "none";
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    TextView to_login;
    ImageButton upload;
    LoadingDialog loadingDialog;
    Bitmap bitmap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findId();

        try{
            dbHelper = new DBHelper(this);
            sqLiteDatabase = dbHelper.getReadableDatabase();
            loadingDialog = new LoadingDialog(this);


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }

        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(sign_up.this, MainActivity.class));
                finish();
            }
        });

//        upload.setOnClickListener(v -> mGetContent .launch("image/*"));
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        upload.setOnClickListener(v -> activityResultLauncher.launch(intent));

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(bDate);

            }
        });






        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.showLoading("Please Wait...");

//                Bitmap bitmap=((BitmapDrawable)profile.getDrawable()).getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[]image = stream.toByteArray();

                Bitmap bitmap = ((BitmapDrawable)profile.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();


                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[]item_image = stream.toByteArray();
                final String base64image = Base64.encodeToString(item_image,Base64.DEFAULT);



                String name1 = name.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String contact1 = contact.getText().toString().trim();
                String password1 = password.getText().toString().trim();
                String bDate1 = bDate.getText().toString().trim();
                String confirm1 = confirm.getText().toString().trim();
                String address1 = address.getText().toString().trim();
                String coin = "0";

//                ContentValues contentValues = new ContentValues();
//                contentValues.put("fullName", name1);
//                contentValues.put("contact", contact1);
//                contentValues.put("email", email1);
//                contentValues.put("password", password1);
//                contentValues.put("profile", image);
//                contentValues.put("b_day", bDate1);
//                contentValues.put("address", address1);
//                contentValues.put("coin", coin);
//                contentValues.put("verified", Verification);

                if(name1.equals("") || email1.equals("") || contact1.equals("") || password1.equals("") || bDate1.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Complete Details", Toast.LENGTH_SHORT).show();
                    loadingDialog.hideLoading();
                }else{
                    if(confirm1.equals(password1)){

                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url ="http://"+final_ip.IP_ADDRESS+"/ebetmo_final/signup.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @SuppressLint("ResourceAsColor")
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("Success")){
                                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(sign_up.this, MainActivity.class));
                                            finish();
                                        }else
                                            loadingDialog.hideLoading();
                                            Toast.makeText(sign_up.this, response, Toast.LENGTH_SHORT).show();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error", error.getLocalizedMessage());
                            }
                        }){
                            protected Map<String, String> getParams(){
                                Map<String, String> paramV = new HashMap<>();
                                paramV.put("fullname", name1);
                                paramV.put("email", email1);
                                paramV.put("contact", contact1);
                                paramV.put("password", password1);
                                paramV.put("bday", bDate1);
                                paramV.put("address", address1);
                                paramV.put("coin", coin);
                                paramV.put("verified", Verification);
                                paramV.put("profile", base64image);

                                return paramV;
                            }
                        };
                        queue.add(stringRequest);





//                        boolean check = dbHelper.check_email(email1);
//                        if(!check){
//                            long result = sqLiteDatabase.insert("users",null,contentValues);
//                            if(result==-1){
//                                loadingDialog.hideLoading();
//                                Toast.makeText(getApplicationContext(), "Something problem in Inserting Data", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(sign_up.this, MainActivity.class));
//                                finish();
//                            }
//                        }else{
//                            loadingDialog.hideLoading();
//                            Toast.makeText(getApplicationContext(), "Email Already Exist!", Toast.LENGTH_SHORT).show();
//                        }

                    }else{
                        loadingDialog.hideLoading();
                        Toast.makeText(getApplicationContext(), "Password not match!", Toast.LENGTH_SHORT).show();
                    }

                }






            }
        });


    }

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            profile.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    private void showDateTimeDialog(EditText bDate) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy");

                bDate.setText(simpleDateFormat.format(calendar.getTime()));

              }
        };
        new DatePickerDialog(sign_up.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    private void findId() {
        name = findViewById(R.id.fName);
        contact = findViewById(R.id.contact);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        bDate = findViewById(R.id.bDay);
        SignUp = findViewById(R.id.signUp_btn);
        profile = findViewById(R.id.profile);
        to_login = findViewById(R.id.to_login);
        address = findViewById(R.id.address);
        upload = findViewById(R.id.upload);

    }

    ActivityResultLauncher<String> mGetContent =registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    profile.setImageURI(result);

                }
            }
    );
}