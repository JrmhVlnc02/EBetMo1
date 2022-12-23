package com.example.ebetmo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BettorsAdapter extends RecyclerView.Adapter<BettorsAdapter.ViewHolder>{
    Context context;
    int singleData;
    ArrayList<NotifyModel> modelArrayList;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;

    public BettorsAdapter(Context context, int singleData, ArrayList<NotifyModel> modelArrayList, SQLiteDatabase sqLiteDatabase, DBHelper dbHelper) {
        this.context = context;
        this.singleData = singleData;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public BettorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_bettors,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BettorsAdapter.ViewHolder holder, int position) {
        final NotifyModel notifyModel = modelArrayList.get(position);

        holder.user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id",Integer.parseInt(notifyModel.getOwner_id()));
                    Intent intent=new Intent(context,view_user.class);
                    intent.putExtra("user_data",bundle);
                    context.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context, "Error: Try Again!", Toast.LENGTH_SHORT).show();

                }
            }
        });



//        try{
//            String user_id = notifyModel.getOwner_id();
//            Cursor user = sqLiteDatabase.rawQuery("SELECT fullName, profile FROM users WHERE id=?", new String[]{user_id});
//
//            while (user.moveToNext()){
//                holder.user_name.setText(user.getString(0));
//
//                byte[]image = user.getBlob(1);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
//                holder.user_image.setImageBitmap(bitmap);
//
//
//            }
            Picasso.with(context).load("http://"+ final_ip.IP_ADDRESS +"/ebetmo_final/"+notifyModel.getItemImage()).into(holder.user_image);

//            Cursor bet = sqLiteDatabase.rawQuery("SELECT chosen_number FROM bets WHERE item_id =? and owner_id =?", new String[]{notifyModel.getItem_id(),user_id});
//            while(bet.moveToNext()){
//                holder.slot_number.setText(bet.getString(0));
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            Intent i = new Intent(context, home.class);
//            context.startActivity(i);
//            Toast.makeText(context, "Error: Try again!", Toast.LENGTH_SHORT).show();
//
//        }



    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user_image;
        TextView user_name, slot_number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.bettor_image);
            user_name = itemView.findViewById(R.id.bettor_name);
            slot_number = itemView.findViewById(R.id.bettor_slot);
        }
    }
}
