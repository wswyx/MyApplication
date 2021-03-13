package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.myapplication.DrawerMenu.MenuItemAdpter;
import com.example.myapplication.DrawerMenu.TabFragment.CommunitFragment;
import com.example.myapplication.DrawerMenu.TabFragment.HomeFragment;
import com.example.myapplication.DrawerMenu.TabFragment.MoreFragment;
import com.example.myapplication.DrawerMenu.TabFragment.SubscribeFragment;
import com.example.myapplication.DrawerMenu.TabFragment.WeatherFragment;
import com.example.myapplication.utils.SharedPreferencesUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Main2ActivityRepair extends AppCompatActivity {

    private static final String TAG = "尴尬咯";
    private static final String APP_ID = "101900088";

    private Handler mHandler;

    private Toolbar mtoolbar;

    private Button btn_Viewpager;

    protected DrawerLayout mDrawerLayout;
    private FrameLayout contentFrameLayout;
    private Fragment currentFragment;
    private List<Fragment> tabFragments = new ArrayList<>();


    private SwipeRefreshLayout swipeRefresh;
    private final TextView textView = null;
    private int currenttext = 0;
    String[] numbers = new String[]{"1号", "2号", "3号", "4号", "5号", "6号"};

    //腾讯
    private Tencent mTencent;
    private Main2ActivityRepair.BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private Button login;
    private TextView name;
    private ImageView img;
    boolean flag=false;

    //侧滑
    private ListView mListView;
    private List<com.example.myapplication.DrawerMenu.MenuItem> menuItemList = new ArrayList<>();
    private MenuItemAdpter adapter;
    private DrawerLayout drawerLayout;

    //声明Fragment
    private HomeFragment homeFragment;
    private CommunitFragment CommunitFragment;
    private WeatherFragment weatherFragment;
    private SubscribeFragment subscribeFragment;
    private MoreFragment MoreFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_repair);
        initToolBar();
        initView();
        intiData();
        clickEvents();
    }

    //toolbar(topmenu)
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }



    //初始化Toolbar(返回)
    private void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        login=(Button)findViewById(R.id.button_login);
        //设置标题颜色
        mtoolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用
    }


    //初始化toolbar(topmenu)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences qq = getSharedPreferences("qq", 0);
        String name = qq.getString("name", "");
        String img = qq.getString("img", "");

        switch (item.getItemId()) {
            case R.id.button_login:
            {
                Log.i(TAG, "onOptionsItemSelected: " + flag);

                String state = (String) SharedPreferencesUtils.get(this,"loginState", "");
                Log.i(TAG, "onOptionsItemSelected: " + state);
                if(state.equals("403")) {
                    item.setTitle("退出");
                    mIUiListener = new BaseUiListener();
                    mTencent.login(Main2ActivityRepair.this, "all", mIUiListener);//all表示获取所有权限
                    mUserInfo = new UserInfo(Main2ActivityRepair.this, mTencent.getQQToken()); //获取用户信息
                    mUserInfo.getUserInfo(mIUiListener);
                    if (name.equals("")&&img.equals("")){
                        this.name.setText("未登录");
                        this.img.setImageResource(R.mipmap.logo);
                    }else {
                        this.name.setText(name);
                        Glide.with(getApplicationContext()).load(img).into(this.img);
                    }
                }else if(state.equals("401")) {
                    item.setTitle("登录");
                    SharedPreferencesUtils.put(this,"loginState", "403"); // 未授权，未登录
                    String loginUser = (String) SharedPreferencesUtils.get(this,"loginUser", "");
                    Log.i(TAG, "onOptionsItemSelected: "+ loginUser); // json序列化
                    mTencent.logout(this);
                    this.name.setText("未登录");
                    this.img.setImageResource(R.mipmap.logo);
                }

//                if(!flag) {
//                    mIUiListener = new BaseUiListener();
//                    //all表示获取所有权限
//                    mTencent.login(Main2ActivityRepair.this, "all", mIUiListener);
//                    mUserInfo = new UserInfo(Main2ActivityRepair.this, mTencent.getQQToken()); //获取用户信息
//                    mUserInfo.getUserInfo(mIUiListener);
//                    if (name.equals("")&&img.equals("")){
//                        this.name.setText("未登录");
//                        this.img.setImageResource(R.mipmap.logo);
//                    }else {
//                        this.name.setText(name);
//                        Glide.with(getApplicationContext()).load(img).into(this.img);
//                    }
//                    item.setTitle("退出");
//                    flag=true;
//                }
//                else{
//                    mTencent.logout(this);
//                    this.name.setText("未登录");
//                    this.img.setImageResource(R.mipmap.logo);
//                    item.setTitle("登录");
//                    flag=false;
//                }
            }
                break;
            case R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //初始化侧拉菜单
    public void initListView() {
        String[] data_zh = getResources().getStringArray(R.array.menu_zh);
        String[] data_en = getResources().getStringArray(R.array.menu_en);
        for (int i = 0; i < data_zh.length; i++) {
            com.example.myapplication.DrawerMenu.MenuItem menuItem = new com.example.myapplication.DrawerMenu.MenuItem(data_zh[i], data_en[i]);
            menuItemList.add(menuItem);
        }
        adapter = new MenuItemAdpter(this, R.layout.menu_list_item, menuItemList);
        mListView.setAdapter(adapter);
    }


    //菜单点击事件
    public void clickEvents() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //先关闭侧滑，再加载内容（或许手感更加丝滑）
                drawerLayout.closeDrawers();
                adapter.changeSelected(position);
                //获取点击的选项
                com.example.myapplication.DrawerMenu.MenuItem menuItem = (com.example.myapplication.DrawerMenu.MenuItem) parent.getItemAtPosition(position);
                String selectZhText = menuItem.getItem_text_zh();
                //当点击后，设置标题文字
                mtoolbar.setTitle(selectZhText);
                if (selectZhText.equals("天气"))
                    switchFragment(weatherFragment);
                else if (selectZhText.equals("主页"))
                    switchFragment(homeFragment);
                else if (selectZhText.equals("订阅"))
                    switchFragment(subscribeFragment);
                else
                    Toast.makeText(getApplicationContext(), menuItem.getItem_text_zh() + ":还没准备好呢QAQ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //切换主视图的fragment，避免重复实例化加载
    // @param fragment 要切换的Fragment对象
    public void switchFragment(Fragment fragment) {
        //R.id.fl_content表示将Fragment内容放到容器id，把Fragment放到FrameLayout里
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, fragment)
                .commitAllowingStateLoss();
    }

    //初始化页面数据
    private void intiData() {
        initListView();
        //准备Fragment
        homeFragment = new HomeFragment();
        weatherFragment = new WeatherFragment();
        subscribeFragment = new SubscribeFragment();

        //加载主页面到容器里
        switchFragment(homeFragment);

        //一开始没有点击事件，设置默认标题
        mtoolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean isFirstRun;
            @Override
            public void onGlobalLayout() {
                if (!isFirstRun) {
                    mtoolbar.setTitle("主页");
                    isFirstRun = true;
                }
            }
        });
    }



    //下拉刷新
    private void refreshName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    textView.setText(numbers[++currenttext % numbers.length]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }



    //QQ登录
    private IUiListener listener;

    //初始化视图，绑定控件
    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        mListView = findViewById(R.id.menu_list_view);
        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, Main2ActivityRepair.this.getApplicationContext());

        name = findViewById(R.id.info_name);
        img = findViewById(R.id.info_icon);

//        SharedPreferences qq = getSharedPreferences("qq", 0);
//        String name = qq.getString("name", "");
//        String img = qq.getString("img", "");
//        if (name.equals("")&&img.equals("")){
//            this.name.setText("未登录");
//            this.img.setImageResource(R.mipmap.logo);
//        }else {
//            this.name.setText(name);
//            Glide.with(getApplicationContext()).load(img).into(this.img);
//        }
    }

    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(Main2ActivityRepair.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        // 保持身份状态信息
                        SharedPreferencesUtils.put(getApplicationContext(),"loginState", "401");  // 已授权 已登录
                        SharedPreferencesUtils.put(getApplicationContext(),"loginUser", response.toString());
                        Log.e(TAG, "登录成功" + response.toString());
                        if (response == null) {
                            return;
                        }
                        try {
                            JSONObject jo = (JSONObject) response;
                            Toast.makeText(Main2ActivityRepair.this, "登录成功", Toast.LENGTH_SHORT).show();
                            String nickName = jo.getString("nickname");
                            String figureurl_qq_1 = jo.getString("figureurl_qq");
                            SharedPreferences qq = getApplicationContext().getSharedPreferences("qq", 0);
                            SharedPreferences.Editor edit = qq.edit();
                            edit.putString("name",nickName);
                            edit.putString("img",figureurl_qq_1);
                            edit.commit();
                            name.setText(nickName);
                            Glide.with(getApplicationContext()).load(figureurl_qq_1).into(img);
                            //Uri parse_qq = Uri.parse(figureurl_qq_1);
                            //img.setImageURI(parse_qq);
//                            URL url = new URL(figureurl_qq_1);
//                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                            conn.setConnectTimeout(5000);//设置超时
//                            conn.setDoInput(true);
//                            conn.setUseCaches(false);//不缓存
//                            conn.connect();
//                            InputStream inputStream = conn.getInputStream();
//                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                            img.setImageBitmap(bitmap);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(Main2ActivityRepair.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(Main2ActivityRepair.this, "授权取消", Toast.LENGTH_SHORT).show();

        }
    }

    //腾讯QQ回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, listener);
            }
        }
    }



}