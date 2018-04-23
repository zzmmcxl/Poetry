package com.shi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shi.adapter.TiPageAdapter;
import com.shi.bean.TiBean;
import com.shi.bean.User;
import com.shi.db.UserDao;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoTiActivity extends AppCompatActivity implements BeforeNextListener {

    private TiPageAdapter adapter;
    ArrayList<TiBean> data;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            data = (ArrayList<TiBean>) msg.obj;
            if (data == null || data.size() <= 0) {
                emptyLayout.setText("暂无题目");
                emptyLayout.setVisibility(View.VISIBLE);
                queryResultList.setVisibility(View.GONE);
                loadView.setVisibility(View.GONE);
                return;
            }

            adapter = new TiPageAdapter(data, DoTiActivity.this);
            queryResultList.setOffscreenPageLimit(adapter.getCount());
            queryResultList.setAdapter(adapter);
            queryResultList.setCurrentItem(0, true);
            queryResultList.setVisibility(View.VISIBLE);
            loadView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
        }
    };

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    NoScrollViewPager queryResultList;
    @BindView(R.id.empty_layout)
    TextView emptyLayout;
    @BindView(R.id.load_view)
    ProgressBar loadView;
    private SharedPreferences sharedPreferences;
    private String userName;
    private User userBean;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_ti);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("答题评测");
        new Thread() {
            @Override
            public void run() {
                initTi();
            }
        }.start();
        loadView.setVisibility(View.VISIBLE);
        sharedPreferences = getSharedPreferences("shici", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("user", "");
        userDao = new UserDao(getBaseContext());
        userBean = userDao.findByNamePwd(userName);
    }

    private String[] tiEdit = {
            "1、“大风起兮云飞扬，威加海内兮归故乡，安得猛士兮守四方。”是汉高祖刘邦酒酣时创作的《大风歌》，请问，刘邦创作这首诗的时候，是通过打击什么乐器来传达豪情的？",
            "2、《孔雀东南飞》当中，刘兰芝是多少岁学习弹箜篌的？",
            "3、“若言琴上有琴声，放在匣中何不鸣？若言声在指头上，何不于君指上听？”出自于谁的笔下的？",
            "4、“雁柱十三弦，一一春笃语。”出自于欧阳修的《生查子 含羞整翠鬟》，其中的“雁柱十三弦”是指哪种乐器？",
            "5、“半销宿酒头仍重，新脱冬衣体乍轻。睡觉心空思想尽，近来乡梦不多成。”请问诗中的“睡觉”是什么意思？",
            "6、白居易因为有爱酒的嗜好，为自己起了一个什么雅号？",
            "7、白居易的作品《阿崔》中的“阿崔”和白居易是什么关系？",
            "8、“国耻未雪，何由成名。”这一名句出自于李白的《独漉篇》，这首诗当中的“国耻”是指什么？",
            "9、诗句“八月涛声吼地来，头高数丈触山回。”是描述杭州的哪个景点？",
            "10、王维的作品《酬张少府》是写给张九龄的，请问诗中的“渔歌”指的是什么？"
    };

    private String[] keyEdit = {
            "筑", "15", "苏轼", "古筝", "睡醒", "醉吟先生", "父子", "安史之乱", "钱塘江", "隐士之歌"
    };

    private String[] tiSelect = {
            "11、柳永自称奉旨填词柳三变，以毕生精力作词，并以“白衣卿相”自诩，请问下面哪个选项和柳永有关？",
            "12、下列哪位诗人没有做过官",
            "13、以下哪句诗不是描写爱情的？",
            "14、下列哪句诗描写的是秋季？",
            "15、古诗词中形容月亮的诗句甚多，下列哪一句与月亮无关"
    };
    private String[][] selectStr = {
            {"A、吊柳会", "B、柳侯祠", "C、柳河东"},
            {"A、孟浩然", "B、辛弃疾", "C、李绅"},
            {"A、春蚕到死丝方尽，蜡炬成灰泪始干。", "B、落红不是无情物，化作春泥更护花。", "C、曾经沧海难为水，除却巫山不是云。"},
            {"A、随风潜入夜，润物细无声。", "B、胜日寻芳泗水滨，无边光景一时新。", "C、不堪红叶青苔地，又是凉风暮雨天。"},
            {"A、团团冰镜吐清辉", "B、玉蝉离海上", "C、大珠小珠落玉盘（琵琶行）"}
    };

    private String[] keySelect = {
            "A、吊柳会", "A、孟浩然", "B、落红不是无情物，化作春泥更护花。", "C、不堪红叶青苔地，又是凉风暮雨天。", "C、大珠小珠落玉盘（琵琶行）"
    };

    private void initTi() {
        ArrayList<TiBean> tiBeans = new ArrayList<>();
        for (int i = 0; i < tiEdit.length; i++) {
            tiBeans.add(new TiBean(tiEdit[i], keyEdit[i]));
        }

        for (int i = 0; i < tiSelect.length; i++) {
            tiBeans.add(new TiBean(tiSelect[i], selectStr[i], keySelect[i]));
        }

        Message msg = handler.obtainMessage();
        msg.obj = tiBeans;
        handler.sendMessage(msg);
    }

    boolean isExit = false;

    @Override
    public void onBackPressed() {
        if (!isExit) {
            Toast.makeText(this, "中途退出答题不计成绩，再次点击返回键退出答题", Toast.LENGTH_SHORT).show();
            isExit = true;
            new CountDownTimer(3000, 3000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    isExit = false;
                }
            }.start();
        } else {
            finish();
        }
    }

    @Override
    public void onBefore(int index) {
        queryResultList.setCurrentItem(index, true);
    }

    @Override
    public void onNext(int index) {
        queryResultList.setCurrentItem(index, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ti, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ti_submit || item.getItemId() == android.R.id.home) {
            int unit = adapter.getAllUnit();
            Toast.makeText(this, "答题完毕!", Toast.LENGTH_SHORT).show();
            userBean.setUnit(unit);
            if (userDao.update(userBean, System.currentTimeMillis())) {
                finish();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
