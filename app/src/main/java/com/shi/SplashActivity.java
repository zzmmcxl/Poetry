package com.shi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shi.bean.Data;
import com.shi.bean.ShiBean;
import com.shi.db.ShiDao;
import com.shi.login.LoginActivity;
import com.shi.login.RegisterActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private String[] permissions = {
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE,
    };

    private boolean hasDataBase;
    private String user;
    private View view;
    private SharedPreferences sharedPreferences;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(SplashActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();
                    loadView.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private View loadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("shici", Context.MODE_PRIVATE);
        user = sharedPreferences.getString("user", "");
        view = findViewById(R.id.btn_layout);
        loadView = findViewById(R.id.load_layout);
        loadView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndPermission.with(getBaseContext()).permission(permissions).onDenied(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                Toast.makeText(SplashActivity.this, "请手动开启需要的权限", Toast.LENGTH_SHORT).show();
                AndPermission.permissionSetting(getBaseContext());
                finish();
            }
        })
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        hasDataBase = sharedPreferences.getBoolean("hasDataBase", false);
                        if (hasDataBase) {
                            if (!TextUtils.isEmpty(user)) {
                                view.setVisibility(View.GONE);
                                new CountDownTimer(2000, 2000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                                        finish();
                                    }
                                }.start();
                            }
                        }

                        if (!hasDataBase) {//无数据库
                            Toast.makeText(SplashActivity.this, "请稍候，正在导入数据", Toast.LENGTH_SHORT).show();
                            loadView.setVisibility(View.VISIBLE);
                            new Thread() {
                                @Override
                                public void run() {
                                    if (loadDataBase()) {
                                        handler.sendEmptyMessage(1);
                                        if (!TextUtils.isEmpty(user)) {
                                            view.setVisibility(View.GONE);
                                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                                            finish();
                                        }
                                    }
                                }
                            }.start();
                        }
                    }
                }).start();
    }

    private boolean loadDataBase() {
        try {
            InputStream inputStream = getAssets().open("data.json");
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            byte[] bytes = new byte[inputStream.available()];
            dataInputStream.readFully(bytes);
            sharedPreferences.edit().putBoolean("hasDataBase", true).apply();
            Gson gson = new Gson();
            Data data = gson.fromJson(new String(bytes), Data.class);
            ShiDao shiDao = new ShiDao(getBaseContext());
            for (int i = 0; i < data.getFather().size(); i++) {
                Data.FatherBean fatherBean = data.getFather().get(i);
                String topic = fatherBean.getTopic();
                for (int j = 0; j < fatherBean.getDes().size(); j++) {
                    Data.FatherBean.DesBean desBean = fatherBean.getDes().get(j);
                    ShiBean shiBean = desBean.toShiBean(topic);
                    if (shiDao.save(shiBean)) {
                    } else {
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void login(View view) {
        startActivityForResult(new Intent(this, LoginActivity.class), 11);
    }

    public void register(View view) {
        startActivityForResult(new Intent(this, RegisterActivity.class), 11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            finish();
        }
    }

//
//    {
//        ShiDao shiDao = new ShiDao(getBaseContext());
//        ShiBean[] data = shiDao.findAll();
//        ArrayList<String> authorList = new ArrayList<>();
//
//        for (ShiBean shiBean : data) {
//            boolean has = false;
//            String author = shiBean.getDynasty();
//            if (!TextUtils.isEmpty(author)) {
//                for (int i = 0; i < authorList.size(); i++) {
//                    if (author.contentEquals(authorList.get(i))) {
//                        has = true;
//                        break;
//                    }
//                }
//                if (!has) {
//                    authorList.add(author);
//                }
//            }
//        }
//
//        StringBuilder builder = new StringBuilder("");
//        for (String a : authorList) {
//            builder.append("\"").append(a).append("\",");
//        }
//        Log.e("-----", builder.toString());
//    }
}
