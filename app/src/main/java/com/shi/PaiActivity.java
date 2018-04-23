package com.shi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shi.bean.User;
import com.shi.db.UserDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaiActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pai_name)
    TextView paiName;
    @BindView(R.id.pai_unit)
    TextView paiUnit;
    @BindView(R.id.pai_lv)
    TextView paiLv;
    @BindView(R.id.pai_group)
    LinearLayout paiGroup;
    @BindView(R.id.content_group)
    ScrollView contentGroup;
    @BindView(R.id.load_layout_pai)
    ProgressBar loadLayoutPai;
    @BindView(R.id.empty_layout_pai)
    TextView emptyLayoutPai;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<User> list = (List<User>) msg.obj;
            if (list == null || list.size() == 0) {
                loadLayoutPai.setVisibility(View.GONE);
                contentGroup.setVisibility(View.GONE);
                emptyLayoutPai.setVisibility(View.VISIBLE);
            }

            loadPaiData(list);
            loadLayoutPai.setVisibility(View.GONE);
            contentGroup.setVisibility(View.VISIBLE);
            emptyLayoutPai.setVisibility(View.GONE);
        }
    };

    private void loadPaiData(List<User> list) {
        paiGroup.removeAllViews();

        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            View view = getLayoutInflater().inflate(R.layout.pai_item, null, false);
            TextView num = view.findViewById(R.id.pai_item_num);
            TextView name = view.findViewById(R.id.pai_item_name);
            TextView unit = view.findViewById(R.id.pai_item_unit);
            TextView lv = view.findViewById(R.id.pai_item_lv);
            num.setText((i+1)+"");
            name.setText(user.getUserName());
            unit.setText(user.getUnit() + "");
            float unitCount = user.getUnit();
            float f = (unitCount / 15f) * 100;
            lv.setText(String.format("%.0f%%", f));
            paiGroup.addView(view);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pai);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("排行榜");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLayoutPai.setVisibility(View.VISIBLE);
        contentGroup.setVisibility(View.GONE);
        emptyLayoutPai.setVisibility(View.GONE);
        new Thread() {
            @Override
            public void run() {
                UserDao userDao = new UserDao(getBaseContext());
                List<User> list = userDao.findAllUnit();
                Message msg = handler.obtainMessage();
                msg.obj = list;
                handler.sendMessage(msg);
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
