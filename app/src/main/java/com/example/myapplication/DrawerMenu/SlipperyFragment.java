package com.example.myapplication.DrawerMenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Main2Activity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class SlipperyFragment extends Fragment {


    private ListView mListView;
    private List<MenuItem> menuItemList = new ArrayList<>();
    private MenuItemAdpter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View navView = inflater.inflate(R.layout.activity_menu, container, false);


        mListView = (ListView) navView.findViewById(R.id.menu_list_view);
        initListView();
        clickEvents();


        return navView;
    }


    public void initListView() {
        String[] data_zh = getResources().getStringArray(R.array.menu_zh);
        String[] data_en = getResources().getStringArray(R.array.menu_en);
        for (int i = 0; i < data_zh.length; i++) {
            MenuItem menuItem = new MenuItem(data_zh[i], data_en[i]);
            menuItemList.add(menuItem);
        }
        adapter = new MenuItemAdpter(getActivity(), R.layout.menu_list_item, menuItemList);
        mListView.setAdapter(adapter);
    }



    public void clickEvents() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.changeSelected(position);
                Main2Activity activity = (Main2Activity) getActivity();
                DrawerLayout mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.main2_layout);
                mDrawerLayout.closeDrawer(Gravity.START);
                activity.switchFragment(position);
            }
        });
    }
}