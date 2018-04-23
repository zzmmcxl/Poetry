package com.shi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author: Yunr
 * Date: 2018-04-19 15:47
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "shici", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table t_user(" +
                "_id integer primary key autoincrement," +
                "username text,password text,age integer,head BLOB,unit integer,date text)";
        db.execSQL(sql);
        String sql2 = "create table shi(" +
                "_id integer primary key autoincrement," +
                "topic text," +
                "title text,detail text,author text,dynasty text," +
                "translate text,appreciation text)";
        db.execSQL(sql2);

        String sql3 = "create table like_a(username text,_id integer,topic text,title text," +
                "detail text,author text,dynasty text," +
                "translate text,appreciation text,date text)";
        db.execSQL(sql3);

        String history = "create table history(username text," +
                "_id integer,topic text,title text," +
                "detail text,author text,dynasty text," +
                "translate text,appreciation text,date text)";
        db.execSQL(history);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
