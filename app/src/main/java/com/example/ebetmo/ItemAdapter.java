package com.example.ebetmo;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.core.database.CursorWindowCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    Context context;
    int singleData;
    ArrayList<ModelItem> modelArrayList;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    SessionManager sessionManager;
    Dialog dialog;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    //Constructor


    public ItemAdapter(Context context, int singleData, ArrayList<ModelItem> modelArrayList, SQLiteDatabase sqLiteDatabase, DBHelper dbHelper, SessionManager sessionManager, Dialog dialog,Calendar calendar,SimpleDateFormat simpleDateFormat) {
        this.context = context;
        this.singleData = singleData;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.dbHelper = dbHelper;
        this.sessionManager = sessionManager;
        this.dialog = dialog;
        this.calendar = calendar;
        this.simpleDateFormat = simpleDateFormat;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ModelItem modelItem = modelArrayList.get(position);
        String image = modelItem.getItemImage();

        Picasso.with(context).load("http://"+ final_ip.IP_ADDRESS +"/ebetmo_final/"+image).into(holder.item_image);
        holder.item_name.setText(modelItem.getItemName());
        holder.item_description.setText(modelItem.getDescription());
        double price = Double.parseDouble(modelItem.getPrice());
        holder.item_price.setText("₱"+price);
        holder.slots.setText(modelItem.getSlots()+" Slots");



        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, bet_item.class);
                String id = String.valueOf(modelItem.getItemId());
                i.putExtra("item_id", id);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

//                    intent
//                    context.startActivity(new Intent(context, bet_item.class));


//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
//                View v = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
//                    ImageView itemImage1 = v.findViewById(R.id.item_image);
//                    TextView itemName, itemDes, itemPrice,itemSlots, itemDate, close_txt;
//                    itemName = v.findViewById(R.id.item_name);
//                    itemDes = v.findViewById(R.id.item_des);
//                    itemPrice = v.findViewById(R.id.item_price);
//                    itemSlots = v.findViewById(R.id.item_slots);
//                    itemDate = v.findViewById(R.id.item_date);
//                    Button bet = v.findViewById(R.id.bet);
//                    EditText choose = v.findViewById(R.id.choose_input);
//                    TextView more = v.findViewById(R.id.more);
//                    LinearLayout bet_panel = v.findViewById(R.id.bet_panel);
//                    close_txt = v.findViewById(R.id.close_betting);
//                    ImageButton info = v.findViewById(R.id.view_info);


//                bottomSheetDialog.setContentView(v);
//                bottomSheetDialog.show();

//                info.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        try {
//                            dialog.setContentView(R.layout.bet_info);
//                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                            dialog.setCancelable(false);
//                            Button ok = dialog.findViewById(R.id.ok_btn);
//
//                            ok.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dialog.dismiss();
//                                }
//                            });
//                            dialog.show();
//                        } catch (Exception e) {
//                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//                });
//
//
//
//
//
//                itemName.setText(modelItem.getItemName());
//                itemDes.setText(modelItem.getDescription());
//                itemDate.setText(modelItem.getDate());
//                itemPrice.setText("₱"+price);
////                Picasso.with(context).load("http://192.168.0.122/ebetmo_final/"+image).into(item_image1);
//
//                itemSlots.setText(modelItem.getSlots()+ " Slots");

//                String currentStatus = "close";
//                Cursor c = sqLiteDatabase.rawQuery("SELECT status FROM items WHERE id = ?", new String[]{String.valueOf(modelItem.getItemId())});
//                if (c.getCount()>0){
//                    while(c.moveToNext()){
//                        currentStatus = c.getString(0);
//                    }
//
//                }else Toast.makeText(context, "Failed to fetch item status", Toast.LENGTH_SHORT).show();
//                c.close();
//
//                if(currentStatus.equals("open")){
//                    bet_panel.setVisibility(View.VISIBLE);
//                    close_txt.setVisibility(View.GONE);
//
//                }
//                else {
//                    bet_panel.setVisibility(View.GONE);
//                    close_txt.setVisibility(View.VISIBLE);
//                    close_txt.setText("Betting is Closed");
//                }

//                bet.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                        if (choose.getText().toString().equals("")) Toast.makeText(context, "Choose your Slot!", Toast.LENGTH_SHORT).show();
//                        else{
//
//                                int getSlots = Integer.parseInt(modelItem.getSlots());
//                                int chosen = Integer.parseInt(choose.getText().toString());
//                                if (chosen > getSlots)
//                                    Toast.makeText(context, "Choose at the given range of slots", Toast.LENGTH_SHORT).show();
//                                else{
//                                    String notify = "not_viewed";
//                                    calendar = Calendar.getInstance();
//                                    simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//                                    String Date = simpleDateFormat.format(calendar.getTime());
//                                    String chosen_number = choose.getText().toString();
//                                    ContentValues contentValues = new ContentValues();
//                                    contentValues.put("owner_id", sessionManager.getId());
//                                    contentValues.put("item_id", modelItem.getItemId());
//                                    contentValues.put("item_owner_id", modelItem.getOwner());
//                                    contentValues.put("item_image", modelItem.getItemImage());
//                                    contentValues.put("status", notify);
//                                    contentValues.put("chosen_number", chosen_number);
//                                    contentValues.put("bet_date", Date);
//
//                                    int TotalBets = dbHelper.totalBet(String.valueOf(modelItem.getItemId()));
//                                    if (TotalBets <= Integer.parseInt(modelItem.getSlots())){
//
//                                        boolean checkSlots = dbHelper.checkSlot(sessionManager.getId(),String.valueOf(modelItem.getItemId()));
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
//                                                            betDialog();
//                                                            updateCoins(remainingCoins);
//                                                            bottomSheetDialog.dismiss();
//
//                                                        }
//
//                                                    }
//
//
//                                                }
//
//                                            }else Toast.makeText(context, "Failed to get Coin", Toast.LENGTH_SHORT).show();
//                                            getCoin.close();
//
//                                        }
//
//                                    }else Toast.makeText(context, "Out of slots", Toast.LENGTH_SHORT).show();
//
//
//                                }
//
//
//
//
//                        }
//                    }
//                });
//
//                more.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        try{
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("item_id", modelItem.getItemId());
//                            bundle.putString("item_name", modelItem.getItemName());
//                            Intent intent=new Intent(context,view_item.class);
//                            intent.putExtra("item_data",bundle);
//                            context.startActivity(intent);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            Intent i = new Intent(context, home.class);
//                            context.startActivity(i);
//                            Toast.makeText(context, "Error: Try reload!", Toast.LENGTH_SHORT).show();
//
//                        }
//                        bottomSheetDialog.dismiss();
//                    }
//                });


            }


            private void betDialog() {
                dialog.setContentView(R.layout.bet_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                Button ok = dialog.findViewById(R.id.ok_btn);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

            private void updateCoins(double remainingCoins) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("coin", String.valueOf(remainingCoins));

                sessionManager.setCoin(String.valueOf(remainingCoins));


                long result = sqLiteDatabase.update("users",contentValues,"id="+sessionManager.getId(),null);
                if(result==-1){
                    Toast.makeText(context, "error updating coins", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "your coins is updated", Toast.LENGTH_SHORT).show();

                }

            }

        });




    }


    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_image;

        TextView item_name, item_description, item_price, slots;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.image_item);
            item_name = itemView.findViewById(R.id.item_name);
            item_description = itemView.findViewById(R.id.item_des);
            item_price = itemView.findViewById(R.id.item_price);
            slots = itemView.findViewById(R.id.slots);

        }
    }
}
