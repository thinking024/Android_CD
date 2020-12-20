package com.yiyuanzhu.thinking.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.yiyuanzhu.thinking.R;
import com.yiyuanzhu.thinking.adapter.MyAdapter;
import com.yiyuanzhu.thinking.pojo.Grade;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        context = getContext();
        dashboardViewModel.setContext(context); // 必须在获取data前就设定
        String account = "1805050213";
        dashboardViewModel.setLiveData(account);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final ExpandableListView listView = root.findViewById(R.id.listview);
        dashboardViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Grade>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Grade> grades) {
                ArrayList<String> terms = new ArrayList<>();
                ArrayList<ArrayList<Grade>> children = new ArrayList<>();
                for (Grade grade : grades) {
                    String term = grade.getTerm();
//                    没有此学期，则添加
                    if (terms.indexOf(term) == -1) {
                        terms.add(term);
                        children.add(new ArrayList<Grade>());
                    }
                    int index = terms.indexOf(term);
                    children.get(index).add(grade);
                }
                listView.setAdapter(new MyAdapter(context, terms, children));
                listView.expandGroup(0);
            }
        });
        return root;
    }
}