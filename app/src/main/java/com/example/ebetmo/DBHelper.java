package com.example.ebetmo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "betting.db";
    public static final int VER = 1;

    public DBHelper(@Nullable Context context ) {
        super(context, DBNAME, null, VER);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(id INTEGER primary key AUTOINCREMENT,fullName TEXT, contact TEXT, address TEXT, email TEXT, password TEXT, profile BLOB, verified TEXT, b_day TEXT, coin TEXT)");
        db.execSQL("create table items(id INTEGER primary key AUTOINCREMENT, owner_id TEXT, item_name TEXT, item_description TEXT, item_image BLOB, type TEXT, time TEXT, price TEXT, slots TEXT, winner TEXT, status TEXT)");
        db.execSQL("create table verification(id INTEGER primary key AUTOINCREMENT, owner_id TEXT, name TEXT,age TEXT, house TEXT, street TEXT, brgy TEXT, city TEXT, province TEXT, zip TEXT, id_type TEXT, id_picture BLOB)");
        db.execSQL("create table bets(id INTEGER primary key AUTOINCREMENT, owner_id TEXT, item_id TEXT, item_owner_id TEXT, item_image BLOB, status TEXT, chosen_number TEXT, bet_date TEXT)");
        db.execSQL("create table topUp(id INTEGER primary key AUTOINCREMENT, owner_id TEXT, amount TEXT, date TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists users");
        db.execSQL("drop Table if exists items");
        db.execSQL("drop Table if exists verification");
        db.execSQL("drop Table if exists bets");
        db.execSQL("drop Table if exists topUp");
    }

    public boolean check_email(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where email=?", new String[]{email});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean check_emails_pass(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where email=? and password=?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkSlot(String owner, String item){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor checkSlot = db.rawQuery("SELECT * FROM bets WHERE owner_id =? and item_id=?", new String[]{owner, item});

        if (checkSlot.getCount()>0){
            checkSlot.close();
            return true;
        }else {
            return false;
        }


    }



    public int totalBet(String item_id){
        SQLiteDatabase db = this.getWritableDatabase();
        int total;
        Cursor c = db.rawQuery("SELECT * FROM bets WHERE item_id = ?", new String[]{item_id});
        total = c.getCount();
        return total;

    }
}
