package com.yiyuanzhu.thinking.utils;

import com.yiyuanzhu.thinking.pojo.User;

public class GlobalInfo {
    private User user;
    private static GlobalInfo globalInfo = new GlobalInfo();
    private GlobalInfo() {

    }
    public static GlobalInfo getInstance() {
        return globalInfo;
    }

    public void setUser(User user) {
        globalInfo.user = user;
    }

    public User getUser() {
        return user;
    }
}
