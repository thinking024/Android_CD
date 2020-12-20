package com.yiyuanzhu.thinking.dao.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yiyuanzhu.thinking.dao.DbHelper;
import com.yiyuanzhu.thinking.pojo.Course;
import com.yiyuanzhu.thinking.pojo.Grade;

import java.util.ArrayList;
import java.util.UUID;

public class GradeCRUD {
    public static String insertGrade(String account, Grade grade, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        ContentValues values = new ContentValues();
        values.put("id", uuid);
        values.put("account", grade.getAccount());
        values.put("term", grade.getTerm());
        values.put("name", grade.getName());
        values.put("score", grade.getScore());
        values.put("credit", grade.getCredit());
        values.put("grade_point", grade.getGradePoint());
        values.put("term_again", grade.getTerm_again());
        values.put("exam_type", grade.getExam_type());
        values.put("course_type", grade.getCourse_type());
        long result = db.insert("grade", null, values);
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "insertGrade");
        return uuid;

    }

    public static String deleteGrade(String account, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.delete("grade","account=?", new String[] {account});
        db.close();
        if (result == -1) {
            return null;
        }
        Log.i("db", "deleteGrade");
        return result + "";
    }

    public static ArrayList<Grade> queryGrade(String[] columns, String selection, String[] selectionArgs, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("grade", columns, selection, selectionArgs, null, null, "term DESC");
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return null;
        }
        ArrayList<Grade> grades = new ArrayList<>();
        while (cursor.moveToNext()) {
            Grade grade = new Grade();
            if (cursor.getColumnIndex("id") != -1) {
                grade.setId(cursor.getString(cursor.getColumnIndex("id")));
            }
            if (cursor.getColumnIndex("account") != -1) {
                grade.setAccount(cursor.getString(cursor.getColumnIndex("account")));
            }
            if (cursor.getColumnIndex("term") != -1) {
                grade.setTerm(cursor.getString(cursor.getColumnIndex("term")));
            }
            if (cursor.getColumnIndex("name") != -1) {
                grade.setName(cursor.getString(cursor.getColumnIndex("name")));
            }
            if (cursor.getColumnIndex("score") != -1) {
                grade.setScore(cursor.getString(cursor.getColumnIndex("score")));
            }
            if (cursor.getColumnIndex("credit") != -1) {
                grade.setCredit(cursor.getString(cursor.getColumnIndex("credit")));
            }
            if (cursor.getColumnIndex("grade_point") != -1) {
                grade.setGradePoint(cursor.getString(cursor.getColumnIndex("grade_point")));
            }
            if (cursor.getColumnIndex("term_again") != -1) {
                grade.setTerm_again(cursor.getString(cursor.getColumnIndex("term_again")));
            }
            if (cursor.getColumnIndex("exam_type") != -1) {
                grade.setExam_type(cursor.getString(cursor.getColumnIndex("exam_type")));
            }
            if (cursor.getColumnIndex("course_type") != -1) {
                grade.setCourse_type(cursor.getString(cursor.getColumnIndex("course_type")));
            }
            grades.add(grade);
        }
        cursor.close();
        db.close();
        return grades;

    }
}
