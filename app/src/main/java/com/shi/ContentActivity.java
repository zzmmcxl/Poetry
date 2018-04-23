package com.shi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.shi.bean.ShiBean;
import com.shi.db.HisDao;
import com.shi.db.LikeDao;

/**
 * Author: Yunr
 * Date: 2018-04-20 16:35
 */
public class ContentActivity extends AppCompatActivity {

    private ShiBean shiBean;
    private TextView author;
    private TextView detail;
    private TextView tralstion;
    private TextView shang;
    private LikeDao likeDao;
    private boolean like;
    private SharedPreferences sharedPreferences;
    private String userName;
    private HisDao hisDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        author = findViewById(R.id.content_author);
        detail = findViewById(R.id.content_detail);
        tralstion = findViewById(R.id.content_tralstion);
        shang = findViewById(R.id.content_shang);
        sharedPreferences = getSharedPreferences("shici", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("user", "");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shiBean = (ShiBean) getIntent().getSerializableExtra("data");
        if (shiBean != null) {
            getSupportActionBar().setTitle(shiBean.getTitle());
            author.setText(shiBean.getDynasty() + "·" + shiBean.getAuthor());
            detail.setText(shiBean.getDetail());
            tralstion.setText(shiBean.getTranslate());
            shang.setText(shiBean.getAppreciation());

            hisDao = new HisDao(getBaseContext());
            hisDao.save(shiBean, userName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        likeDao = new LikeDao(getBaseContext());
        MenuItem lickItem = menu.findItem(R.id.contetn_like);
        if (likeDao.findItem(shiBean.getTitle(), shiBean.getDetail(), shiBean.getAuthor(), userName)) {
            lickItem.setIcon(R.drawable.ic_favorite_n);
            like = true;
        } else {
            lickItem.setIcon(R.drawable.ic_favorite_o);
            like = false;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.contetn_like) {
            like = !like;
            if (like) {
                item.setIcon(R.drawable.ic_favorite_n);
                if (likeDao.save(shiBean, userName)) {
                    Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                item.setIcon(R.drawable.ic_favorite_o);
                if (likeDao.del(shiBean, userName)) {
                    Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
