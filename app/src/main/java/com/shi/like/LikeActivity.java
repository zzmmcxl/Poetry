package com.shi.like;

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

import com.shi.R;
import com.shi.bean.ShiBean;
import com.shi.db.LikeDao;
import com.shi.result.ResultAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class LikeActivity extends AppCompatActivity {

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
                    emptyView.setVisibility(View.GONE);
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
        getSupportActionBar().setTitle("个人收藏");
        sharedPreferences = getSharedPreferences("shici", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("user", "");
        list = findViewById(R.id.query_result_list);
        emptyView = findViewById(R.id.empty_layout);
        loadView = findViewById(R.id.load_view);

        emptyView.setText("暂无收藏记录");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread() {
            LikeDao likeDao;

            @Override
            public void run() {
                likeDao = new LikeDao(LikeActivity.this);
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
