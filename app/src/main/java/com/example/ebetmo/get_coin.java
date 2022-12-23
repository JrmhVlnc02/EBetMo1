package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class get_coin extends AppCompatActivity {
    Button five, ten, twenty, fifty, one_hundred, two_hundred, five_hundred, one_thousand, two_thousand, three_thousand,four_thousand,five_thousand, proceed;
    TextView my_coin;
    ImageButton close;
    Dialog dialog;
    String user_id_session;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    double myCoin, chosenCoin, chosenTotal;

    SessionManager sessionManager;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_coin);
        ten = findViewById(R.id.ten);
        twenty = findViewById(R.id.twenty);
        fifty = findViewById(R.id.fifty);
        two_hundred = findViewById(R.id.two_hundred);
        one_hundred = findViewById(R.id.one_hundred);
        five_hundred = findViewById(R.id.five_hundred);
        one_thousand = findViewById(R.id.one_thousand);
        two_thousand = findViewById(R.id.two_thousand);
        three_thousand = findViewById(R.id.three_thousand);
        five_thousand = findViewById(R.id.five_thousand);
        my_coin = findViewById(R.id.add_coin);
        close = findViewById(R.id.close);
        four_thousand = findViewById(R.id.four_thousand);
        five = findViewById(R.id.five);
        proceed = findViewById(R.id.proceed);



        try{
            sessionManager = new SessionManager(getApplicationContext());
            dbHelper = new DBHelper(getApplicationContext());
            sqLiteDatabase = dbHelper.getWritableDatabase();
            myCoin = Double.parseDouble(sessionManager.getCoin());
            my_coin.setText("₱"+myCoin);
            dialog = new Dialog(get_coin.this);

            user_id_session = sessionManager.getId();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), home.class));
                    finish();

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home.class));
                finish();
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 5;
                AddDialog();
            }
        });
        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 10;
                AddDialog();
            }
        });

        twenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 20;
                AddDialog();
            }
        });

        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 50;
                AddDialog();
            }
        });

        one_hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 100;
                AddDialog();
            }
        });

        two_hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 200;
                AddDialog();
            }
        });

        five_hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 500;
                AddDialog();
            }
        });

        one_thousand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 1000;
                AddDialog();
            }
        });

        two_thousand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 2000;
                AddDialog();
            }
        });

        three_thousand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 3000;
                AddDialog();
            }
        });

        four_thousand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 4000;
                AddDialog();
            }
        });

        five_thousand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenCoin = 5000;
                AddDialog();
            }
        });




    }

    private void AddDialog() {
        dialog.setContentView(R.layout.add_coin_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        Button ok = dialog.findViewById(R.id.ok_btn);
        Button cancel = dialog.findViewById(R.id.cancel_btn);
        TextView context = dialog.findViewById(R.id.add_coin_context);
        String coin_message = "Add ₱"+chosenCoin+" to your coin?";
        context.setText(coin_message);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenTotal = chosenCoin + myCoin;

                Toast.makeText(get_coin.this, "Coin Added.", Toast.LENGTH_SHORT).show();
                my_coin.setText("₱"+chosenTotal);


                sessionManager.setCoin(String.valueOf(chosenTotal));

                updateUserCoin(String.valueOf(chosenTotal));
//
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("coin", String.valueOf(chosenTotal));
//
//
//
//
//                long result = sqLiteDatabase.update("users",contentValues,"id="+user_id_session,null);
//                if(result==-1){
//                    Toast.makeText(getApplicationContext(), "error updating coin", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getApplicationContext(), "your coin is updated", Toast.LENGTH_SHORT).show();
//
//                }

                insertTopUp(user_id_session, String.valueOf(chosenCoin));

                startActivity(new Intent(getApplicationContext(), get_coin.class));
                finish();
                dialog.dismiss();

            }
        });


        dialog.show();

    }

    private void updateUserCoin(String total) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/update_user_coin.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Success")) Toast.makeText(getApplicationContext(), "your coin is updated", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(getApplicationContext(), "error updating coin", Toast.LENGTH_SHORT).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getLocalizedMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("coin", total);
                paramV.put("user_id", sessionManager.getId());
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    private void insertTopUp(String user_id_session, String chosen) {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String Date = simpleDateFormat.format(calendar.getTime());

//        ContentValues contentValues = new ContentValues();
//        contentValues.put("amount", chosen);
//        contentValues.put("owner_id", user_id_session);
//        contentValues.put("date", Date);
//
//        long result = sqLiteDatabase.insert("topUp",null,contentValues);
//        if(result==-1){
//            Toast.makeText(getApplicationContext(), "Something problem in Inserting coin!", Toast.LENGTH_SHORT).show();
//
//        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/topup.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getLocalizedMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("amount", chosen);
                paramV.put("owner_id", user_id_session);
                paramV.put("date", Date);
                return paramV;
            }
        };
        queue.add(stringRequest);


    }

//    private void insertTopUp(String user_id_session, String chosen) {
//        calendar = Calendar.getInstance();
//        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        String Date = simpleDateFormat.format(calendar.getTime());
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("amount", chosen);
//        contentValues.put("owner_id", user_id_session);
//        contentValues.put("date", Date);
//
//        long result = sqLiteDatabase.insert("topUp",null,contentValues);
//        if(result==-1){
//            Toast.makeText(getApplicationContext(), "Something problem in Inserting coin!", Toast.LENGTH_SHORT).show();
//
//        }
//
//
//    }
}