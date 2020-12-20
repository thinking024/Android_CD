package com.yiyuanzhu.thinking.ui.dashboard;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yiyuanzhu.thinking.dao.crud.GradeCRUD;
import com.yiyuanzhu.thinking.pojo.Grade;
import com.yiyuanzhu.thinking.viewmodel.GradeViewModel;

import java.util.ArrayList;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Grade>> liveData;
    private Context context;

    public DashboardViewModel() {
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Grade>> getLiveData() {
        return liveData;
    }

    public void setLiveData(String account) {
        ArrayList<Grade> grades = GradeCRUD.queryGrade(null, "account=?", new String[]{account}, context);
        liveData.setValue(grades);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}