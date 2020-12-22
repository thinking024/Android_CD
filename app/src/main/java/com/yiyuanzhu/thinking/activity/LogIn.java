package com.yiyuanzhu.thinking.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yiyuanzhu.thinking.R;
import com.yiyuanzhu.thinking.dao.crud.UserCRUD;
import com.yiyuanzhu.thinking.pojo.CourseTable;
import com.yiyuanzhu.thinking.pojo.User;
import com.yiyuanzhu.thinking.utils.GlobalInfo;
import com.yiyuanzhu.thinking.utils.crawler.HNUSTCrawler;
import com.yiyuanzhu.thinking.utils.crawler.SaveData;

import java.util.ArrayList;

public class LogIn extends AppCompatActivity {
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        progress = findViewById(R.id.progressBar);
        final SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        final String account = sp.getString("account", null);
//        已有账号处于登录状态，则跳过登录，直接进
        if (account != null) {
            ArrayList<User> users = UserCRUD.queryUser(null, "account=?", new String[]{account}, this);
            GlobalInfo.getInstance().setUser(users.get(0));
            System.out.println(GlobalInfo.getInstance().getUser());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 0) {
                    Toast.makeText(LogIn.this,"账号或密码错误",Toast.LENGTH_LONG).show();
                }
                if (msg.what == 1) {
                    progress.setVisibility(View.GONE);
                }
            }
        };

//        没有处于登录状态的账号
        Button button = findViewById(R.id.login);
        final EditText text1 = findViewById(R.id.username);
        final EditText text2 = findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = text1.getText().toString();
                final String password = text2.getText().toString();
                progress.setVisibility(View.VISIBLE);
                System.out.println(1 + username + " " + password);
                ArrayList<User> users = UserCRUD.queryUser(null, "account=?", new String[]{username}, LogIn.this);
                if (users == null) {  // 第一次登录
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Message message = new Message();
                                ArrayList<CourseTable> courseTables = HNUSTCrawler.getCourse(username, password);
                                if (courseTables == null) {
//                                    Toast.makeText(LogIn.this,"账号或密码错误",Toast.LENGTH_LONG).show();
                                    message.what = 0;
                                } else {
//                                  成功登录
                                    User user = new User(username,password,courseTables.get(0).getTableName(),10,1);
                                    System.out.println(user);
                                    UserCRUD.insertUser(user, LogIn.this);
                                    SaveData.saveCourse(user, LogIn.this);
                                    SaveData.saveGrade(user, LogIn.this);

                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("account", username);
                                    editor.commit();

                                    GlobalInfo.getInstance().setUser(user);

                                    message.what = 1;
                                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else if (! users.get(0).getPassword().equals(password)){
                    Toast.makeText(LogIn.this,"账号或密码错误",Toast.LENGTH_LONG).show();
                } else {
//                    成功登录
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("account", username);
                    editor.commit();

                    GlobalInfo.getInstance().setUser(users.get(0));
                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}