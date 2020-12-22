package com.yiyuanzhu.thinking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yiyuanzhu.thinking.R;
import com.yiyuanzhu.thinking.dao.DbHelper;
import com.yiyuanzhu.thinking.dao.crud.UserCRUD;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        System.out.println(1);
        System.out.println(UserCRUD.queryUser(null, null, null, this).get(0));

        /*ContentValues contentValues = new ContentValues();
        contentValues.put("default_table", "de");
        contentValues.put("show_weekend", 1);
        UserCRUD.updateUser(contentValues, "1805050213", this);
        System.out.println(2);
        System.out.println(UserCRUD.queryUser(null, null, null, this).get(0));

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update user set current_week = ?", new Object[]{8});

        System.out.println(3);
        System.out.println(UserCRUD.queryUser(null, null, null, this).get(0));*/
        List<String> list = new ArrayList<String>();
        list.add("苹果");
        list.add("香蕉");
        list.add("橘子");
        list.add("香蕉");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Spinner sp = (Spinner) findViewById(R.id.spinner);
        sp.setAdapter(adapter);

    }
}