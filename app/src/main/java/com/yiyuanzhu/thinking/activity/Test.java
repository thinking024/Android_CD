package com.yiyuanzhu.thinking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yiyuanzhu.thinking.R;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        /*List<String> list = new ArrayList<String>();
        list.add("苹果");
        list.add("香蕉");
        list.add("橘子");
        list.add("香蕉");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Spinner sp = (Spinner) findViewById(R.id.spinner);
        sp.setAdapter(adapter);*/

    }
}