package com.yiyuanzhu.thinking.dao.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yiyuanzhu.thinking.dao.DbHelper;
import com.yiyuanzhu.thinking.pojo.MyClass;

import java.util.ArrayList;
import java.util.UUID;

public class MyClassCRUD {

    public static String insertMyclass(String courseId, MyClass myClass, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        ContentValues values = new ContentValues();
        values.put("id", uuid);
        values.put("course_id", courseId);
        values.put("day", myClass.getDay());
        values.put("week_begin", myClass.getWeekBegin());
        values.put("week_end", myClass.getWeekEnd());
        values.put("order_begin", myClass.getOrderBegin());
        values.put("order_end", myClass.getOrderEnd());
        values.put("classroom", myClass.getClassroom());
        long result = db.insert("myclass", null, values);
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "insertMyclass");
        return uuid;

    }

    public static String updateMyClass(ContentValues values, String id, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.update("myclass", values, "id=?", new String[] {id});
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "updateMyClass");
        return result + "";
    }

    public static String deleteMyClass(String id, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.delete("myclass","id=?", new String[] {id});
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "deleteMyClass");
        return result + "";
    }

    public static ArrayList<MyClass> queryMyClass(String[] columns, String selection, String[] selectionArgs, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("myclass", columns, selection, selectionArgs, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return null;
        }
        ArrayList<MyClass> myClasses = new ArrayList<>();
        while (cursor.moveToNext()) {
            MyClass myClass = new MyClass();
            if (cursor.getColumnIndex("id") != -1) {
                myClass.setId(cursor.getString(cursor.getColumnIndex("id")));
            }
            if (cursor.getColumnIndex("course_id") != -1) {
                myClass.setCourseId(cursor.getString(cursor.getColumnIndex("course_id")));
            }
            if (cursor.getColumnIndex("day") != -1) {
                myClass.setDay(cursor.getInt(cursor.getColumnIndex("day")));
            }
            if (cursor.getColumnIndex("week_begin") != -1) {
                myClass.setWeekBegin(cursor.getInt(cursor.getColumnIndex("week_begin")));
            }
            if (cursor.getColumnIndex("week_end") != -1) {
                myClass.setWeekEnd(cursor.getInt(cursor.getColumnIndex("week_end")));
            }
            if (cursor.getColumnIndex("order_begin") != -1) {
                myClass.setOrderBegin(cursor.getInt(cursor.getColumnIndex("order_begin")));
            }
            if (cursor.getColumnIndex("order_end") != -1) {
                myClass.setOrderEnd(cursor.getInt(cursor.getColumnIndex("order_end")));
            }
            if (cursor.getColumnIndex("classroom") != -1) {
                myClass.setClassroom(cursor.getString(cursor.getColumnIndex("classroom")));
            }
            myClasses.add(myClass);
        }

        cursor.close();
        db.close();
        return myClasses;
    }
}
