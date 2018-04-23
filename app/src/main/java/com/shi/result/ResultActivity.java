package com.shi.result;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.shi.R;
import com.shi.bean.ShiBean;
import com.shi.db.ShiDao;

import java.util.ArrayList;

/**
 * Author: Yunr
 * Date: 2018-04-19 14:12
 */
public class ResultActivity extends AppCompatActivity {

    private String query;

    private RecyclerView list;
    private ResultAdapter adapter;
    private View emptyView;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        query = getIntent().getStringExtra("query");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("搜索\"" + query + "\"的结果");
        list = findViewById(R.id.query_result_list);
        emptyView = findViewById(R.id.empty_layout);
        loadView = findViewById(R.id.load_view);
        new Thread() {
            public ShiDao shiDao;

            @Override
            public void run() {
                shiDao = new ShiDao(ResultActivity.this);
                ShiBean[] shiBeans = shiDao.findAll();
                ArrayList<ShiBean> queryResult = new ArrayList<>();
                for (int i = 0; i < shiBeans.length; i++) {
                    ShiBean shiBean = shiBeans[i];
                    if (shiBean.getAuthor().contains(query) || shiBean.getDetail().contains(query)
                            || shiBean.getTitle().contains(query)) {
                        queryResult.add(shiBean);
                    }
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
