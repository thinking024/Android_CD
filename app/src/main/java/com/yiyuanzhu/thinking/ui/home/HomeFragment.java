package com.yiyuanzhu.thinking.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.yiyuanzhu.thinking.R;
import com.yiyuanzhu.thinking.utils.GlobalInfo;
import com.yiyuanzhu.thinking.viewmodel.MyClassViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private StringBuffer week = new StringBuffer("10");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    Calendar cld = Calendar.getInstance(Locale.CHINA);
    String[] day_of_week = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    String[] date = new String[7];
    String today = df.format(cld.getTime());;
    String month;

//    在这里完成数据初始化
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getContext();
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.setContext(context);
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String account = sp.getString("account", "1805050213");

        homeViewModel.setAccount(GlobalInfo.getInstance().getUser().getAccount());
        homeViewModel.setTableName(GlobalInfo.getInstance().getUser().getDefault_table());
        System.out.println("tableName=======" + GlobalInfo.getInstance().getUser().getDefault_table());
        homeViewModel.setWeek(GlobalInfo.getInstance().getUser().getCurrent_week());
        System.out.println("week=======" + GlobalInfo.getInstance().getUser().getCurrent_week());
        homeViewModel.loadData();

        cld.setFirstDayOfWeek(Calendar.MONDAY);//以周一为首日
        for (int i = 0; i < 6; i++) {
            cld.set(Calendar.DAY_OF_WEEK, i + 2);
            if (i == 0) {
                month = cld.get(Calendar.MONTH) + 1 + "";
            }
            date[i] = cld.get(Calendar.DAY_OF_MONTH) + "\n" + day_of_week[cld.get(Calendar.DAY_OF_WEEK) - 1];
        }
        cld.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        date[6] = cld.get(Calendar.DAY_OF_MONTH) + "\n" + day_of_week[cld.get(Calendar.DAY_OF_WEEK) - 1];

        System.out.println("oncreate");
    }

    //    每次切换页面时执行
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TableLayout table = root.findViewById(R.id.table);
        final LinearLayout week_layout = root.findViewById(R.id.week);

        TextView week_text = root.findViewById(R.id.weekview);
        week_text.setText(today + "  " + "第" + week.toString() + "周");
        System.out.println("oncreateview");

//        livedata切换时自动调用
        homeViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<MyClassViewModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<MyClassViewModel> viewModels) {
                System.out.println("getLivedata");
                TextView tablename_text = root.findViewById(R.id.tablename);
                tablename_text.setText(viewModels.get(0).getTable_name());

                TextView month_text = (TextView)week_layout.getChildAt(0);
                month_text.setText(month + "\n" + "月");
                for (int i = 1; i < week_layout.getChildCount(); i ++) {
                    TextView week_text = (TextView)week_layout.getChildAt(i);
                    week_text.setText(date[i - 1]);
                }

                ArrayList<TableRow> rows = new ArrayList<>();
                for (int i = 0; i < table.getChildCount(); i ++) {
                    TableRow row = (TableRow) table.getChildAt(i);
                    rows.add(row);
                }

                for (final MyClassViewModel viewModel : viewModels) {
                    TableRow row = rows.get(viewModel.getOrder_end() / 2 - 1);
                    Button button = (Button) row.getChildAt(viewModel.getDay());
                    button.setText(viewModel.getCourse_name() + "\n\n" + viewModel.getClassroom());
                    button.setTextSize(10.0f);button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String week = "第" + viewModel.getWeek_begin() + "-" + viewModel.getWeek_end() + "周" + "  周" + day_of_week[viewModel.getDay() % 7];
                            String order = "第" + viewModel.getOrder_begin() + "-" + viewModel.getOrder_end() + "节";
                            String teacher = viewModel.getTeacher();
                            String classroom = viewModel.getClassroom();
                            String classInfo = viewModel.getCourse_info();
                            AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle(viewModel.getCourse_name())
                                    .setMessage(week + "\n" + order + "\n" + teacher + "\n" + classroom + "\n" + classInfo)
                                    .setPositiveButton("确定",null)
                                    .create();
                            dialog.show();
                        }
                    });
                    button.setVisibility(View.VISIBLE);
                }
                for (MyClassViewModel viewModel : viewModels) {
                    System.out.println(viewModel);
                }
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View home = getActivity().findViewById(R.id.scrollView);
        final LinearLayout week_layout = getActivity().findViewById(R.id.week);
        final TableLayout table = getActivity().findViewById(R.id.table);
        final TextView week_text = getActivity().findViewById(R.id.weekview);
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float x = e1.getX()-e2.getX();
                float x2 = e2.getX()-e1.getX();
                if(x > 50 && Math.abs(velocityX) > 0){
                    Log.i("TAG","向左手势");

                }else if(x2 > 50 && Math.abs(velocityX) > 0){
                    Log.i("TAG","向右手势");
                    int previous = Integer.parseInt(week.toString()) - 1;
                    if (previous > 0) {

//                      清空界面
                        ArrayList<TableRow> rows = new ArrayList<>();
                        for (int i = 0; i < table.getChildCount(); i ++) {
                            TableRow row = (TableRow) table.getChildAt(i);
                            for (int j = 1; j < row.getChildCount(); j ++) {
                                row.getChildAt(j).setVisibility(View.INVISIBLE);
                            }
                            rows.add(row);
                        }

//                      周次-1
                        week.replace(0, week.length(), previous + "");
                        System.out.println("week" + week);
                        homeViewModel.setWeek(Integer.parseInt(week.toString()));
                        String string = "第" + week.toString() + "周";
                        if (week.toString().equals(GlobalInfo.getInstance().getUser().getCurrent_week())) {
                            string = string;
                        } else {
                            string = string + "  " + "非本周";
                        }
                        week_text.setText(string);
                        homeViewModel.loadData();

                        cld.add(Calendar.DATE, -7);
                        for (int i = 0; i < 6; i++) {
                            cld.set(Calendar.DAY_OF_WEEK, i + 2);
                            if (i == 0) {
                                month = cld.get(Calendar.MONTH) + 1 + "";
                            }
                            date[i] = cld.get(Calendar.DAY_OF_MONTH) + "\n" + day_of_week[cld.get(Calendar.DAY_OF_WEEK) - 1];
                        }
                        cld.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        date[6] = cld.get(Calendar.DAY_OF_MONTH) + "\n" + day_of_week[cld.get(Calendar.DAY_OF_WEEK) - 1];
                        System.out.println(Arrays.toString(date));

                        TextView month_text = (TextView)week_layout.getChildAt(0);
                        month_text.setText(month + "\n" + "月");
                        for (int i = 1; i < week_layout.getChildCount(); i ++) {
                            TextView week_text = (TextView)week_layout.getChildAt(i);
                            week_text.setText(date[i - 1]);
                        }

                        /*ArrayList<MyClassViewModel> viewModels = homeViewModel.getLiveData().getValue();
                        for (MyClassViewModel viewModel : viewModels) {
                            TableRow row = rows.get(viewModel.getOrder_end() / 2 - 1);
                            Button button = (Button) row.getChildAt(viewModel.getDay());
                            button.setText(viewModel.getCourse_name() + "\n\n" + viewModel.getClassroom() + "\n\n" + viewModel.getTeacher());
                            button.setTextSize(10.0f);
                            button.setVisibility(View.VISIBLE);
                        }*/
                    }
                }

                return false;
            }
        });

        home.setLongClickable(true);
        home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        home.setLongClickable(true);//必需设置这为true 否则也监听不到手势
    }

}