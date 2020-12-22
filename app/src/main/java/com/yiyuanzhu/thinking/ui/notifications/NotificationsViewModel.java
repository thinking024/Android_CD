package com.yiyuanzhu.thinking.ui.notifications;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yiyuanzhu.thinking.dao.crud.CourseTableCRUD;
import com.yiyuanzhu.thinking.pojo.CourseTable;
import com.yiyuanzhu.thinking.pojo.User;
import com.yiyuanzhu.thinking.utils.GlobalInfo;

import java.util.ArrayList;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<User> user;
    private MutableLiveData<ArrayList<String>> tables;
    private Context context;

    public NotificationsViewModel() {
        user = new MutableLiveData<>();
        tables = new MutableLiveData<>();
    }
    public void loadData() {
        user.setValue(GlobalInfo.getInstance().getUser());
        ArrayList<CourseTable> courseTables =
                CourseTableCRUD.queryCourseTable(new String[]{"table_name"}, "account=?", new String[]{GlobalInfo.getInstance().getUser().getAccount()}, context);
        ArrayList<String> tableNames = new ArrayList<>();
        for (CourseTable courseTable : courseTables) {
            tableNames.add(courseTable.getTableName());
        }
        tables.setValue(tableNames);
    }
    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(MutableLiveData<User> user) {
        this.user = user;
    }

    public MutableLiveData<ArrayList<String>> getTables() {
        return tables;
    }

    public void setTables(MutableLiveData<ArrayList<String>> tables) {
        this.tables = tables;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}