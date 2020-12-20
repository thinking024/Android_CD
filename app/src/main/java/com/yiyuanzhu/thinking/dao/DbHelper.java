package com.yiyuanzhu.thinking.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context, "Thinking.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");

        String createUser = "CREATE TABLE user (account varchar(20) NOT NULL,password varchar(255) NOT NULL,PRIMARY KEY (account))";
        db.execSQL(createUser);

        String createCourseTable = "CREATE TABLE course_table (\n" +
                "  id varchar(255) PRIMARY KEY NOT NULL,\n" +
                "  account varchar(20) NOT NULL,\n" +
                "  table_name varchar(255) NOT NULL,\n" +
                "  CONSTRAINT account_fk FOREIGN KEY (account) REFERENCES user (account) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ")";
        db.execSQL(createCourseTable);
        db.execSQL("create unique index account_table_u on course_table (account, table_name)");

        String createCourse = "CREATE TABLE course (\n" +
                "  id varchar(255) NOT NULL,\n" +
                "  course_table_id varchar(255) NOT NULL,\n" +
                "  course_name varchar(255) NOT NULL,\n" +
                "  teacher varchar(255) NOT NULL,\n" +
                "  course_info varchar(255) NOT NULL,\n" +
                "  PRIMARY KEY (id),\n" +
                "  CONSTRAINT table_id_fk FOREIGN KEY (course_table_id) REFERENCES course_table (id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ")";
        db.execSQL(createCourse);
        db.execSQL("create unique index table_course_u on course (course_table_id, course_name)");

        String createMyClass = "CREATE TABLE myclass (\n" +
                "  id varchar(255) NOT NULL,\n" +
                "  course_id varchar(255) NOT NULL,\n" +
                "  day int NOT NULL,\n" +
                "  week_begin int NOT NULL,\n" +
                "  week_end int NOT NULL,\n" +
                "  order_begin int NOT NULL,\n" +
                "  order_end int NOT NULL,\n" +
                "  classroom varchar(255) NOT NULL,\n" +
                "  PRIMARY KEY (id),\n" +
                "  CONSTRAINT course_id_fk FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ")";
        db.execSQL(createMyClass);

        String createGrade = "CREATE TABLE grade (\n" +
                "  id varchar(255) NOT NULL,\n" +
                "  account varchar(255) NOT NULL,\n" +
                "  term varchar(225) NOT NULL,\n" +
                "  name varchar(255) NOT NULL,\n" +
                "  score varchar(255) NOT NULL,\n" +
                "  credit varchar(255) NOT NULL,\n" +
                "  grade_point varchar(225) NOT NULL,\n" +
                "  term_again varchar(255) NOT NULL,\n" +
                "  exam_type varchar(255) NOT NULL,\n" +
                "  course_type varchar(255) NOT NULL,\n" +
                "  PRIMARY KEY (id),\n" +
                "  CONSTRAINT grade_account_fk FOREIGN KEY (account) REFERENCES user (account) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ")";
        db.execSQL(createGrade);
        Log.i("db","create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }
}
