package com.shi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shi.adapter.HomeAdapter;
import com.shi.bean.ShiBean;
import com.shi.bean.User;
import com.shi.board.BoardActivity;
import com.shi.db.ShiDao;
import com.shi.db.UserDao;
import com.shi.like.LikeActivity;
import com.shi.me.MeActivity;
import com.shi.result.ResultActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SearchView mSearchView;
    private ImageView mSearchButton;
    private View logout;
    private ImageView dashBoard;
    private String user;
    private ImageView headIcon;
    private TextView userName;
    private DrawerLayout drawer;
    private View headLayout;
    private SharedPreferences sharedPreferences;

    //注册，登录，更改个人信息（头像，年龄等），添加个人收藏，查看历史浏览痕迹

    private String[] boardTopic = {"五言绝句", "七言绝句", "五言律诗", "七言律诗", "五言古诗", "七言古诗", "乐府"};
    private String[] authorTopic = {"元稹", "王之涣", "王建", "王维", "白居易", "西鄙人", "李白", "李商隐", "李端", "宋之问", "杜甫", "孟浩然", "金昌绪", "柳宗元", "韦应物", "祖咏", "张祜", "贾岛", "裴迪", "刘长卿", "权德舆", "王昌龄", "王翰", "朱庆馀", "岑参", "李益", "杜牧", "柳中庸", "韦庄", "张旭", "张泌", "张继", "陈陶", "贺知章", "温庭筠", "刘方平", "刘禹锡", "郑畋", "韩翃", "韩偓", "顾况", "王勃", "王湾", "司空曙", "杜荀鹤", "杜审言", "沈佺期", "李隆基", "马戴", "崔涂", "常建", "张九龄", "张乔", "张籍", "许浑", "皎然", "刘昚虚", "卢纶", "钱起", "骆宾王", "戴叔伦", "李颀", "皇甫冉", "秦韬玉", "高适", "崔曙", "崔颢", "薛逢", "元结", "丘为", "綦毋潜", "陈子昂", "韩愈", "孟郊"};

    private TabLayout tabLayout;
    private ShiDao shiDao;
    private ShiBean[] shiBeans;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("shici", Context.MODE_PRIVATE);
        drawer = findViewById(R.id.drawer_layout);
        logout = findViewById(R.id.logout);
        dashBoard = findViewById(R.id.dash_board);
        tabLayout = findViewById(R.id.tab_layout);
        recyclerView = findViewById(R.id.recycler_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        headLayout = navigationView.getHeaderView(0).findViewById(R.id.head_layout);
        headIcon = navigationView.getHeaderView(0).findViewById(R.id.head_icon_n);
        userName = navigationView.getHeaderView(0).findViewById(R.id.user_name_n);

        headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MeActivity.class));
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navigationView.setItemTextColor(getResources().getColorStateList(R.color.bg_blank));
        navigationView.setItemBackgroundResource(R.color.bg_write);

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString("user", "").apply();
                startActivity(new Intent(getBaseContext(), SplashActivity.class));
                finish();
            }
        });

        dashBoard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), BoardActivity.class), 11);
            }
        });

        shiDao = new ShiDao(getBaseContext());
        shiBeans = shiDao.findAll();
        for (int i = 0; i < boardTopic.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(boardTopic[i]));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (adapter != null) {
                    if (index == 0) {
                        adapter.filterBoard(tab.getText());
                        recyclerView.scrollToPosition(0);
                    } else {
                        adapter.filterAuthor(tab.getText());
                        recyclerView.scrollToPosition(0);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        adapter = new HomeAdapter(shiBeans);
        recyclerView.setAdapter(adapter);
        adapter.filterBoard(boardTopic[0]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            index = data.getIntExtra("data", 0);
            if (index == 0) {//按类型
                tabLayout.removeAllTabs();
                for (int i = 0; i < boardTopic.length; i++) {
                    tabLayout.addTab(tabLayout.newTab().setText(boardTopic[i]));
                }
                tabLayout.setScrollPosition(0, 0, true);
                tabLayout.scrollTo(0, 0);
                adapter.filterBoard(boardTopic[0]);
            } else {
                tabLayout.removeAllTabs();
                for (int i = 0; i < authorTopic.length; i++) {
                    tabLayout.addTab(tabLayout.newTab().setText(authorTopic[i]));
                }

                tabLayout.scrollTo(0, 0);
                tabLayout.setScrollPosition(0, 0, true);
                adapter.filterAuthor(authorTopic[0]);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = sharedPreferences.getString("user", "");
        UserDao userDao = new UserDao(getBaseContext());
        User userBean = userDao.findByNamePwd(user);
        if (userBean != null) {
            userName.setText(userBean.getUserName());
            Bitmap head = userBean.getHead();
            if (head != null) {
                headIcon.setImageBitmap(head);
            } else {
                byte[] img = userBean.getHeadByte();
                if (img != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                    headIcon.setImageBitmap(bitmap);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_grade) {//收藏
            startActivity(new Intent(getBaseContext(), LikeActivity.class));
        } else if (id == R.id.nav_restore) {//浏览记录
            startActivity(new Intent(getBaseContext(), HisActivity.class));
        }else if (id == R.id.nav_ti) {//答题评测
            startActivity(new Intent(getBaseContext(), TiActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint("搜索诗词");
        mSearchView.setIconifiedByDefault(false);
        SearchView.SearchAutoComplete editText = mSearchView.findViewById(R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.text_write));
        ImageView imageView = mSearchView.findViewById(R.id.search_mag_icon);
        imageView.setImageDrawable(null);//设置资源文件为null
        final ImageView closeBtn = mSearchView.findViewById(R.id.search_close_btn);
        closeBtn.setVisibility(View.VISIBLE);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.onActionViewCollapsed();
            }
        });
        mSearchView.setSubmitButtonEnabled(true);
        mSearchButton = mSearchView.findViewById(R.id.search_go_btn);
        mSearchButton.setImageResource(R.drawable.ic_search);
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtn.setVisibility(View.VISIBLE);
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                intent.putExtra("query", query);
                Log.e("----", "Start");
                startActivity(intent);
                mSearchView.onActionViewCollapsed();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.onActionViewCollapsed();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
