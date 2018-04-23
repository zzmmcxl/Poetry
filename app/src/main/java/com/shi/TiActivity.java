package com.shi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shi.bean.User;
import com.shi.db.UserDao;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TiActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ti_fen)
    TextView tiFen;
    @BindView(R.id.query_result_list)
    LinearLayout queryResultList;
    @BindView(R.id.empty_layout)
    TextView emptyLayout;
    @BindView(R.id.load_view)
    ProgressBar loadView;
    private SharedPreferences sharedPreferences;
    private String userName;
    private User userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ti);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("答题评测");
        sharedPreferences = getSharedPreferences("shici", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("user", "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserDao userDao = new UserDao(getBaseContext());
        userBean = userDao.findByNamePwd(userName);
        if (userBean != null) {
            tiFen.setText(userBean.getUnit() + "");
        } else {
            tiFen.setText("0");
        }
    }

    public void doDaTi(View view) {
        startActivity(new Intent(getBaseContext(),DoTiActivity.class));
    }

    public void showPaiHang(View view) {
        startActivity(new Intent(getBaseContext(),PaiActivity.class));
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
