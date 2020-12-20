package com.yiyuanzhu.thinking.dao.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yiyuanzhu.thinking.dao.DbHelper;
import com.yiyuanzhu.thinking.pojo.User;

import java.util.ArrayList;

public class UserCRUD {
    public static String insertUser(User user, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account", user.getAccount());
        values.put("password", user.getPassword());
        long result = db.insert("user", null, values);
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "insertUser");
        return result + "";
    }

    public static String updateUser(ContentValues values, String account, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.update("user", values, "account=?", new String[] {account});
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "updateUser");
        return result + "";
    }

    public static String deleteUser(String account, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.delete("user","account=?", new String[] {account});
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "deleteUser");
        return result + "";
    }

    public static ArrayList<User> queryUser(String[] columns, String selection, String[] selectionArgs, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user", columns, selection, selectionArgs, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return null;
        }

        ArrayList<User> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            User user = new User();
            if (cursor.getColumnIndex("account") != -1) {
                user.setAccount(cursor.getString(cursor.getColumnIndex("account")));
            }
            if (cursor.getColumnIndex("password") != -1) {
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            }
            users.add(user);
        }
        cursor.close();
        db.close();
        return users;

    }
}
