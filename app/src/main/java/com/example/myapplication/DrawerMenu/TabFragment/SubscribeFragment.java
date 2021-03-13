package com.example.myapplication.DrawerMenu.TabFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

/**
 * 订阅Fragment示范
 */
public class SubscribeFragment extends Fragment {
    private View mView;
    private Button btnSubscribe;
    private Button btnCancelSubscribe;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_subscribe, container, false);
        initView();
        initEvent();
        return mView;
    }

    private void initView(){
        btnSubscribe = mView.findViewById(R.id.btn_subscribe);
        btnCancelSubscribe = mView.findViewById(R.id.btn_cancel_subscribe);
    }
    private void initEvent(){
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了订阅！", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了取消订阅！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
