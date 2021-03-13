package com.example.myapplication.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class ImageThreeFragment extends Fragment {

    ImageView imageView;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //通过参数中的布局填充获取对应布局
       /**
        * 1、设置布局文件
        *       view就是你的布局
        * 2、findviewbyId（）   你要找控件，必须通过  “布局文件” 找
        */
        view = inflater.inflate(R.layout.fragment_image,container,false);
       imageView = view.findViewById(R.id.img_fragment);
          imageView.setImageResource(R.drawable.img_three);
             return view;
        }
}
