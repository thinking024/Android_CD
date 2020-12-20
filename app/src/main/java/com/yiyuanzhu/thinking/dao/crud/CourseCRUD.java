package com.yiyuanzhu.thinking.dao.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yiyuanzhu.thinking.dao.DbHelper;
import com.yiyuanzhu.thinking.pojo.Course;

import java.util.ArrayList;
import java.util.UUID;

public class CourseCRUD {
    public static String insertCourse(String tableId, Course course, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        ContentValues values = new ContentValues();
        values.put("id", uuid);
        values.put("course_table_id", tableId);
        values.put("course_name", course.getCourseName());
        values.put("teacher", course.getTeacher());
        values.put("course_info", course.getCourseInfo());
        long result = db.insert("course", null, values);
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "insertCourse");
        return uuid;

    }

    public static String updateCourse(ContentValues values, String id, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.update("course", values, "id=?", new String[] {id});
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "updateCourse");
        return result + "";
    }

    public static String deleteCourse(String id, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.delete("course","id=?", new String[] {id});
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "deleteCourse");
        return result + "";
    }

    public static ArrayList<Course> queryCourse(String[] columns, String selection, String[] selectionArgs, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("course", columns, selection, selectionArgs, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return null;
        }
        ArrayList<Course> courses = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course course = new Course();
            if (cursor.getColumnIndex("id") != -1) {
                course.setId(cursor.getString(cursor.getColumnIndex("id")));
            }
            if (cursor.getColumnIndex("course_table_id") != -1) {
                course.setCourseTableId(cursor.getString(cursor.getColumnIndex("course_table_id")));
            }
            if (cursor.getColumnIndex("course_name") != -1) {
                course.setCourseName(cursor.getString(cursor.getColumnIndex("course_name")));
            }
            if (cursor.getColumnIndex("teacher") != -1) {
                course.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
            }
            if (cursor.getColumnIndex("course_info") != -1) {
                course.setCourseInfo(cursor.getString(cursor.getColumnIndex("course_info")));
            }
            courses.add(course);
        }
        cursor.close();
        db.close();
        return courses;

    }
}
