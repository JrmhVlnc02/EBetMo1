package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

public class notification extends AppCompatActivity {
    ImageButton close;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    SessionManager sessionManager;
    String current_session_id;
    RecyclerView recyclerView, coin_rv;
    NotifyAdapter notifyAdapter;
    CoinAdapter coinAdapter;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        close = findViewById(R.id.close);
        recyclerView = findViewById(R.id.notification_rv);
        coin_rv = findViewById(R.id.TopUp_rv);


        try{
            sessionManager = new SessionManager(this);
            dbHelper = new DBHelper(getApplicationContext());
            sqLiteDatabase = dbHelper.getWritableDatabase();
            dialog = new Dialog(this);
            current_session_id = sessionManager.getId();
            displayNotification(current_session_id);
            displayTopUp(current_session_id);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void displayTopUp(String current_session_id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/display_topup.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            ArrayList<ModelCoin> modelCoins = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0 ) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String owner = jsonObject.getString("owner_id");
                                    String context = jsonObject.getString("amount");
                                    String date = jsonObject.getString("date");
                                    modelCoins.add(new ModelCoin(id,owner,context,date));

                                }
                                coinAdapter = new CoinAdapter(getApplicationContext(),R.layout.single_topup, modelCoins,sqLiteDatabase,dbHelper);
                                coin_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL, false));
                                coin_rv.setAdapter(coinAdapter);
                            }else coin_rv.setVisibility(View.GONE);
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
                paramV.put("user_id1", sessionManager.getId());
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    private void displayNotification(String current_session_id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/display_topup.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            ArrayList<NotifyModel> notifyModels = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0 ) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);




                                    int id = jsonObject.getInt("id");
                                    String owner_id = jsonObject.getString("owner_id");
                                    String item_id = jsonObject.getString("item_id");
                                    String item_owner_id = jsonObject.getString("item_owner_id");
                                    String image = jsonObject.getString("item_image");
                                    String status = jsonObject.getString("status");
                                    String chose = jsonObject.getString("chosen_number");
                                    String date = jsonObject.getString("date");
                                    notifyModels.add(new NotifyModel(id,owner_id,item_id,item_owner_id,status,chose,image,date));

                                }
                                notifyAdapter = new NotifyAdapter(getApplicationContext(),R.layout.single_notify,notifyModels,sqLiteDatabase,dbHelper,sessionManager,dialog);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL, false));
                                recyclerView.setAdapter(notifyAdapter);
                            }else recyclerView.setVisibility(View.GONE);
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
                paramV.put("user_id", sessionManager.getId());
                return paramV;
            }
        };
        queue.add(stringRequest);

    }



//    private void displayNotification(String current_session_id) {
//        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM bets WHERE owner_id=? ORDER BY id DESC", new String[]{current_session_id});
//        ArrayList<NotifyModel> notifyModels = new ArrayList<>();
//        if (c.getCount()>0){
//            recyclerView.setVisibility(View.VISIBLE);
//            while(c.moveToNext()){
//                int id = c.getInt(0);
//                String owner_id = c.getString(1);
//                String item_id = c.getString(2);
//                String item_owner_id = c.getString(3);
//                byte[] image = c.getBlob(4);
//                String status = c.getString(5);
//                String chose = c.getString(6);
//                String date = c.getString(7);
//
//                notifyModels.add(new NotifyModel(id,owner_id,item_id,item_owner_id,status,chose,image,date));
//
//            }
//            notifyAdapter = new NotifyAdapter(this,R.layout.single_notify,notifyModels,sqLiteDatabase,dbHelper,sessionManager,dialog);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
//            recyclerView.setAdapter(notifyAdapter);
//            c.close();
//
//        }else {
//            recyclerView.setVisibility(View.GONE);
//        }
//    }
}