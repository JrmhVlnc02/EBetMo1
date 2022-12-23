package com.example.ebetmo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class add_item extends AppCompatActivity {
    private final int REQUEST_CAMERA = 1, REQUEST_GALLERY = 0;
    private Bitmap bitmap;
    SessionManager sessionManager;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText name, des, price,type, time, slots;
    ImageView image;
    String owner_id;
    Button submit_item, upload, save;
    ImageButton back;
    LoadingDialog loadingDialog;
    String item_id;
    public static String imageLocation = "";
    private RequestQueue queue;

    //192.168.0.122

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getID();
        try{


            dbHelper = new DBHelper(this);
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sessionManager = new SessionManager(this);
            owner_id = sessionManager.getId();
            loadingDialog = new LoadingDialog(this);
            slots.setText("0");

        }catch (Exception e){

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();
            finish();

        }

        if(getIntent().getBundleExtra("item_data")!=null){
            Bundle bundle = getIntent().getBundleExtra("item_data");
            item_id = String.valueOf(bundle.getInt("item_id"));
            save.setVisibility(View.VISIBLE);
            submit_item.setVisibility(View.GONE);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/single_item.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                if(jsonArray.length() > 0 ) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        name.setText(jsonObject.getString("item_name"));
                                        des.setText(jsonObject.getString("item_description"));
                                        price.setText(jsonObject.getString("price"));
                                        type.setText(jsonObject.getString("type"));
                                        time.setText(jsonObject.getString("time"));
                                        slots.setText(jsonObject.getString("slots"));
                                        Picasso.with(getApplicationContext()).load("http://" + final_ip.IP_ADDRESS + "/ebetmo_final/" + jsonObject.getString("item_image")).into(image);


                                    }
                                }else{
                                        Toast.makeText(add_item.this, "Empty Data! Try again.", Toast.LENGTH_SHORT).show();
                                        finish();
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
                    paramV.put("id", item_id);
                    return paramV;
                }
            };
            queue.add(stringRequest);





        }

//        upload.setOnClickListener(v -> mGetContent .launch("image/*"));
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        upload.setOnClickListener(v -> activityResultLauncher.launch(intent));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.showLoading("Please Wait...");


                Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();


                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[]item_image = stream.toByteArray();
                    final String base64image = Base64.encodeToString(item_image,Base64.DEFAULT);




                String named = name.getText().toString().trim();
                String des1 = des.getText().toString().trim();
                String price1 = price.getText().toString().trim();
                String type1 = type.getText().toString().trim();
                String time1 = time.getText().toString().trim();
                String slots1= slots.getText().toString().trim();
                String status = "open";
                String winner = "0";

//                ContentValues contentValues = new ContentValues();
//                contentValues.put("owner_id", owner_id);
//                contentValues.put("item_name", named);
//                contentValues.put("item_description", des1);
//                contentValues.put("item_image", item_image);
//                contentValues.put("type", type1);
//                contentValues.put("time", time1);
//                contentValues.put("price", price1);
//                contentValues.put("slots", slots1);
//                contentValues.put("status", status);
//                contentValues.put("winner", winner);

                if(named.equals("")||des1.equals("")||type1.equals("")||time1.equals("")||price1.equals("")||slots1.equals("")){
                    Toast.makeText(add_item.this, "Please Complete Details!", Toast.LENGTH_SHORT).show();
                    loadingDialog.hideLoading();
                }

                double getSlot = Double.parseDouble(slots1);

                    if (getSlot > 100){
                        loadingDialog.hideLoading();
                        Toast.makeText(add_item.this, "Maximum slots is up to 100 only", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (named.equals("")||des1.equals("")||price1.equals("")||type1.equals("")||time1.equals("")||slots1.equals("")){
                            Toast.makeText(add_item.this, "Please complete details!", Toast.LENGTH_SHORT).show();
                        }else{
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/post_item.php";

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equals("Success")){
                                                Toast.makeText(getApplicationContext(), "Item Posted.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(add_item.this, home.class));
                                                finish();

                                            }else{
                                                Toast.makeText(add_item.this, response, Toast.LENGTH_SHORT).show();
                                                loadingDialog.hideLoading();

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
                                    paramV.put("owner_id", owner_id);
                                    paramV.put("name", named);
                                    paramV.put("des", des1);
                                    paramV.put("price", price1);
                                    paramV.put("type", type1);
                                    paramV.put("time", time1);
                                    paramV.put("slots", slots1);
                                    paramV.put("status", status);
                                    paramV.put("winner", winner);


//
//                                    String encodedImage = Base64.encodeToString(item_image, Base64.DEFAULT);

                                    paramV.put("item_image", base64image);

                                    return paramV;
                                }
                            };
                            queue.add(stringRequest);
//                            long result = sqLiteDatabase.insert("items",null,contentValues);
//                            if(result==-1){
//                                Toast.makeText(getApplicationContext(), "Something problem in Inserting Data!", Toast.LENGTH_SHORT).show();
//                                loadingDialog.hideLoading();
//                            }else{
//                                Toast.makeText(getApplicationContext(), "Item Posted.", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(add_item.this, home.class));
//                                finish();
//                            }
                        }
                    }










            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.showLoading("Please Wait...");

//                Bitmap bitmap=((BitmapDrawable)image.getDrawable()).getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[]item_image = stream.toByteArray();

                Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();


                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[]item_image = stream.toByteArray();
                final String base64image = Base64.encodeToString(item_image,Base64.DEFAULT);



                String named = name.getText().toString().trim();
                String des1 = des.getText().toString().trim();
                String price1 = price.getText().toString().trim();
                String type1 = type.getText().toString().trim();
                String time1 = time.getText().toString().trim();
                String slots1= slots.getText().toString().trim();

//                ContentValues contentValues = new ContentValues();
//                contentValues.put("item_name", named);
//                contentValues.put("item_description", des1);
//                contentValues.put("item_image", item_image);
//                contentValues.put("type", type1);
//                contentValues.put("time", time1);
//                contentValues.put("price", price1);
//                contentValues.put("slots", slots1);

                if(named.equals("")||des1.equals("")||type1.equals("")||time1.equals("")||price1.equals("")||slots1.equals("")){
                    Toast.makeText(add_item.this, "Please Complete Details!", Toast.LENGTH_SHORT).show();
                    loadingDialog.hideLoading();
                }else{
                    double getSlot = Double.parseDouble(slots1);
                    if (getSlot > 100){
                        loadingDialog.hideLoading();
                        Toast.makeText(add_item.this, "Maximum slots is up to 100 only", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (named.equals("")||des1.equals("")||price1.equals("")||type1.equals("")||time1.equals("")||slots1.equals("")){
                            Toast.makeText(add_item.this, "Please complete details!", Toast.LENGTH_SHORT).show();
                        }else{

                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/update_item.php";

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equals("Success")){
                                                    Toast.makeText(getApplicationContext(), "Item is Updated.", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(add_item.this, home.class));
                                                    finish();

                                            }else{
                                                Toast.makeText(add_item.this, response, Toast.LENGTH_SHORT).show();
                                                loadingDialog.hideLoading();

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


                                    paramV.put("item_name", named);
                                    paramV.put("item_description", des1);
                                    paramV.put("item_image", base64image);
                                    paramV.put("type", type1);
                                    paramV.put("time", time1);
                                    paramV.put("price", price1);
                                    paramV.put("slots", slots1);
                                    paramV.put("item_id", item_id);





                                    return paramV;
                                }
                            };
                            queue.add(stringRequest);

//                            long result = sqLiteDatabase.update("items",contentValues,"id="+item_id,null);
//                            if(result==-1){
//                                Toast.makeText(getApplicationContext(), "Something problem in Updating Data!", Toast.LENGTH_SHORT).show();
//                                loadingDialog.hideLoading();
//                            }else{
//                                Toast.makeText(getApplicationContext(), "Item is Updated.", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(add_item.this, home.class));
//                                finish();
//                            }
                        }
                    }

                }




            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(time);
            }
        });


    }

    private void showDateTimeDialog(EditText time) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy HH:mm");

                        time.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(add_item.this, timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(add_item.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void getID() {

        name = findViewById(R.id.item_name);
        des = findViewById(R.id.item_des);
        price = findViewById(R.id.item_price);
        type = findViewById(R.id.item_type);
        time = findViewById(R.id.item_date);
        slots = findViewById(R.id.item_slots);
        image = findViewById(R.id.item_image);
        submit_item = findViewById(R.id.submit_btn);
        back = findViewById(R.id.back);
        upload = findViewById(R.id.upload_image);
        save = findViewById(R.id.save_btn);
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
                            image.setImageBitmap(bitmap);
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
//                    if(result.)
//
//                }
//            }
//    );
//
//    ActivityResultLauncher<String> mGetContent =registerForActivityResult(
//            new ActivityResultContracts.GetContent(),
//            new ActivityResultCallback<Uri>() {
//                @Override
//                public void onActivityResult(Uri result) {
//                    image.setImageURI(result);
//
//                }
//            }
//    );
//
//    private void getPic()
//    {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        galleryIntent.setType(getString(R.string.galleryType));
//        startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select Images"), REQUEST_GALLERY);
//    }
//
//    private void takePic()
//    {
//        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(takePicIntent, REQUEST_CAMERA);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK)
//        {
//            if(requestCode == REQUEST_CAMERA)
//            {
//
//                itemBitmap = (Bitmap) data.getExtras().get("data");
//                image.setImageBitmap(itemBitmap);
//
//                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//                Uri tempUri = getImageUri(getApplicationContext(), itemBitmap);
//
//                // CALL THIS METHOD TO GET THE ACTUAL PATH
//                File finalFile = new File(getRealPathFromURI(tempUri));
//
//                imageLocation = finalFile.getAbsolutePath();
//
//
//            }
//            else if(requestCode == REQUEST_GALLERY && data != null)
//            {
//                try {
//                    itemBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),
//                            data.getData());
//                    image.setImageBitmap(itemBitmap);
//
//                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//                    Uri tempUri = getImageUri(getApplicationContext(), itemBitmap);
//
//                    // CALL THIS METHOD TO GET THE ACTUAL PATH
//                    File finalFile = new File(getRealPathFromURI(tempUri));
//
//                    imageLocation = finalFile.getAbsolutePath();
//                    Toast.makeText(getApplicationContext(), finalFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public String getRealPathFromURI(Uri uri) {
//        String path = "";
//        if (getContentResolver() != null) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                path = cursor.getString(idx);
//                cursor.close();
//            }
//        }
//        return path;
//    }
//
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//    public void insertPic1(View view){
//        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Upload Item Image");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(items[which].equals("Camera"))
//                    takePic();
//                else if(items[which].equals("Gallery"))
//                    getPic();
//                else if(items[which].equals("Cancel"))
//                    dialog.dismiss();
//            }
//        });
//        builder.show();
//    }
}