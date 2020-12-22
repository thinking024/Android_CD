package com.yiyuanzhu.thinking.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yiyuanzhu.thinking.R;
import com.yiyuanzhu.thinking.dao.crud.CourseCRUD;
import com.yiyuanzhu.thinking.dao.crud.CourseTableCRUD;
import com.yiyuanzhu.thinking.dao.DbHelper;
import com.yiyuanzhu.thinking.dao.crud.GradeCRUD;
import com.yiyuanzhu.thinking.dao.crud.MyClassCRUD;
import com.yiyuanzhu.thinking.dao.crud.UserCRUD;
import com.yiyuanzhu.thinking.pojo.Course;
import com.yiyuanzhu.thinking.pojo.CourseTable;
import com.yiyuanzhu.thinking.pojo.Grade;
import com.yiyuanzhu.thinking.pojo.MyClass;
import com.yiyuanzhu.thinking.pojo.User;
import com.yiyuanzhu.thinking.utils.crawler.HNUSTCrawler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DbHelper dbHelper = new DbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        final String userAccount = "1805050213";
        final String password = "hn095573";
        /*new Thread() {
            @Override
            public void run() {
                try {
                    ArrayList<CourseTable> courseTables = HNUSTCrawler.getCourse(userAccount, password);
                    for (CourseTable courseTable : courseTables) {
                        for (Course course : courseTable.getCourses()) {
                            System.out.println(course);
                        }
                        System.out.println("===========");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();*/

    }

}