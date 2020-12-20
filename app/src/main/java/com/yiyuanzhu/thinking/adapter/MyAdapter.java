package com.yiyuanzhu.thinking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.yiyuanzhu.thinking.R;
import com.yiyuanzhu.thinking.pojo.Grade;

import java.util.ArrayList;

public class MyAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> group;
    private ArrayList<ArrayList<Grade>> child;

    public MyAdapter(Context context, ArrayList<String> group, ArrayList<ArrayList<Grade>> child) {
        this.context = context;
        this.group = group;
        this.child = child;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 10 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_group_view,parent,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.text = (TextView)convertView.findViewById(R.id.grade_term);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
        groupViewHolder.text.setText(group.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grades_child_view,parent,false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.grade_name = (TextView)convertView.findViewById(R.id.grade_name);
            childViewHolder.grade_point = (TextView)convertView.findViewById(R.id.grade_point);
            childViewHolder.grade_score = (TextView)convertView.findViewById(R.id.grade_score);
            childViewHolder.grade_credit = (TextView)convertView.findViewById(R.id.grade_credit);
            childViewHolder.grade_course_type = (TextView)convertView.findViewById(R.id.grade_course_type);
            childViewHolder.grade_exam_type = (TextView)convertView.findViewById(R.id.grade_exam_type);
            childViewHolder.grade_term_again = (TextView)convertView.findViewById(R.id.grade_term_again);
            convertView.setTag(childViewHolder);

        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.grade_name.setText(child.get(groupPosition).get(childPosition).getName());
        childViewHolder.grade_point.setText("绩点：" + child.get(groupPosition).get(childPosition).getGradePoint());
        childViewHolder.grade_score.setText("分数：" + child.get(groupPosition).get(childPosition).getScore());
        childViewHolder.grade_credit.setText("学分：" + child.get(groupPosition).get(childPosition).getCredit());
        childViewHolder.grade_course_type.setText(child.get(groupPosition).get(childPosition).getCourse_type());
        childViewHolder.grade_exam_type.setText(child.get(groupPosition).get(childPosition).getExam_type());
        childViewHolder.grade_term_again.setText(child.get(groupPosition).get(childPosition).getTerm_again() + "");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        TextView text;
    }
    class ChildViewHolder {
        TextView grade_name;
        TextView grade_score;
        TextView grade_credit;
        TextView grade_point;
        TextView grade_course_type;
        TextView grade_exam_type;
        TextView grade_term_again;
    }
}
