package com.shi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shi.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Yunr
 * Date: 2018-04-19 17:38
 */
public class UserDao {

    private DBHelper dbHelp;
    private SQLiteDatabase db;
    private Context context;

    public UserDao(Context context) {
        dbHelp = new DBHelper(context);
        this.context = context;
    }

    public boolean save(User user) {
        db = dbHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUserName());
        values.put("password", user.getPassword());
        values.put("age", user.getAge());
        values.put("unit", 0);
        values.put("date", System.currentTimeMillis()+"");
        if (user.getHeadByte() != null) {
            values.put("head", user.getHeadByte());
        }

        long id = db.insert("t_user", null, values);
        db.close();
        if (id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update(User user) {
        db = dbHelp.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUserName());
        contentValues.put("password", user.getPassword());
        contentValues.put("age", user.getAge());
        byte[] data = user.getHeadByte();
        contentValues.put("head", data);
        contentValues.put("unit", user.getUnit());
        int a = db.update("t_user", contentValues,
                "_id=?", new String[]{user.getId() + ""});
        db.close();
        return a != -1;
    }

    public boolean update(User user, long time) {
        db = dbHelp.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUserName());
        contentValues.put("password", user.getPassword());
        contentValues.put("age", user.getAge());
        byte[] data = user.getHeadByte();
        contentValues.put("head", data);
        contentValues.put("unit", user.getUnit());
        contentValues.put("date", time + "");
        int a = db.update("t_user", contentValues,
                "_id=?", new String[]{user.getId() + ""});
        db.close();
        return a != -1;
    }

    public void del(int id) {
        db = dbHelp.getWritableDatabase();
        String sql = "delete from t_user where _id = ?";
        db.execSQL(sql, new Object[]{id});
        db.close();
    }

    public List<User> findAllUnit() {
        List<User> list = new ArrayList<User>();
        db = dbHelp.getReadableDatabase();
        String sql = "select username,unit from t_user ORDER BY unit DESC";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            User user = new User();
            user.setUserName(cursor.getString(cursor.getColumnIndex("username")));
            user.setUnit(cursor.getInt(cursor.getColumnIndex("unit")));
            list.add(user);
        }
        db.close();
        return list;
    }

    public User findByID(int id) {
        User user = null;
        db = dbHelp.getReadableDatabase();
        String sql = "select _id,username,password from t_user where _id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});

        while (cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        }

        db.close();
        return user;
    }

    public User findByNamePwd(String username) {
        User user = null;
        db = dbHelp.getReadableDatabase();
        String sql = "select _id,username,password,age,head,unit from t_user where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});

        while (cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
            user.setUnit(cursor.getInt(cursor.getColumnIndex("unit")));
            byte[] data = cursor.getBlob(cursor.getColumnIndex("head"));
            user.setHeadData(data);
        }

        db.close();
        return user;
    }


}
