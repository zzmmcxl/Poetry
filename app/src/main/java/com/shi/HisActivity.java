package com.shi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.shi.bean.ShiBean;
import com.shi.db.HisDao;
import com.shi.result.ResultAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Yunr
 * Date: 2018-04-21 16:21
 */
public class HisActivity extends AppCompatActivity {

    private RecyclerView list;
    private ResultAdapter adapter;
    private TextView emptyView;
    private View loadView;
    ArrayList<ShiBean> data;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                data = (ArrayList<ShiBean>) msg.obj;
                if (data.size() > 0) {
                    adapter = new ResultAdapter(data);
                    list.setAdapter(adapter);
                    loadView.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                    loadView.setVisibility(View.GONE);
                }
            }
        }
    };
    private SharedPreferences sharedPreferences;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("浏览记录");
        sharedPreferences = getSharedPreferences("shici", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("user", "");
        list = findViewById(R.id.query_result_list);
        emptyView = findViewById(R.id.empty_layout);
        loadView = findViewById(R.id.load_view);
        emptyView.setText("暂无浏览记录");
        new Thread() {
            HisDao likeDao;

            @Override
            public void run() {
                likeDao = new HisDao(HisActivity.this);
                ShiBean[] shiBeans = likeDao.findAll(userName);
                ArrayList<ShiBean> queryResult = new ArrayList<>();
                if (shiBeans != null) {
                    queryResult.addAll(Arrays.asList(shiBeans));
                }

                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = queryResult;
                handler.sendMessage(message);
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
