package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class home extends AppCompatActivity {
    TextView coin;
    SessionManager sessionManager;
    Button account, home, notification;
    RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    ImageButton popMenu;
    DBHelper dbHelper;
    ItemAdapter itemAdapter;
    String Verification, myid;
    double my_coin;
    LinearLayout empty;
    Dialog dialog;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findId();


        try{
           sessionManager = new SessionManager(this);
           dbHelper = new DBHelper(getApplicationContext());
           sqLiteDatabase = dbHelper.getWritableDatabase();
           dialog = new Dialog(getApplicationContext());
           displayItems_online();
           my_coin = Double.parseDouble(sessionManager.getCoin());
           coin.setText("₱"+my_coin);
           Verification = sessionManager.getValid();
            calendar = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

           myid = sessionManager.getId();


        }catch (Exception e){
            e.printStackTrace();


        }


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home.class));
                finish();
            }
        });







        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), account.class));
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), notification.class));
            }
        });

        popMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu(view);
            }
        });


    }

    private void mainMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(home.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.add_item){
                    if (Verification.equals("Verified"))
                        startActivity(new Intent(getApplicationContext(), add_item.class));
                    else Toast.makeText(home.this, "You are not Verified yet.", Toast.LENGTH_SHORT).show();

                }

                if (menuItem.getItemId() == R.id.add_coin){
                    startActivity(new Intent(getApplicationContext(), get_coin.class));

                }

                return false;
            }
        });
        popupMenu.show();

    }

    private void displayItems_online(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/get_items.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            ArrayList<ModelItem> modelItems = new ArrayList<>();
                            empty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0 ) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id1 = jsonObject.getInt("id");
                                    String owner = jsonObject.getString("owner_id");
                                    String name = jsonObject.getString("item_name");
                                    String Des = jsonObject.getString("item_description");
                                    String itemImage = jsonObject.getString("item_image");
                                    String type = jsonObject.getString("type");
                                    String time = jsonObject.getString("time");
                                    String price = jsonObject.getString("price");
                                    String slots = jsonObject.getString("slots");
                                    modelItems.add(new ModelItem(id1, name, Des, itemImage, type, time, owner, slots, price));

                                }
                                itemAdapter = new ItemAdapter(getApplicationContext(), R.layout.single_item, modelItems, sqLiteDatabase, dbHelper, sessionManager, dialog, calendar, simpleDateFormat);

                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setAdapter(itemAdapter);
                            }else{
                                empty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
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
                paramV.put("param", "abc");
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    private void displayItems() {
//        Cursor item = sqLiteDatabase.rawQuery("SELECT * FROM items ORDER BY id DESC", null);
//        ArrayList<ModelItem> modelItems = new ArrayList<>();
//        if(item.getCount() > 0){
//            empty.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//
//            while (item.moveToNext()) {
//                int id1 = item.getInt(0);
//                String owner = item.getString(1);
//                String name = item.getString(2);
//                String Des = item.getString(3);
//                byte[] itemImage = item.getBlob(4);
//                String type = item.getString(5);
//                String time = item.getString(6);
//                String price = item.getString(7);
//                String slots = item.getString(8);
//                modelItems.add(new ModelItem(id1,name,Des,itemImage,type,time,owner,slots,price));
//
//            }
//            itemAdapter = new ItemAdapter(this,R.layout.single_item,modelItems,sqLiteDatabase,dbHelper,sessionManager,dialog,calendar,simpleDateFormat);
//
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
//            recyclerView.setLayoutManager(gridLayoutManager);
//            recyclerView.setAdapter(itemAdapter);
//
//            item.close();
//
//        }
//
//        else {
//            empty.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//        }

    }

    private void findId() {
        recyclerView = findViewById(R.id.item_rv);
        coin = findViewById(R.id.coin);
        account = findViewById(R.id.account);
        home = findViewById(R.id.home);
        empty = findViewById(R.id.empty);
        popMenu = findViewById(R.id.main_menu);
        notification = findViewById(R.id.notification);
    }
}