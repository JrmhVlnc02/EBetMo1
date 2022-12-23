package com.example.ebetmo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class verification extends AppCompatActivity {
    CardView id_panel;
    ImageView id_picture;
    Button driver, sss, passport, voters, postal, HDMF, GOCC, PRC, barangay, national, senior, submit;
    String ID_Type ,owner_id;
    ImageButton back;
    Dialog dialog;
    SessionManager sessionManager;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    double my_coin;
    EditText name, age, house, street, brgy, city, province, zip;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        id_panel = findViewById(R.id.ID_Panel);
        driver = findViewById(R.id.driver);
        sss = findViewById(R.id.sss);
        passport = findViewById(R.id.passport);
        voters = findViewById(R.id.voters);
        postal = findViewById(R.id.postal);
        HDMF = findViewById(R.id.HDMF);
        GOCC = findViewById(R.id.GOCC);
        PRC = findViewById(R.id.PRC);
        barangay = findViewById(R.id.barangay);
        national = findViewById(R.id.national);
        senior = findViewById(R.id.senior);
        back = findViewById(R.id.back);
        name = findViewById(R.id.name_text);
        age = findViewById(R.id.age_text);
        house = findViewById(R.id.house_text);
        street = findViewById(R.id.street_text);
        brgy = findViewById(R.id.barangay_text);
        city = findViewById(R.id.city_text);
        province = findViewById(R.id.province_text);
        zip = findViewById(R.id.zip_text);
        submit = findViewById(R.id.submit_verification);


        try{
            sessionManager = new SessionManager(getApplicationContext());
            dbHelper = new DBHelper(getApplicationContext());
            sqLiteDatabase = dbHelper.getWritableDatabase();
            my_coin = Double.parseDouble(sessionManager.getCoin());
            dialog = new Dialog(verification.this);
            owner_id = sessionManager.getId();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1, age1, house1, street1, brgy1, city1, province1, zip1;
                name1 = name.getText().toString().trim();
                age1 = age.getText().toString().trim();
                house1 = house.getText().toString().trim();
                street1 = street.getText().toString().trim();
                brgy1 = brgy.getText().toString().trim();
                city1 = city.getText().toString().trim();
                province1 = province.getText().toString().trim();
                zip1 = zip.getText().toString().trim();

//                Bitmap bitmap=((BitmapDrawable)id_picture.getDrawable()).getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[]image = stream.toByteArray();

                Bitmap bitmap = ((BitmapDrawable)id_picture.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();


                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[]item_image = stream.toByteArray();
                final String base64image = Base64.encodeToString(item_image,Base64.DEFAULT);

//                ContentValues contentValues = new ContentValues();
//                contentValues.put("owner_id", owner_id);
//                contentValues.put("name", name1);
//                contentValues.put("age", age1);
//                contentValues.put("house", house1);
//                contentValues.put("street", street1);
//                contentValues.put("brgy", brgy1);
//                contentValues.put("city", city1);
//                contentValues.put("province", province1);
//                contentValues.put("zip", zip1);
//                contentValues.put("id_picture", image);
//                contentValues.put("id_type", ID_Type);

                if (name1.equals("")||age1.equals("")||house1.equals("")||street1.equals("")||brgy1.equals("")||city1.equals("")||province1.equals("")||zip1.equals(""))
                    Toast.makeText(verification.this, "Please Complete Details!", Toast.LENGTH_SHORT).show();
                else{

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/insert_verify.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("Success")){

                                        successDialog();
                                        updateUserValidation(owner_id);
                                        sessionManager.setValid("Verified");
                                    }else
                                        Toast.makeText(verification.this, response, Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", error.getLocalizedMessage());
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("owner_id", owner_id);
                            paramV.put("name", name1);
                            paramV.put("age", age1);
                            paramV.put("house", house1);
                            paramV.put("street", street1);
                            paramV.put("brgy", brgy1);
                            paramV.put("city", city1);
                            paramV.put("province", province1);
                            paramV.put("zip", zip1);
                            paramV.put("id_picture", base64image);
                            paramV.put("id_type", ID_Type);

                            return paramV;
                        }
                    };
                    queue.add(stringRequest);

//                    long result = sqLiteDatabase.insert("verification",null,contentValues);
//                    if(result==-1){
//                        Toast.makeText(getApplicationContext(), "Something problem in Inserting Data", Toast.LENGTH_SHORT).show();
//                    }else{
//                        successDialog();
//                        updateUserValidation(owner_id);
//                        sessionManager.setValid("Verified");
//
//
//                    }
                }



            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type = "Driver's License";
                getIdDialog();
            }
        });
        sss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type = "SSS";
                getIdDialog();
            }
        });
        passport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type = "Passport ID";
                getIdDialog();
            }
        });
        voters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type = "Voter's ID";
                getIdDialog();
            }
        });
        postal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type="Postal ID";
                getIdDialog();
            }
        });
        HDMF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type = "HDMF(Pagibig)";
                getIdDialog();
            }
        });
        GOCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type = "GOCC ID";
                getIdDialog();
            }
        });
        PRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type = "PRC ID";
                getIdDialog();
            }
        });
        barangay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type = "Barangay ID";
                getIdDialog();
            }
        });
        national.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type = "National ID";
                getIdDialog();
            }
        });
        senior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID_Type = "Senior Citizen ID";
                getIdDialog();
            }
        });
    }

    private void successDialog() {
        dialog.setContentView(R.layout.application_submitted_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        Button done = dialog.findViewById(R.id.done_btn);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }
    private void updateUserValidation(String owner_id_verify) {
        String verify = "Verified";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/update_verify.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Success")){
                            Toast.makeText(verification.this, "you are verified", Toast.LENGTH_SHORT).show();

                        }else
                            Toast.makeText(verification.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getLocalizedMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("owner_id", owner_id_verify);
                paramV.put("verified", "Verified");


                return paramV;
            }
        };
        queue.add(stringRequest);

    }
//    private void updateUserValidation(String owner_id) {
//        String verify = "Verified";
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("verified", verify);
//
//        long result = sqLiteDatabase.update("users",contentValues,"id="+owner_id,null);
//        if(result==-1){
//            Toast.makeText(getApplicationContext(), "error user verification", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(this, "you are verified", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    private void getIdDialog() {
        dialog.setContentView(R.layout.pick_id_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        ImageButton close = dialog.findViewById(R.id.close);
        ImageButton pick_image = dialog.findViewById(R.id.pick_image);
        id_picture = dialog.findViewById(R.id.id_picture);
        Button done = dialog.findViewById(R.id.done_btn);

//        pick_image.setOnClickListener(v -> mGetContent .launch("image/*"));
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        pick_image.setOnClickListener(v -> activityResultLauncher.launch(intent));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_panel.setVisibility(View.GONE);
                dialog.dismiss();
                Toast.makeText(verification.this, ID_Type, Toast.LENGTH_SHORT).show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
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
                            id_picture.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

//    ActivityResultLauncher<String> mGetContent =registerForActivityResult(
//            new ActivityResultContracts.GetContent(),
//            new ActivityResultCallback<Uri>() {
//                @Override
//                public void onActivityResult(Uri result) {
//                    id_picture.setImageURI(result);
//
//                }
//            }
//    );

}