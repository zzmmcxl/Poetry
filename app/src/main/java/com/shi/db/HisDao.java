package com.shi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shi.bean.ShiBean;

/**
 * Author: Yunr
 * Date: 2018-04-20 14:59
 */
public class HisDao {

    private DBHelper dbHelp;
    private SQLiteDatabase db;
    private Context context;

    public HisDao(Context context) {
        dbHelp = new DBHelper(context);
        this.context = context;
    }

    public boolean save(ShiBean shiBean, String username) {
        if (findItem(shiBean.getTitle(), shiBean.getDetail(), shiBean.getAuthor(), username)) {
            if (update(shiBean, username)) {
                return true;
            } else {
                return false;
            }
        }

        db = dbHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", shiBean.getId());
        values.put("topic", shiBean.getTopic());
        values.put("username", username);
        values.put("title", shiBean.getTitle());
        values.put("detail", shiBean.getDetail());
        values.put("author", shiBean.getAuthor());
        values.put("dynasty", shiBean.getDynasty());
        values.put("translate", shiBean.getTranslate());
        values.put("appreciation", shiBean.getAppreciation());
        values.put("date",System.currentTimeMillis()+"");
        long id = db.insert("history", null, values);
        db.close();

        if (id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update(ShiBean shiBean, String username) {
        db = dbHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", shiBean.getId());
        values.put("topic", shiBean.getTopic());
        values.put("username", username);
        values.put("title", shiBean.getTitle());
        values.put("detail", shiBean.getDetail());
        values.put("author", shiBean.getAuthor());
        values.put("dynasty", shiBean.getDynasty());
        values.put("translate", shiBean.getTranslate());
        values.put("appreciation", shiBean.getAppreciation());
        values.put("date", System.currentTimeMillis() + "");
        long id = db.update("history", values,
                "username=? and _id=?",
                new String[]{username,shiBean.getId()+""});
        db.close();
        if (id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ShiBean[] findAll(String username) {
        db = dbHelp.getReadableDatabase();
        String sql = "select * from history where username=? ORDER BY date DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if (cursor.getCount() == 0) {
            return null;
        }

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

    public boolean findItem(String title, String detail, String author, String userName) {
        db = dbHelp.getReadableDatabase();
        String sql = "select title,detail,author from history where username=? and" +
                " title=? and detail=? and author=?";
        Cursor cursor = db.rawQuery(sql, new String[]{userName, title, detail, author});
        boolean has = false;
        while (cursor.moveToNext()) {
            String mTitle = cursor.getString(cursor.getColumnIndex("title"));
            String mDetail = cursor.getString(cursor.getColumnIndex("detail"));
            String mAuthor = cursor.getString(cursor.getColumnIndex("author"));
            if (mTitle.contentEquals(title) && mDetail.contentEquals(detail)
                    && mAuthor.contentEquals(author)) {
                has = true;
                break;
            }
        }
        db.close();
        return has;
    }

}
