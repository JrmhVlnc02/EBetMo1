package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class raffle extends AppCompatActivity {
    ImageButton close;
    String item_id, owner_id,winnerNum, current_user_id;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    SessionManager sessionManager;
    TextView item_name, item_des, item_price, win_num, slot_total, win_name;
    ImageView item_image;
    RecyclerView recyclerView;
    BettorsAdapter bettorsAdapter;
    int TotalSlots , bettorCount;
    Button draw, view_winner;
    LoadingDialog loadingDialog;
    boolean checkWinner;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raffle);
        close = findViewById(R.id.close);
        item_name = findViewById(R.id.item_name);
        item_des = findViewById(R.id.item_des);
        item_price = findViewById(R.id.item_price);
        item_image = findViewById(R.id.item_image);
        win_num = findViewById(R.id.win_num);
        slot_total = findViewById(R.id.slot_total);
        win_name = findViewById(R.id.win_name);
        recyclerView = findViewById(R.id.bettors_rv);
        draw = findViewById(R.id.btn_draw);
        view_winner = findViewById(R.id.btn_view_winner);

        try{
            dbHelper = new DBHelper(getApplicationContext());
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sessionManager = new SessionManager(this);
            loadingDialog = new LoadingDialog(this);
            current_user_id = sessionManager.getId();
            dialog = new Dialog(this);


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }

        if(getIntent().getBundleExtra("item_data")!=null){
            Bundle bundle = getIntent().getBundleExtra("item_data");
            item_id = String.valueOf(bundle.getInt("item_id"));
            owner_id = String.valueOf(bundle.getInt("owner_id"));

            displayItemInfo(item_id);
            displayBettors(item_id);

            if (checkWinner) draw.setVisibility(View.GONE);
            else{
                if(owner_id.equals(current_user_id)) draw.setVisibility(View.VISIBLE);
                else draw.setVisibility(View.GONE);
            }

        }else{
            Toast.makeText(this, "Failed to get item Info. Try again!", Toast.LENGTH_SHORT).show();
            finish();
        }



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.draw_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                Button yes, no;
                yes = dialog.findViewById(R.id.yes_btn);
                no = dialog.findViewById(R.id.no_btn);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (bettorCount == 0){
                            Toast.makeText(raffle.this, "No Bettors!", Toast.LENGTH_SHORT).show();

                        }else {


                            draw.setVisibility(View.GONE);

                            Random generator = new Random();


                            int nIntFromET = TotalSlots;
                            int x =generator.nextInt(nIntFromET);

                            win_num.setText(Integer.toString(x)); // Show the random number
                            winnerNum = Integer.toString(x);
                            view_winner.setVisibility(View.VISIBLE);
                        }
                        dialog.dismiss();


                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();





            }
        });

        view_winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_winner.setVisibility(View.GONE);
                win_name.setVisibility(View.VISIBLE);
                String betClose = "close";

                ContentValues contentValues = new ContentValues();
                contentValues.put("winner", winnerNum);
                contentValues.put("status", betClose);

                long result = sqLiteDatabase.update("items",contentValues,"id="+item_id,null);
                if(result==-1){
                    Toast.makeText(getApplicationContext(), "error updating winner", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "winner updated", Toast.LENGTH_SHORT).show();

                }

                displayWinner(winnerNum);
            }
        });
    }

    private void displayWinner(String winner) {
        String user_id = "0";
        Cursor c = sqLiteDatabase.rawQuery("SELECT owner_id FROM bets WHERE item_id =? and chosen_number = ?", new String[]{item_id,winner});
        if (c.getCount()>0){
            while(c.moveToNext()) user_id = c.getString(0);
            displayUserWinner(user_id);
        }else{
            win_name.setText("No Winner");
        }
        c.close();




    }

    private void displayUserWinner(String user_id) {
        Cursor c = sqLiteDatabase.rawQuery("SELECT fullName FROM users WHERE id=?", new String[]{user_id});
        if (c.getCount()>0){
            while(c.moveToNext()) win_name.setText(c.getString(0));
        }else Toast.makeText(this, "Failed to get winner name. try reload!", Toast.LENGTH_SHORT).show();
        c.close();
    }

    private void displayBettors(String item_id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://" + final_ip.IP_ADDRESS + "/ebetmo_final/display_item_notif.php";

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
                                bettorsAdapter = new BettorsAdapter(getApplicationContext(),R.layout.single_bettors,notifyModels,sqLiteDatabase,dbHelper);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL, false));
                                recyclerView.setAdapter(bettorsAdapter);
                            }else {

                                Toast.makeText(getApplicationContext(), "Empty Bettors", Toast.LENGTH_SHORT).show();
                                draw.setVisibility(View.GONE);
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
                paramV.put("user_id", sessionManager.getId());
                return paramV;
            }
        };
        queue.add(stringRequest);


    }

//    private void displayBettors(String item_id) {
//        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM bets WHERE item_id=?", new String[]{item_id});
//        ArrayList<NotifyModel> notifyModels = new ArrayList<>();
//        if (c.getCount()>0){
//            draw.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.VISIBLE);
//            while(c.moveToNext()){
//                int id = c.getInt(0);
//                String owner_id = c.getString(1);
//                String items_id = c.getString(2);
//                String item_owner_id = c.getString(3);
//                String image = c.getBlob(4);
//                String status = c.getString(5);
//                String chose = c.getString(6);
//                String date = c.getString(7);
//
//                notifyModels.add(new NotifyModel(id,owner_id,items_id,item_owner_id,status,chose,image,date));
//
//            }
//            bettorsAdapter = new BettorsAdapter(this,R.layout.single_bettors,notifyModels,sqLiteDatabase,dbHelper);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
//            recyclerView.setAdapter(bettorsAdapter);
//            c.close();
//
//        }else
//        {
//            Toast.makeText(this, "Empty Bettors", Toast.LENGTH_SHORT).show();
//            draw.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.GONE);
//        }
//
//
//    }

    private void displayItemInfo(String item_id) {
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM items WHERE id=?", new String[]{item_id});
        bettorCount = c.getCount();
        if (bettorCount > 0){
            while (c.moveToNext()){
                item_name.setText(c.getString(2));
                item_des.setText(c.getString(3));
                item_price.setText("₱"+Double.parseDouble(c.getString(7)));

                byte[]image = c.getBlob(4);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
                item_image.setImageBitmap(bitmap);


                TotalSlots = Integer.parseInt(c.getString(8));


                slot_total.setText(TotalSlots+" Total Slots");

                String winner = c.getString(9);
                if (winner.equals("") || winner.equals("0"))  {
                    win_num.setText("Unknown");


                    if (owner_id.equals(current_user_id)) draw.setVisibility(View.VISIBLE);
                    else draw.setVisibility(View.GONE);
                    win_name.setVisibility(View.GONE);
                    view_winner.setVisibility(View.GONE);

                    checkWinner = false;
                }
                else {
                    winnerNum = winner;
                    win_num.setText(winner);
                    win_name.setVisibility(View.VISIBLE);
                    draw.setVisibility(View.GONE);
                    view_winner.setVisibility(View.GONE);
                    displayWinner(winner);
                    checkWinner = true;


                }

            }

        }else{
            Toast.makeText(this, "Empty item to be Fetch", Toast.LENGTH_SHORT).show();
            finish();
        }
        c.close();
    }

}