package com.yiyuanzhu.thinking.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.yiyuanzhu.thinking.R;
import com.yiyuanzhu.thinking.activity.LogIn;
import com.yiyuanzhu.thinking.dao.crud.CourseTableCRUD;
import com.yiyuanzhu.thinking.dao.crud.GradeCRUD;
import com.yiyuanzhu.thinking.dao.crud.UserCRUD;
import com.yiyuanzhu.thinking.pojo.CourseTable;
import com.yiyuanzhu.thinking.pojo.User;
import com.yiyuanzhu.thinking.utils.GlobalInfo;
import com.yiyuanzhu.thinking.utils.crawler.SaveData;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private Context context;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        notificationsViewModel.setContext(context);
        notificationsViewModel.loadData();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView account_text = root.findViewById(R.id.textView2);
        final TextView password_text = root.findViewById(R.id.textView7);
        final Spinner spinner = root.findViewById(R.id.spinner2);
        notificationsViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                account_text.setText(user.getAccount());
                password_text.setText(user.getPassword());
            }
        });
        notificationsViewModel.getTables().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> tableNames) {
                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, tableNames);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setAdapter(adapter);
                String default_table = GlobalInfo.getInstance().getUser().getDefault_table();
                int index = tableNames.indexOf(default_table);
                spinner.setSelection(index,true);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FragmentActivity activity = getActivity();

        Spinner spinner = activity.findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GlobalInfo.getInstance().getUser().setDefault_table(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


//        退出登录
        final Button logout_btn = activity.findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = activity.getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("account", null);
                editor.commit();

                Intent intent = new Intent(activity, LogIn.class);
                startActivity(intent);
                activity.finish();
            }
        });

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Toast toast = makeText(context, null, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                if (msg.what == 0) {
                    toast.setText("成功导入课程");
                    toast.show();
                }
                if (msg.what == 1) {
                    toast.setText("成功导入成绩");
                    toast.show();
                }
            }
        };

//        重新导入课程
        final Button reCourseTable = activity.findViewById(R.id.button);
        reCourseTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = GlobalInfo.getInstance().getUser();
                CourseTableCRUD.deleteCourseTable("account=?", new String[]{user.getAccount()}, context);
                new Thread() {
                    @Override
                    public void run() {
                        SaveData.saveCourse(user,context);
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                }.start();

            }
        });

//        重新导入成绩
        final Button reGrade = activity.findViewById(R.id.button2);
        reGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = GlobalInfo.getInstance().getUser();
                GradeCRUD.deleteGrade(user.getAccount(), context);
                new Thread() {
                    @Override
                    public void run() {
                        SaveData.saveGrade(user,context);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }.start();
            }
        });

//        重新导入全部数据
        Button reAll = activity.findViewById(R.id.button3);
        reAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reCourseTable.performClick();
                reGrade.callOnClick();
            }
        });
    }
}