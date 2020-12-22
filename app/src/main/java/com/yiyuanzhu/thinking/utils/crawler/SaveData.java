package com.yiyuanzhu.thinking.utils.crawler;

import android.content.ContentValues;
import android.content.Context;

import com.yiyuanzhu.thinking.dao.crud.CourseCRUD;
import com.yiyuanzhu.thinking.dao.crud.CourseTableCRUD;
import com.yiyuanzhu.thinking.dao.crud.GradeCRUD;
import com.yiyuanzhu.thinking.dao.crud.MyClassCRUD;
import com.yiyuanzhu.thinking.dao.crud.UserCRUD;
import com.yiyuanzhu.thinking.pojo.Course;
import com.yiyuanzhu.thinking.pojo.CourseTable;
import com.yiyuanzhu.thinking.pojo.Grade;
import com.yiyuanzhu.thinking.pojo.MyClass;
import com.yiyuanzhu.thinking.pojo.User;

import java.util.ArrayList;

public class SaveData {
    public static void saveCourse(User user, Context context) {
        try {
            ArrayList<CourseTable> courseTables = HNUSTCrawler.getCourse(user.getAccount(), user.getPassword());

            for (CourseTable courseTable : courseTables) {
                System.out.println(courseTable);
                String tableId = CourseTableCRUD.insertCourseTable(courseTable, context);
                if (tableId != null) {
                    for (Course course : courseTable.getCourses()) {
                        String courseId = CourseCRUD.insertCourse(tableId, course, context);
                        if (courseId != null) {
                            for (MyClass myclass : course.getMyClasses()) {
                                String myclassId = MyClassCRUD.insertMyclass(courseId, myclass, context);
                                if (myclass == null) {
                                    System.out.println("myclass insert error");
                                }
                            }
                        } else {
                            System.out.println("course insert error");
                        }
                    }
                } else {
                    System.out.println("table insert error");
                }
            }

//将第一个课表作为默认课表
            ContentValues contentValues = new ContentValues();
            contentValues.put("default_table", courseTables.get(0).getTableName());
            UserCRUD.updateUser(contentValues, user.getAccount(), context);

            System.out.println(UserCRUD.queryUser(null,null,null,context));

            ArrayList<MyClass> myClasses = MyClassCRUD.queryMyClass(null, null, null, context);
            if (myClasses != null) {
                for (MyClass myClass : myClasses) {
                    System.out.println(myClass);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveGrade(User user, Context context) {
        try {
            ArrayList<Grade> grades = HNUSTCrawler.getGrade(user.getAccount(), user.getPassword());
            for (Grade grade : grades) {
                GradeCRUD.insertGrade(user.getAccount(), grade, context);
            }

            ArrayList<Grade> gradeArrayList = GradeCRUD.queryGrade(null, null, null, context);
            if (gradeArrayList != null) {
                for (Grade grade : gradeArrayList) {
                    System.out.println(grade);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
