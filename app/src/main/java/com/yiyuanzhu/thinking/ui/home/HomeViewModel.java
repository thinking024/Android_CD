package com.yiyuanzhu.thinking.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yiyuanzhu.thinking.dao.DbHelper;
import com.yiyuanzhu.thinking.viewmodel.MyClassViewModel;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MyClassViewModel>> liveData;
    private Context context;
    private String account;
    private String tableName;
    private int week;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public HomeViewModel() {
        liveData = new MutableLiveData<>();
    }

    public void loadData() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT\n" +
                "course_table.table_name,\n" +
                "course.id AS course_id,\n" +
                "course.teacher,\n" +
                "course.course_name,\n" +
                "course.course_info,\n" +
                "myclass.id AS myclass_id,\n" +
                "myclass.day,\n" +
                "myclass.week_begin,\n" +
                "myclass.week_end,\n" +
                "myclass.order_begin,\n" +
                "myclass.order_end,\n" +
                "myclass.classroom \n" +
                "FROM user, course_table, course, myclass \n" +
                "WHERE\n" +
                "user.account = course_table.account \n" +
                "AND course_table.id = course.course_table_id \n" +
                "AND course.id = myclass.course_id \n" +
                "AND user.account = ? AND course_table.table_name = ? AND myclass.week_begin <= ? AND myclass.week_end >= ?";
        Cursor cursor = db.rawQuery(query, new String[] {account, tableName, week + "", week + ""});
        ArrayList<MyClassViewModel> viewModels = new ArrayList<>();
        while (cursor.moveToNext()) {
            String table_name = cursor.getString(0);
            String course_id = cursor.getString(1);
            String teacher = cursor.getString(2);
            String course_name = cursor.getString(3);
            String course_info = cursor.getString(4);
            String myclass_id = cursor.getString(5);
            int day = cursor.getInt(6);
            int week_begin = cursor.getInt(7);
            int week_end = cursor.getInt(8);
            int order_begin = cursor.getInt(9);
            int order_end = cursor.getInt(10);
            String classroom = cursor.getString(11);
            MyClassViewModel viewModel =
                    new MyClassViewModel
                            (table_name, course_id, teacher, course_name, course_info,myclass_id, day, week_begin, week_end, order_begin, order_end, classroom);
            viewModels.add(viewModel);
        }
        System.out.println(viewModels.size() + "=========");
        liveData.setValue(viewModels);
        for (MyClassViewModel viewModel : viewModels) {
            System.out.println(viewModel);
        }
    }

    public LiveData<ArrayList<MyClassViewModel>> getLiveData() {
        return liveData;
    }
}