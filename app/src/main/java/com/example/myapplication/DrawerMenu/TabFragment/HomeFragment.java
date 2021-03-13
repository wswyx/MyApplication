package com.example.myapplication.DrawerMenu.TabFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.ViewPagerActivity;

/**
 * 好友
 */
public class HomeFragment extends Fragment {
    private View mView;
    private Button btn_Viewpager;

    private SwipeRefreshLayout swipeRefresh;
    private TextView textView = null;
    private int currenttext = 0;
    String[] numbers = new String[]{"1号", "2号", "3号", "4号", "5号", "6号"};


    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置好友Fragment使用的布局
        mView = inflater.inflate(R.layout.fragment_friend, container, false);
        initView();
        initEvent();
        return mView;
    }

    private void initView(){
        //刷新refresh
        textView = mView.findViewById(R.id.text_name);
        textView.setText(numbers[0]);
        swipeRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        //进入viewpager的按钮
        btn_Viewpager = mView.findViewById(R.id.btn_viewpager);
    }

    private void initEvent(){
        btn_Viewpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
                startActivity(intent);
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                refreshName();
            }
        });
    }

    //刷新当天值日名字
    private void refreshName () {
        new Thread(new Runnable() {
            @Override
            public void run () {
                try {
                    Thread.sleep(500);
                    textView.setText(numbers[++currenttext % numbers.length]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //等同于runOnUiThread，但runOnUiThread只能在Activity使用，这里是Fragment
                handler.post(new Runnable() {
                    @Override
                    public void run () {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
