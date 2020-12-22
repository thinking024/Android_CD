package com.yiyuanzhu.thinking.pojo;

public class User {
    private String account;
    private String password;
    private String default_table;
    private int current_week;
    private int show_weekend;
    public User() {
    }

    public User(String account, String password, String default_table, int current_week, int show_weekend) {
        this.account = account;
        this.password = password;
        this.default_table = default_table;
        this.current_week = current_week;
        this.show_weekend = show_weekend;
    }

    public String getDefault_table() {
        return default_table;
    }

    public void setDefault_table(String default_table) {
        this.default_table = default_table;
    }

    public int getCurrent_week() {
        return current_week;
    }

    public void setCurrent_week(int current_week) {
        this.current_week = current_week;
    }

    public int getShow_weekend() {
        return show_weekend;
    }

    public void setShow_weekend(int show_weekend) {
        this.show_weekend = show_weekend;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", default_table='" + default_table + '\'' +
                ", current_week=" + current_week +
                ", show_weekend=" + show_weekend +
                '}';
    }
}
