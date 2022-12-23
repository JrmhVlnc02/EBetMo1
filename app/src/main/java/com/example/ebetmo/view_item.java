package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

public class view_item extends AppCompatActivity {
    TextView name, des, price, status, slots, date, winner,type,slotNumber,betDate, item_owner;
    ImageButton back;
    ImageView item_image;
    String item_id;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    LinearLayout manager_button, entry_panel;
    SessionManager sessionManager;
    String current_user_id, owner_id;
    Button edit, start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        name = findViewById(R.id.item_name);
        des = findViewById(R.id.item_des);
        price = findViewById(R.id.item_price);
        status = findViewById(R.id.item_status);
        slots = findViewById(R.id.item_slots);
        date = findViewById(R.id.item_date);
        winner = findViewById(R.id.item_winner);
        type = findViewById(R.id.item_type);
        back = findViewById(R.id.back);
        item_image = findViewById(R.id.item_image);
        manager_button = findViewById(R.id.manager_button);
        edit = findViewById(R.id.edit_btn);
        entry_panel = findViewById(R.id.entry_panel);
        slotNumber = findViewById(R.id.slot_num);
        betDate = findViewById(R.id.bet_date);
        item_owner = findViewById(R.id.item_owner);
        start = findViewById(R.id.start_btn);

        Bundle extras = getIntent().getExtras();
        item_id = extras.getString("item_id");

        if(item_id !=null){

        }else finish();

        try{
            dbHelper = new DBHelper(getApplicationContext());
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sessionManager = new SessionManager(this);
            current_user_id = sessionManager.getId();
            displayItemsInfo_online(item_id);
            checkManager_online(item_id);
            checkBet_online(item_id,current_user_id);



        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putInt("item_id",Integer.parseInt(item_id));
                    bundle.putInt("owner_id",Integer.parseInt(owner_id));
                    Intent intent=new Intent(getApplicationContext(),raffle.class);
                    intent.putExtra("item_data",bundle);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    finish();
                    Toast.makeText(getApplicationContext(), "Error: Try Again!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        item_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id",Integer.parseInt(owner_id));
                    Intent intent=new Intent(getApplicationContext(),view_user.class);
                    intent.putExtra("user_data",bundle);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    finish();
                    Toast.makeText(getApplicationContext(), "Error: Try Again!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putInt("item_id",Integer.parseInt(item_id));
                    bundle.putInt("owner_id",Integer.parseInt(owner_id));
                    Intent intent=new Intent(getApplicationContext(),raffle.class);
                    intent.putExtra("item_data",bundle);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    finish();
                    Toast.makeText(getApplicationContext(), "Error: Try Again!", Toast.LENGTH_SHORT).show();

                }

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putInt("item_id",Integer.parseInt(item_id));
                    bundle.putString("item_name", name.getText().toString().trim());
                    Intent intent=new Intent(getApplicationContext(),add_item.class);
                    intent.putExtra("item_data",bundle);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    finish();

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void displaySeller(String owner_id) {
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

                                    item_owner.setText(jsonObject.getString("fullname"));


                                }

                            }else{
                                Toast.makeText(view_item.this, "Failed to retreive seller. ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(view_item.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                paramV.put("user_id", owner_id);
                return paramV;
            }
        };
        queue.add(stringRequest);
//        Cursor c = sqLiteDatabase.rawQuery("Select fullName FROM users WHERE id = ?", new String[]{owner_id});
//        while(c.moveToNext()) item_owner.setText(c.getString(0));
    }

    private void checkBet_online(String item_id, String current_user_id){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/checkbet.php";

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
                                    slotNumber.setText("Slot number: "+jsonObject.getString("chosen_number"));
                                    betDate.setText(jsonObject.getString("bet_date"));


                                }

                            }else entry_panel.setVisibility(View.GONE);

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
                paramV.put("owner_id", current_user_id);
                paramV.put("item_id", item_id);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

//    private void checkBet(String item_id, String current_user_id) {
//        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM bets WHERE owner_id =? and item_id=?", new String[]{current_user_id,item_id});
//        if (c.getCount()>0){
//            entry_panel.setVisibility(View.VISIBLE);
//            while(c.moveToNext()){
//                slotNumber.setText("Slot number: "+c.getString(6));
//                betDate.setText(c.getString(7));
//            }
//            c.close();
//
//        }else{
//            entry_panel.setVisibility(View.GONE);
//        }
//    }

    private void checkManager_online(String item_id){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/single_item.php";

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
                                    String item_owner_id = "0";
                                     item_owner_id = jsonObject.getString("owner_id");
                                    if (item_owner_id.equals(current_user_id)){
                                        manager_button.setVisibility(View.VISIBLE);
                                    }else manager_button.setVisibility(View.GONE);




                                }

                            }else{
                                Toast.makeText(view_item.this, "Failed to get manager!", Toast.LENGTH_SHORT).show();
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

//    private void checkManager(String item_id) {
//        String item_owner_id = "0";
//        Cursor c = sqLiteDatabase.rawQuery("SELECT owner_id FROM items where id=?", new String[]{item_id});
//        if (c.getCount()>0){
//            while (c.moveToNext()){
//                item_owner_id = c.getString(0);
//            }
//            if (item_owner_id.equals(current_user_id)){
//                manager_button.setVisibility(View.VISIBLE);
//            }else manager_button.setVisibility(View.GONE);
//
//
//        }else{
//            Toast.makeText(this, "Failed to match ID of manager", Toast.LENGTH_SHORT).show();
//            manager_button.setVisibility(View.GONE);
//        }
//        c.close();
//    }
    private  void displayItemsInfo_online(String item_id){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/single_item.php";

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

                                    name.setText(jsonObject.getString("item_name"));
                                    des.setText(jsonObject.getString("item_description"));
                                    double getPrice = Double.parseDouble(jsonObject.getString("price"));
                                    price.setText("₱"+getPrice);
                                    displaySeller(jsonObject.getString("owner_id"));

                                    String getStatus = jsonObject.getString("status");
                                    if (getStatus.equals("open")) status.setText("Open for Betting");
                                    else status.setText("Betting is Closed.");

                                    slots.setText(jsonObject.getString("slots")+" Slots");
                                    date.setText(jsonObject.getString("time"));
                                    String getWinner = jsonObject.getString("winner");
                                    if(getWinner.equals("0")||getWinner.equals("")) winner.setText("Unknown");
                                    else{
                                        winner.setText(getWinner);
                                    }

                                    type.setText(jsonObject.getString("type"));
                                    Picasso.with(getApplicationContext()).load("http://"+final_ip.IP_ADDRESS+"/ebetmo_final/"+jsonObject.getString("item_image")).into(item_image);



                                }

                            }else{
                                Toast.makeText(view_item.this, "Empty Record!", Toast.LENGTH_SHORT).show();
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
//    private void displayItemsInfo(String item_id) {
//        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM items WHERE id =?", new String[]{item_id});
//        if (c.getCount()>0){
//            while(c.moveToNext()){
//                name.setText(c.getString(2));
//                des.setText(c.getString(3));
//                double getPrice = Double.parseDouble(c.getString(7));
//                price.setText("₱"+getPrice);
//
//                String getStatus = c.getString(10);
//                if (getStatus.equals("open")) status.setText("Open for Betting");
//                else status.setText("Betting is Closed.");
//
//                slots.setText(c.getString(8)+" Slots");
//                date.setText(c.getString(6));
//                String getWinner = c.getString(9);
//                if(getWinner.equals("0")||getWinner.equals("")) winner.setText("Unknown");
//                else{
//                    winner.setText(getWinner);
//                }
//
//                type.setText(c.getString(5));
//
//                byte[]image = c.getBlob(4);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
//                item_image.setImageBitmap(bitmap);
//
//                owner_id = c.getString(1);
//            }
//        }else{
//            Toast.makeText(this, "Failed to fetch info.", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), home.class));
//            finish();
//        }
//        c.close();
//    }
}