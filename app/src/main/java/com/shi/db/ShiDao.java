package com.shi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shi.bean.ShiBean;
import com.shi.bean.User;

/**
 * Author: Yunr
 * Date: 2018-04-20 14:59
 */
public class ShiDao {

    private DBHelper dbHelp;
    private SQLiteDatabase db;
    private Context context;

    public ShiDao(Context context) {
        dbHelp = new DBHelper(context);
        this.context = context;
    }

    public boolean save(ShiBean shiBean) {
        db = dbHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topic", shiBean.getTopic());
        values.put("title", shiBean.getTitle());
        values.put("detail", shiBean.getDetail());
        values.put("author", shiBean.getAuthor());
        values.put("dynasty", shiBean.getDynasty());
        values.put("translate", shiBean.getTranslate());
        values.put("appreciation", shiBean.getAppreciation());
        long id = db.insert("shi", null, values);
        db.close();
        if (id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ShiBean[] findAll() {
        db = dbHelp.getReadableDatabase();
        String sql = "select * from shi";
        Cursor cursor = db.rawQuery(sql, null);
        ShiBean[] data = new ShiBean[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            ShiBean shiBean = new ShiBean();
            shiBean.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            shiBean.setTopic(cursor.getString(cursor.getColumnIndex("topic")));
            shiBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            shiBean.setDetail(cursor.getString(cursor.getColumnIndex("detail")));
            shiBean.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            shiBean.setDynasty(cursor.getString(cursor.getColumnIndex("dynasty")));
            shiBean.setTranslate(cursor.getString(cursor.getColumnIndex("translate")));
            shiBean.setAppreciation(cursor.getString(cursor.getColumnIndex("appreciation")));
            data[i] = shiBean;
            i++;
        }
        db.close();
        return data;
    }

    public User findByNamePwd(String username) {
        User user = null;
        db = dbHelp.getReadableDatabase();
        String sql = "select _id,username,password,age,head from t_user where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});

        while (cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
            byte[] data = cursor.getBlob(cursor.getColumnIndex("head"));
            user.setHeadData(data);
        }

        db.close();
        return user;
    }

}
