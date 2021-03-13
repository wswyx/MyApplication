package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.SharedPreferencesUtils;


public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 登录状态
        String state = (String) SharedPreferencesUtils.get(this,"loginState", "");
        if(state == null){
            SharedPreferencesUtils.put(this,"loginState", "403"); // 未授权，未登录
        }


        button=findViewById(R.id.begin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Main2ActivityRepair.class);
                startActivity(i);
            }
        });
    }


}

