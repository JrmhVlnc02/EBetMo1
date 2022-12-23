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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {
    Context context;
    int singleData;
    ArrayList<NotifyModel> modelArrayList;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    SessionManager sessionManager;
    Dialog dialog;

    public NotifyAdapter(Context context, int singleData, ArrayList<NotifyModel> modelArrayList, SQLiteDatabase sqLiteDatabase, DBHelper dbHelper, SessionManager sessionManager, Dialog dialog) {
        this.context = context;
        this.singleData = singleData;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.dbHelper = dbHelper;
        this.sessionManager = sessionManager;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public NotifyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_notify,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.ViewHolder holder, int position) {
        final NotifyModel notifyModel = modelArrayList.get(position);
        String image = notifyModel.getItemImage();
        Picasso.with(context).load("http://"+ final_ip.IP_ADDRESS +"/ebetmo_final/"+image).into(holder.item_image);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
//        holder.item_image.setImageBitmap(bitmap);

        String fullWinner = "";
        String itemName = "";

        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putInt("item_id", Integer.parseInt(notifyModel.getItem_id()));
                    Intent intent=new Intent(context,view_item.class);
                    intent.putExtra("item_data",bundle);
                    context.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Intent i = new Intent(context, home.class);
                    context.startActivity(i);
                    Toast.makeText(context, "Error: Try reload!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        try{



            String itemId= notifyModel.getItem_id();
            Cursor name = sqLiteDatabase.rawQuery("SELECT * FROM items WHERE id=? ", new String[]{itemId});
            while (name.moveToNext()) {
                String n =name.getString(2);
                holder.name.setText(n);
                holder.date.setText(name.getString(6));
                String getWinner="";
                String user_id = "0";
                String fullName = "";
                getWinner = name.getString(9);
                itemName = n;



                Cursor search = sqLiteDatabase.rawQuery("SELECT owner_id FROM bets WHERE chosen_number = ?",new String[]{getWinner});
                while(search.moveToNext()) {
                    user_id = search.getString(0);
                    Cursor getName = sqLiteDatabase.rawQuery("SELECT fullName FROM users WHERE id=?",new String[]{user_id});
                    while (getName.moveToNext()) fullName = getName.getString(0);
                    getName.close();
                }
                search.close();



                if (getWinner.equals("")||getWinner.equals("0")) {
                    holder.winner.setText("Unknown");
                    fullWinner = "Unknown";
                }else{
                    if (fullName.equals("")){
                        holder.winner.setText(getWinner +" : No Winner");
                        fullWinner = getWinner +" : No Winner";
                    }
                    else{
                        holder.winner.setText(getWinner +" : "+fullName);
                        fullWinner = getWinner +" : "+fullName;
                    }
                }


            }
            name.close();

            String ownerId = notifyModel.getItem_owner_id();
            Cursor owner = sqLiteDatabase.rawQuery("SELECT fullName FROM users WHERE id=?", new String[]{ownerId});
            while(owner.moveToNext()) holder.owner.setText(owner.getString(0));


            String betStatus = "";
            Cursor notify = sqLiteDatabase.rawQuery("SELECT status FROM bets WHERE owner_id=?", new String[]{notifyModel.getOwner_id()});
            while(notify.moveToNext()) betStatus = notify.getString(0);

            notify.close();

            String itemStatus = "";
            Cursor item = sqLiteDatabase.rawQuery("SELECT status FROM items WHERE id=?", new String[]{notifyModel.getItem_id()});
            while(item.moveToNext()) itemStatus = item.getString(0);
            item.close();

            if (itemStatus.equals("close") && betStatus.equals("not_viewed")){
                dialog.setContentView(R.layout.notify_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                ImageView item_pic = dialog.findViewById(R.id.item_image);
                ImageButton close = dialog.findViewById(R.id.close);
                TextView item_name, winner_name;
                Button view, ok;
                view = dialog.findViewById(R.id.view_btn);
                ok = dialog.findViewById(R.id.ok_btn);
                winner_name = dialog.findViewById(R.id.winner_name);
                item_name = dialog.findViewById(R.id.item_name);


                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                String picture = notifyModel.getItemImage();
                Picasso.with(context).load("http://"+ final_ip.IP_ADDRESS +"/ebetmo_final/"+picture).into(item_pic);
//                Bitmap bit = BitmapFactory.decodeByteArray(picture, 0,picture.length);
//                item_pic.setImageBitmap(bit);

                winner_name.setText(fullWinner);
                item_name.setText(itemName);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String status = "viewed";
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("status", status);


                        long result = sqLiteDatabase.update("bets",contentValues,"owner_id=?",new String[]{ notifyModel.getOwner_id()});
                        if(result==-1){
                            Toast.makeText(context, "error updating Notification", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try{
                            String status = "viewed";
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("status", status);


                            long result = sqLiteDatabase.update("bets",contentValues,"owner_id=?",new String[]{notifyModel.getOwner_id()});
                            if(result==-1){
                                Toast.makeText(context, "error updating Notification", Toast.LENGTH_SHORT).show();
                            }


                            Bundle bundle = new Bundle();
                            bundle.putInt("item_id",Integer.parseInt(notifyModel.getItem_id()));
                            bundle.putInt("owner_id",Integer.parseInt(notifyModel.getItem_owner_id()));
                            Intent intent=new Intent(context,raffle.class);
                            intent.putExtra("item_data",bundle);
                            context.startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(context, "Error: Try Again!", Toast.LENGTH_SHORT).show();

                        }

                        dialog.dismiss();
                    }
                });




                dialog.show();
            }else{
                dialog.dismiss();
            }



        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Error. Reload App!", Toast.LENGTH_SHORT).show();
        }




    }




    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_image;
        TextView name, winner, owner, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.notify_image);
            name = itemView.findViewById(R.id.item_name);
            winner = itemView.findViewById(R.id.item_winner);
            owner = itemView.findViewById(R.id.owner);
            date = itemView.findViewById(R.id.item_date);
        }
    }
}
