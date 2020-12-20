package com.yiyuanzhu.thinking.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.yiyuanzhu.thinking.R;
import com.yiyuanzhu.thinking.viewmodel.MyClassViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private StringBuffer week = new StringBuffer("10");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getContext();
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.setContext(context);
        homeViewModel.setAccount("1805050213");
        homeViewModel.setTableName("2020-2021-1");
        homeViewModel.setWeek(Integer.parseInt(week.toString()));
        homeViewModel.loadData();
        System.out.println("oncreate");
    }

    //    每次切换页面时执行
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel.loadData();
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TableLayout table = root.findViewById(R.id.table);
        System.out.println("oncreateview");

        /*TextView textView = root.findViewById(R.id.textView);
        final CalendarView calendar = root.findViewById(R.id.calendarView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendar.getVisibility() == View.INVISIBLE) {
                    calendar.setVisibility(View.VISIBLE);
                } else {
                    calendar.setVisibility(View.INVISIBLE);
                }
            }
        });*/
        /*final Button last_week = root.findViewById(R.id.last_week);
        last_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previous = Integer.parseInt(week.toString()) - 1;
                if (previous > 0) {
                    week.replace(0, 1, previous + "");
                    System.out.println("week" + week);
                    last_week.setText(week);
                }
            }
        });*/

        homeViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<MyClassViewModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<MyClassViewModel> viewModels) {
//                table.removeAllViews();
                System.out.println("getLivedata");
                ArrayList<TableRow> rows = new ArrayList<>();
                for (int i = 1; i < table.getChildCount(); i ++) {
                    TableRow row = (TableRow) table.getChildAt(i);
                    rows.add(row);
                }

                for (MyClassViewModel viewModel : viewModels) {
                    TableRow row = rows.get(viewModel.getOrder_end() / 2 - 1);
                    Button button = (Button) row.getChildAt(viewModel.getDay());
                    button.setText(viewModel.getCourse_name() + "\n\n" + viewModel.getClassroom() + "\n\n" + viewModel.getTeacher());
                    button.setTextSize(10.0f);
                    button.setVisibility(View.VISIBLE);
                }
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Button last_week = (Button)getActivity().findViewById(R.id.last_week);
        View home = getActivity().findViewById(R.id.scrollView);
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
                        week.replace(0, week.length(), previous + "");
                        System.out.println("week" + week);
                        homeViewModel.setWeek(Integer.parseInt(week.toString()));
                        homeViewModel.loadData();
                        ArrayList<MyClassViewModel> viewModels = homeViewModel.getLiveData().getValue();

                        TableLayout table = getActivity().findViewById(R.id.table);
                        ArrayList<TableRow> rows = new ArrayList<>();
                        for (int i = 1; i < table.getChildCount(); i ++) {
                            TableRow row = (TableRow) table.getChildAt(i);
                            for (int j = 1; j < row.getChildCount(); j ++) {
                                row.getChildAt(j).setVisibility(View.INVISIBLE);
                            }
                            rows.add(row);
                        }

                        for (MyClassViewModel viewModel : viewModels) {
                            TableRow row = rows.get(viewModel.getOrder_end() / 2 - 1);
                            Button button = (Button) row.getChildAt(viewModel.getDay());
                            button.setText(viewModel.getCourse_name() + "\n\n" + viewModel.getClassroom() + "\n\n" + viewModel.getTeacher());
                            button.setTextSize(10.0f);
                            button.setVisibility(View.VISIBLE);
                        }
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

        last_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previous = Integer.parseInt(week.toString()) - 1;
                if (previous > 0) {
                    week.replace(0, week.length(), previous + "");
                    System.out.println("week" + week);
                    homeViewModel.setWeek(Integer.parseInt(week.toString()));
                    homeViewModel.loadData();
                    ArrayList<MyClassViewModel> viewModels = homeViewModel.getLiveData().getValue();

                    TableLayout table = getActivity().findViewById(R.id.table);
                    ArrayList<TableRow> rows = new ArrayList<>();
                    for (int i = 1; i < table.getChildCount(); i ++) {
                        TableRow row = (TableRow) table.getChildAt(i);
                        for (int j = 1; j < row.getChildCount(); j ++) {
                            row.getChildAt(j).setVisibility(View.INVISIBLE);
                        }
                        rows.add(row);
                    }

                    for (MyClassViewModel viewModel : viewModels) {
                        TableRow row = rows.get(viewModel.getOrder_end() / 2 - 1);
                        Button button = (Button) row.getChildAt(viewModel.getDay());
                        button.setText(viewModel.getCourse_name() + "\n\n" + viewModel.getClassroom() + "\n\n" + viewModel.getTeacher());
                        button.setTextSize(10.0f);
                        button.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

}