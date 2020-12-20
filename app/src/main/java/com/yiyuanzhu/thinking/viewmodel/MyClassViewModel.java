package com.yiyuanzhu.thinking.viewmodel;

public class MyClassViewModel {
    private String table_name;
    private String course_id;
    private String teacher;
    private String course_name;
    private String course_info;
    private String myclass_id;
    private int day;
    private int week_begin;
    private int week_end;
    private int order_begin;
    private int order_end;
    private String classroom;

    public MyClassViewModel() {
    }

    public MyClassViewModel(String table_name, String course_id, String teacher, String course_name, String course_info, String myclass_id, int day, int week_begin, int week_end, int order_begin, int order_end, String classroom) {
        this.table_name = table_name;
        this.course_id = course_id;
        this.teacher = teacher;
        this.course_name = course_name;
        this.course_info = course_info;
        this.myclass_id = myclass_id;
        this.day = day;
        this.week_begin = week_begin;
        this.week_end = week_end;
        this.order_begin = order_begin;
        this.order_end = order_end;
        this.classroom = classroom;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_info() {
        return course_info;
    }

    public void setCourse_info(String course_info) {
        this.course_info = course_info;
    }

    public String getMyclass_id() {
        return myclass_id;
    }

    public void setMyclass_id(String myclass_id) {
        this.myclass_id = myclass_id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek_begin() {
        return week_begin;
    }

    public void setWeek_begin(int week_begin) {
        this.week_begin = week_begin;
    }

    public int getWeek_end() {
        return week_end;
    }

    public void setWeek_end(int week_end) {
        this.week_end = week_end;
    }

    public int getOrder_begin() {
        return order_begin;
    }

    public void setOrder_begin(int order_begin) {
        this.order_begin = order_begin;
    }

    public int getOrder_end() {
        return order_end;
    }

    public void setOrder_end(int order_end) {
        this.order_end = order_end;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return "MyClassViewModel{" +
                "table_name='" + table_name + '\'' +
                ", course_id='" + course_id + '\'' +
                ", teacher='" + teacher + '\'' +
                ", course_name='" + course_name + '\'' +
                ", course_info='" + course_info + '\'' +
                ", myclass_id='" + myclass_id + '\'' +
                ", day='" + day + '\'' +
                ", week_begin='" + week_begin + '\'' +
                ", week_end='" + week_end + '\'' +
                ", order_begin='" + order_begin + '\'' +
                ", order_end='" + order_end + '\'' +
                ", classroom='" + classroom + '\'' +
                '}';
    }
}
