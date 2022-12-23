package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class bet_item extends AppCompatActivity {
    ImageView close;
    String item_id, item_slot, item_owner, item_image_bet, item_price_bet;
    ImageView item_image;
    TextView item_name, item_description, item_price, slots, itemDate, close_txt;
    Dialog dialog;
     String currentStatus = "";
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    DBHelper dbHelper;
    SessionManager sessionManager;
    public boolean  slot_status;
    public boolean  check_chosen;
    double remainingCoins;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_item);
        close = findViewById(R.id.close);
        item_image = findViewById(R.id.item_image);
        item_name = findViewById(R.id.item_name);
        item_description = findViewById(R.id.item_des);
        item_price = findViewById(R.id.item_price);
        slots = findViewById(R.id.item_slots);
        itemDate = findViewById(R.id.item_date);
        LinearLayout bet_panel = findViewById(R.id.bet_panel);
        close_txt = findViewById(R.id.close_betting);
        ImageButton info = findViewById(R.id.view_info);
        TextView more = findViewById(R.id.more);
        Button bet = findViewById(R.id.bet);
         EditText choose = findViewById(R.id.choose_input);
        sessionManager = new SessionManager(this);

        dialog = new Dialog(getApplicationContext());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        item_id = extras.getString("item_id");
        if(item_id != null){

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url ="http://"+ final_ip.IP_ADDRESS +"/ebetmo_final/single_item.php";

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


                                        item_name.setText(jsonObject.getString("item_name"));
                                        item_description.setText(jsonObject.getString("item_description"));
                                        item_price_bet =jsonObject.getString("price");
                                        item_price.setText("₱ "+item_price_bet);
                                        item_slot = jsonObject.getString("slots");
                                        item_owner = jsonObject.getString("owner_id");
                                        item_image_bet = jsonObject.getString("item_image");
                                        slots.setText(item_slot+" Slots");
                                        itemDate.setText(jsonObject.getString("time"));
                                        Picasso.with(getApplicationContext()).load("http://"+ final_ip.IP_ADDRESS +"/ebetmo_final/"+jsonObject.getString("item_image")).into(item_image);
                                        currentStatus = jsonObject.getString("status");

                                        if(currentStatus.equals("open")){
                                        bet_panel.setVisibility(View.VISIBLE);

                                        close_txt.setText("Open for Betting");
                                            close_txt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.yellow));
                                            close_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

                                        }else {
                                            bet_panel.setVisibility(View.GONE);
                                            close_txt.setVisibility(View.VISIBLE);
                                            close_txt.setText("Betting is Closed");
                                        }



                                    }

                                }else{
                                    Toast.makeText(bet_item.this, "Empty Record!", Toast.LENGTH_SHORT).show();
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

        }else {
            Toast.makeText(getApplicationContext(), "ITEM ID IS EMPTY!", Toast.LENGTH_SHORT).show();
            finish();}



                        more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            Intent i = new Intent(getApplicationContext(), view_item.class);

                            i.putExtra("item_id", item_id);
                            startActivity(i);
                        }catch (Exception e){
                            e.printStackTrace();
                            Intent i = new Intent(getApplicationContext(), bet_item.class);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "Error: Try reload!", Toast.LENGTH_SHORT).show();

                        }

                    }
                });






        info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            dialog = new Dialog(bet_item.this);
                            dialog.setContentView( R.layout.bet_info);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.setCancelable(false);
                            Button ok = dialog.findViewById(R.id.ok_btn);

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();



                    }
                });


                bet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (choose.getText().toString().equals("")) Toast.makeText(getApplicationContext(), "Choose your Slot!", Toast.LENGTH_SHORT).show();
                        else{

                                int getSlots = Integer.parseInt(item_slot);
                                int chosen = Integer.parseInt(choose.getText().toString());
                                if (chosen > getSlots)
                                    Toast.makeText(getApplicationContext(), "Choose at the given range of slots", Toast.LENGTH_SHORT).show();
                                else{
                                    String notify = "not_viewed";
                                    calendar = Calendar.getInstance();
                                    simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String Date = simpleDateFormat.format(calendar.getTime());
                                    String chosen_number = choose.getText().toString();



//                                    check status volley


                                    slot_status = checkSlot(sessionManager.getId(),item_id);
                                    if (slot_status)
                                        Toast.makeText(getApplicationContext(), "You already bet on this item.", Toast.LENGTH_SHORT).show();
                                    else {
                                        try{
                                                double coins = Double.parseDouble(sessionManager.getCoin());
                                                double price = Double.parseDouble(item_price_bet);

                                            if (coins < price) Toast.makeText(getApplicationContext(), "Out of Coin! Try Top Up more coins.", Toast.LENGTH_SHORT).show();
                                            else{

                                                if(check_num(item_id, chosen_number)) Toast.makeText(getApplicationContext(), "Slot is already occupied!", Toast.LENGTH_SHORT).show();
                                                else{

//                                                                                        MY VOLLEYBALL
                                                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                                    String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/bet_item.php";

                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    if(response.equals("Success")){
                                                                        Toast.makeText(getApplicationContext(), "Bet Successfully", Toast.LENGTH_SHORT).show();
                                                                        try{
                                                                            betDialog();
                                                                            remainingCoins = coins - price;
                                                                            updateCoins(remainingCoins);


                                                                        }catch (Exception e){
                                                                            Toast.makeText(bet_item.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                                        }





                                                                    }else
                                                                        Toast.makeText(bet_item.this, response, Toast.LENGTH_SHORT).show();

                                                                }
                                                            }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Log.e("Error", error.getLocalizedMessage());
                                                        }
                                                    }){
                                                        protected Map<String, String> getParams(){
                                                            Map<String, String> paramV = new HashMap<>();


                                                            paramV.put("owner_id", sessionManager.getId());
                                                            paramV.put("item_id", item_id);
                                                            paramV.put("item_owner_id", item_owner);
                                                            paramV.put("item_image", item_image_bet);
                                                            paramV.put("status", notify);
                                                            paramV.put("chosen_number", chosen_number);
                                                            paramV.put("bet_date", Date);

                                                            return paramV;
                                                        }
                                                    };
                                                    queue.add(stringRequest);



//                                    END OF VOLLEY

                                                }

                                            }



                                        }catch (Exception e){
                                            Toast.makeText(bet_item.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }












//                                    int TotalBets = dbHelper.totalBet(String.valueOf(item_id));


//                                    int TotalBets = get_totalBet(item_id);
//                                    if (TotalBets <= Integer.parseInt(item_slot)){
//

//                                        if (checkSlots)
//                                            Toast.makeText(context, "You already bet on this item.", Toast.LENGTH_SHORT).show();
//                                        else{
//                                            Cursor getCoin = sqLiteDatabase.rawQuery("SELECT coin FROM users WHERE id=?", new String[]{sessionManager.getId()});
//                                            if (getCoin.getCount()>0){
//                                                double coins = 0;
//                                                double price = Double.parseDouble(modelItem.getPrice());
//                                                double remainingCoins;
//
//
//                                                while (getCoin.moveToNext()){
//                                                    coins = Double.parseDouble(getCoin.getString(0));
//                                                }
//                                                if (coins < price)
//                                                    Toast.makeText(context, "Out of Coin! Try Top Up more coins.", Toast.LENGTH_SHORT).show();
//                                                else{
//
//                                                    Cursor number = sqLiteDatabase.rawQuery("SELECT chosen_number FROM bets where item_id =?",new String[]{String.valueOf(modelItem.getItemId())});
//                                                    if (number.getCount()>0){
//
//                                                        String fetchNumber = "0";
//                                                        while(number.moveToNext()){
//                                                            fetchNumber = number.getString(0);
//                                                        }
//                                                        if (chosen_number.equals(fetchNumber))
//                                                            Toast.makeText(context, "Slot is already occupied!", Toast.LENGTH_SHORT).show();
//                                                        else{
//
//                                                            long result = sqLiteDatabase.insert("bets",null,contentValues);
//                                                            if(result==-1){
//                                                                Toast.makeText(context, "Something problem in Inserting Data", Toast.LENGTH_SHORT).show();
//                                                            }else{
//                                                                betDialog();
//                                                                remainingCoins = coins - price;
//                                                                updateCoins(remainingCoins);
//
//                                                                bottomSheetDialog.dismiss();
//
//                                                            }
//
//                                                        }
//
//                                                    }else{
//
//                                                        long result = sqLiteDatabase.insert("bets",null,contentValues);
//                                                        if(result==-1){
//                                                            Toast.makeText(context, "Something problem in Inserting Data", Toast.LENGTH_SHORT).show();
//                                                        }else{
//                                                            Toast.makeText(context, "Bet Successfully", Toast.LENGTH_SHORT).show();
//                                                            remainingCoins = coins - price;
//
//                                                        }
//
//                                                    }
//
//
//                                                }
//
//                                            }else Toast.makeText(getApplicationContext(), "Failed to get Coin", Toast.LENGTH_SHORT).show();
//                                            getCoin.close();
//
//                                        }
//
//                                    }else Toast.makeText(getApplicationContext(), "Out of slots", Toast.LENGTH_SHORT).show();


                                }




                        }
                    }


                });






    }

    private void updateCoins(double remainingCoins) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/bet_item.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Success")){
                            Toast.makeText(bet_item.this, "Coin Updated!", Toast.LENGTH_SHORT).show();
                            sessionManager.setCoin(String.valueOf(remainingCoins));
                        }else
                            Toast.makeText(bet_item.this, response, Toast.LENGTH_SHORT).show();

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
                paramV.put("coin", String.valueOf(remainingCoins));

                return paramV;
            }
        };
        queue.add(stringRequest);

    }

    private void betDialog() {
        dialog = new Dialog(bet_item.this);
        dialog.setContentView(R.layout.bet_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        Button ok = dialog.findViewById(R.id.ok_btn);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(bet_item.this, home.class));
                finish();

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public boolean check_num(String item_id, String chosen_number) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/check_num.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Success")) check_chosen = true;
                        else check_chosen = false;


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getLocalizedMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();


                paramV.put("chosen", chosen_number);
                paramV.put("item_id", item_id);

                return paramV;
            }
        };
        queue.add(stringRequest);
        return check_chosen;

    }

    public boolean checkSlot(String current, String item_id){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/bet_item.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0 ) {
                                slot_status = true;

                            }else slot_status = false;
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


                paramV.put("current", current);
                paramV.put("item_id", item_id);

                return paramV;
            }
        };
        queue.add(stringRequest);
        return slot_status;
    }







}