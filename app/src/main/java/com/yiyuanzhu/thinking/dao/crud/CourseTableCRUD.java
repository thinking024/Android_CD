package com.yiyuanzhu.thinking.dao.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yiyuanzhu.thinking.dao.DbHelper;
import com.yiyuanzhu.thinking.pojo.Course;
import com.yiyuanzhu.thinking.pojo.CourseTable;
import com.yiyuanzhu.thinking.pojo.MyClass;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CourseTableCRUD {
    public static String insertCourseTable(CourseTable courseTable, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        ContentValues values = new ContentValues();
        values.put("id", uuid);
        values.put("account", courseTable.getAccount());
        values.put("table_name", courseTable.getTableName());
        long result = db.insert("course_table", null, values);
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "insertCourseTable");
        return uuid;
    }

    public static String updateCourseTable(ContentValues values, String id, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.update("course_table", values, "id=?", new String[] {id});
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "updateCourseTable");
        return result + "";
    }

    public static String deleteCourseTable(String selection, String[] selectionargs, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.delete("course_table",selection, selectionargs);
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "deleteCourseTable");
        return result + "";
    }

    public static ArrayList<CourseTable> queryCourseTable(String[] columns, String selection, String[] selectionArgs, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("course_table", columns, selection, selectionArgs, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return null;
        }

        ArrayList<CourseTable> tables = new ArrayList<>();
        while (cursor.moveToNext()) {
            CourseTable table = new CourseTable();
            if (cursor.getColumnIndex("id") != -1) {
                table.setId(cursor.getString(cursor.getColumnIndex("id")));
            }
            if (cursor.getColumnIndex("account") != -1) {
                table.setAccount(cursor.getString(cursor.getColumnIndex("account")));
            }
            if (cursor.getColumnIndex("table_name") != -1) {
                table.setTableName(cursor.getString(cursor.getColumnIndex("table_name")));
            }
            tables.add(table);
        }
        cursor.close();
        db.close();
        return tables;

    }

}
