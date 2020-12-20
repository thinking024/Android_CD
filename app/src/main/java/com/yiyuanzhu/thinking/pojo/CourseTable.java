package com.yiyuanzhu.thinking.pojo;

import java.util.List;

public class CourseTable {
    private String id;
    private String account;
    private String tableName;
    private List<Course> courses;

    public CourseTable() {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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


    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "CourseTable{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", tableName='" + tableName + '\'' +
                ", courses=" + courses +
                '}';
    }
}
