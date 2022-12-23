package com.example.ebetmo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class edit_profile extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String current_ID;
    EditText name, contact, address, email, password, date;
    Button save;
    ImageView avatar;
    TextView verify;
    ImageButton close, camera;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        findId();

        try{
            sessionManager = new SessionManager(this);
            dbHelper = new DBHelper(getApplicationContext());
            sqLiteDatabase = dbHelper.getWritableDatabase();
            current_ID = sessionManager.getId();
            loadingDialog = new LoadingDialog(this);
            displayProfile_online(current_ID);





        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), account.class));
                finish();
            }
        });

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        camera.setOnClickListener(v -> activityResultLauncher.launch(intent));

//        camera.setOnClickListener(v -> mGetContent .launch("image/*"));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.showLoading("Loading...");

//                Bitmap bitmap=((BitmapDrawable)avatar.getDrawable()).getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[]image = stream.toByteArray();

                Bitmap bitmap = ((BitmapDrawable)avatar.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();


                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[]item_image = stream.toByteArray();
                final String base64image = Base64.encodeToString(item_image,Base64.DEFAULT);


                String name1 = name.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String contact1 = contact.getText().toString().trim();
                String password1 = password.getText().toString().trim();
                String bDate1 = date.getText().toString().trim();
                String address1 = address.getText().toString().trim();


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/update_profile.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("Success")){

                                    updateSession(email1);
                        
                                    Toast.makeText(getApplicationContext(), "Your account is updated.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), account.class));
                                    finish();
                                }else
                                    Toast.makeText(edit_profile.this, response, Toast.LENGTH_SHORT).show();
                                    loadingDialog.hideLoading();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getLocalizedMessage());
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("user_id", current_ID);
                        paramV.put("fullName", name1);
                        paramV.put("contact", contact1);
                        paramV.put("email", email1);
                        paramV.put("password", password1);
                        paramV.put("profile", base64image);
                        paramV.put("b_day", bDate1);
                        paramV.put("address", address1);
                        return paramV;
                    }
                };
                queue.add(stringRequest);

//                ContentValues contentValues = new ContentValues();
//                contentValues.put("fullName", name1);
//                contentValues.put("contact", contact1);
//                contentValues.put("email", email1);
//                contentValues.put("password", password1);
//                contentValues.put("profile", image);
//                contentValues.put("b_day", bDate1);
//                contentValues.put("address", address1);

//                long result = sqLiteDatabase.update("users",contentValues,"id="+current_ID,null);
//                if(result==-1){
//                    Toast.makeText(getApplicationContext(), "Failed updating account.", Toast.LENGTH_SHORT).show();
//                    loadingDialog.hideLoading();
//                }else{
//                    Toast.makeText(getApplicationContext(), "Your account is updated.", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), account.class));
//                    finish();
//                    updateSession(current_ID);
//
//                }

            }
        });



    }

    private void updateSession(String emailuser) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/session.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0 ) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    sessionManager.setId(String.valueOf(jsonObject.getInt("id")));
                                    sessionManager.setName(jsonObject.getString("fullname"));
                                    sessionManager.setCoin(jsonObject.getString("coin"));
                                    sessionManager.setValid(jsonObject.getString("verified"));


                                }
                            }else{
                                Toast.makeText(edit_profile.this, "FAILED TO UPDATE SESSION!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(edit_profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getLocalizedMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("emailed", emailuser);

                return paramV;
            }
        };
        queue.add(stringRequest);

    }

//    private void updateSession(String id) {
//        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{id});
//        try{
//            if (c.getCount()>0){
//                while(c.moveToNext()){
//                    sessionManager.setId(String.valueOf(c.getInt(0)));
//                    sessionManager.setName(c.getString(1));
//                    sessionManager.setCoin(c.getString(9));
//                    sessionManager.setValid(c.getString(7));
//                    sessionManager.setEmail(c.getString(4));
//                }
//                c.close();
//            }else{
//                Toast.makeText(this, "Failed to store session", Toast.LENGTH_SHORT).show();
//            }
//
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Error: Fetching Session!", Toast.LENGTH_SHORT).show();
//
//        }
//    }

    private void findId() {
        name = findViewById(R.id.fName);
        contact = findViewById(R.id.user_contact);
        address = findViewById(R.id.user_address);
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        date = findViewById(R.id.user_bDay);
        save = findViewById(R.id.save_btn);
        avatar = findViewById(R.id.profile);
        verify = findViewById(R.id.verify);
        close = findViewById(R.id.close);
        camera = findViewById(R.id.upload);
    }

    private void displayProfile_online(String current) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/get_single_user.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0 ) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);



                                    name.setText(jsonObject.getString("fullname"));
                                contact.setText(jsonObject.getString("contact"));
                                address.setText(jsonObject.getString("address"));
                                email.setText(jsonObject.getString("email"));
                                password.setText(jsonObject.getString("password"));
                                date.setText(jsonObject.getString("b_day"));

                                    Picasso.with(getApplicationContext()).load("http://"+ final_ip.IP_ADDRESS +"/ebetmo_final/"+jsonObject.getString("profile")).into(avatar);

                                String v = jsonObject.getString("verified");
                                if (v.equals("Verified"))verify.setText("Account verified.");
                                else verify.setText("Unverified account.");


                                }

                            }else{
                                Toast.makeText(edit_profile.this, "Empty Record!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getLocalizedMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("user_id", current);
                return paramV;
            }
        };
        queue.add(stringRequest);

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
                            avatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

//    private void displayProfile(String current) {
//        sqLiteDatabase =dbHelper.getWritableDatabase();
//        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE id=?", new String[]{current});
//        if (c.getCount()>0){
//            while(c.moveToNext()){
//                name.setText(c.getString(1));
//                contact.setText(c.getString(2));
//                address.setText(c.getString(3));
//                email.setText(c.getString(4));
//                password.setText(c.getString(5));
//                date.setText(c.getString(8));
//
//                byte[]profile = c.getBlob(6);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(profile, 0,profile.length);
//                avatar.setImageBitmap(bitmap);
//
//                String v = c.getString(7);
//                if (v.equals("Verified"))verify.setText("Account verified.");
//                else verify.setText("Unverified account.");
//
//
//            }
//        }else Toast.makeText(this, "No Data Fetch!", Toast.LENGTH_SHORT).show();
//
//    }

//    ActivityResultLauncher<String> mGetContent =registerForActivityResult(
//            new ActivityResultContracts.GetContent(),
//            new ActivityResultCallback<Uri>() {
//                @Override
//                public void onActivityResult(Uri result) {
//                    avatar.setImageURI(result);
//
//                }
//            }
//    );
}