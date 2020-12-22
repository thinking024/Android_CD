package com.yiyuanzhu.thinking.utils.crawler;

import com.yiyuanzhu.thinking.pojo.Course;
import com.yiyuanzhu.thinking.pojo.CourseTable;
import com.yiyuanzhu.thinking.pojo.Grade;
import com.yiyuanzhu.thinking.pojo.MyClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Pair;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Integer.parseInt;


public class HNUSTCrawler {
    public static void printInfo(Request request, Response response, HashMap<String, List<Cookie>> cookieStore) {
        System.out.println("request headers");
        for (Pair<? extends String, ? extends String> header : request.headers()) {
            System.out.println(header);
        }

        System.out.println("response headers");
        for (Pair<? extends String, ? extends String> header : response.headers()) {
            System.out.println(header);
        }

        System.out.println("cookies");
        for (Map.Entry<String, List<Cookie>> stringListEntry : cookieStore.entrySet()) {
            System.out.println(stringListEntry);
        }
    }

    public static ArrayList<CourseTable> getCourse(String userAccount, String password) throws Exception {
        String encodeUrl = "http://kdjw.hnust.edu.cn//Logon.do?method=logon&flag=sess";
        HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        MyCookies myCookies = new MyCookies(cookieStore);
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(myCookies).build();

        Request encodeRequest = new Request.Builder().url(encodeUrl).build();
        Response response = client.newCall(encodeRequest).execute();
        if (response.isSuccessful()) {
            String dataStr = response.body().string();
            String scode = dataStr.split("#")[0];
            String sxh = dataStr.split("#")[1];
            String code = userAccount + "%%%" + password;
            String encoded = "";
            for (int i = 0; i < code.length(); i++) {
                if (i < 20) {
                    encoded = encoded + code.substring(i, i + 1) + scode.substring(0, parseInt(sxh.substring(i, i + 1)));
                    scode = scode.substring(parseInt(sxh.substring(i, i + 1)));
                } else {
                    encoded = encoded + code.substring(i);
                    i = code.length();
                }
            }
//            System.out.println("encoded = " + encoded);

            String loginUrl = "http://kdjw.hnust.edu.cn/Logon.do?method=logon";
            FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
            formBody.add("userAccount", userAccount);
            formBody.add("userPassword", "");
            formBody.add("encoded", encoded);

            Request loginRequest = new Request.Builder()//创建Request 对象。
                    .url(loginUrl)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
                    .post(formBody.build())//传递请求体
                    .build();

            Response loginResponse = client.newCall(loginRequest).execute();
//            System.out.println(loginResponse.code());
//            System.out.println(loginResponse.request().url());

            String homeUrl = "http://kdjw.hnust.edu.cn/jsxsd/framework/xsMain.jsp";
            if (loginResponse.isSuccessful()) {
                if (homeUrl.equals(loginResponse.request().url().toString().trim())) {
                    ArrayList<CourseTable> courseTables = new ArrayList<>();

                    String courseUrl = "http://kdjw.hnust.edu.cn/jsxsd/xskb/xskb_list.do";
                    String[] terms = {"2020-2021-1","2019-2020-2","2019-2020-1"};
                    for (String term : terms) {
                        FormBody.Builder courseFormBody = new FormBody.Builder();//创建表单请求体
                        courseFormBody.add("cj0701id", "");
                        courseFormBody.add("zc", "");
                        courseFormBody.add("demo", "");
                        courseFormBody.add("xnxq01id", term);
                        courseFormBody.add("sfFD", "1");
                        courseFormBody.add("wkbkc", "1");
                        courseFormBody.add("kbjcmsid", "0B841F8A531A4C05BE8DB7DB4B40AEF1");

                        Request courseRequest = new Request.Builder()//创建Request 对象。
                                .url(courseUrl)
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
                                .post(courseFormBody.build())
                                .build();

                        Response courseResponse = client.newCall(courseRequest).execute();
                        CourseTable courseTable = courseParser(courseResponse.body().string());
                        courseTable.setAccount(userAccount);
                        courseTables.add(courseTable);
                    }

                    return courseTables;

                } else {
                    System.out.println("password error");
                    return null;
                }
            } else {
                System.out.println("error");
                return null;
            }
        }
        return null;
    }

    public static ArrayList<Grade> getGrade(String userAccount, String password) throws Exception {
        String encodeUrl = "http://kdjw.hnust.edu.cn//Logon.do?method=logon&flag=sess";
        HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        MyCookies myCookies = new MyCookies(cookieStore);
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(myCookies).build();

        Request encodeRequest = new Request.Builder().url(encodeUrl).build();
        Response response = client.newCall(encodeRequest).execute();
        if (response.isSuccessful()) {
            String dataStr = response.body().string();
//            System.out.println(dataStr);

            // 加密算法
            String scode = dataStr.split("#")[0];
            String sxh = dataStr.split("#")[1];
            String code = userAccount + "%%%" + password;
            String encoded = "";
            for (int i = 0; i < code.length(); i++) {
                if (i < 20) {
                    encoded = encoded + code.substring(i, i + 1) + scode.substring(0, parseInt(sxh.substring(i, i + 1)));
                    scode = scode.substring(parseInt(sxh.substring(i, i + 1)));
                } else {
                    encoded = encoded + code.substring(i);
                    i = code.length();
                }
            }
//            System.out.println("encoded = " + encoded);

            String loginUrl = "http://kdjw.hnust.edu.cn/Logon.do?method=logon";
            FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
            formBody.add("userAccount", userAccount);
            formBody.add("userPassword", "");
            formBody.add("encoded", encoded);

            Request loginRequest = new Request.Builder()//创建Request 对象。
                    .url(loginUrl)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
                    .post(formBody.build())//传递请求体
                    .build();

            Response loginResponse = client.newCall(loginRequest).execute();

            String homeUrl = "http://kdjw.hnust.edu.cn/jsxsd/framework/xsMain.jsp";
            if (loginResponse.isSuccessful()) {
                if (homeUrl.equals(loginResponse.request().url().toString().trim())) {
                    String gradeUrl = "http://kdjw.hnust.edu.cn/jsxsd/kscj/cjcx_list";
                    Request gradeRequest = new Request.Builder()//创建Request 对象。
                            .url(gradeUrl)
                            .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
                            .build();
                    Response gradeResponse = client.newCall(gradeRequest).execute();
                    ArrayList<Grade> grades = gradeParser(gradeResponse.body().string(), userAccount);

                    return grades;

                } else {
                    System.out.println("password error");
                    return null;
                }
            } else {
                System.out.println("error");
                return null;
            }
        }
        return null;
    }

    public static CourseTable courseParser(String html) {
//        System.out.println(html);
        Document doc = Jsoup.parse(html);
        String courseTableName = doc.getElementById("xnxq01id").getElementsByAttributeValue("selected", "selected").first().text();
        System.out.println(courseTableName);
        CourseTable courseTable = new CourseTable();
        courseTable.setTableName(courseTableName);

        ArrayList<Course> courses = new ArrayList<>();
        Elements kbcontent = doc.getElementsByClass("kbcontent");

        for (Element element : kbcontent) {
            String content = element.text();
            if (!("".equals(content)) && content != null) {
                String id = element.attr("id");
                String day = id.substring(id.indexOf("-") + 1, id.lastIndexOf("-"));
                /*System.out.println("=======");
                System.out.println(day);*/

//                一个格子有多节课，遍历每个格子里的单节课
                String[] classStrings = content.split("---------------------");
                for (String classString : classStrings) {
//                    每节课的信息
                    String classInfo = classString.trim();
//                    System.out.println(classInfo);
                    String[] strings = classInfo.split(" ");

                    String courseName = strings[0];
                    if (courseName.contains("体育")) {
                        continue;
                    }

                    String courseInfo = strings[strings.length - 1];
                    String classroom = strings[strings.length - 2].substring(strings[strings.length - 2].indexOf("】") + 1);
                    String time = strings[strings.length - 3];
                    String teacher = strings[strings.length - 4];

                    Course course = new Course();
                    course.setCourseName(courseName);
                    if (courses.contains(course) == false) {
                        course.setTeacher(teacher);
                        course.setCourseInfo(courseInfo);
                        courses.add(course);
                    } else {
                        course = courses.get(courses.indexOf(course));
                    }

//                    1-2，5-6周拆成两节课
                    String order = time.substring(time.indexOf("("));
                    int orderBeginIndex = order.indexOf("[");
                    int orderIndex = order.indexOf("-");
                    int orderIndex2 = order.lastIndexOf("-");
                    int orderEndIndex = order.indexOf("节");
                    String orderBegin = order.substring(orderBeginIndex + 1, orderIndex);
                    String orderEnd = order.substring(orderIndex2 + 1, orderEndIndex);

                    String[] weeks = time.substring(0, time.indexOf("(")).split(",");
                    for (String week : weeks) {
                        MyClass myClass = new MyClass();
//                        System.out.println(week);
                        String weekBegin = "";
                        String weekEnd = "";
                        if (week.contains("-")) {
                            int weekIndex = week.indexOf("-");
                            weekBegin += week.substring(0, weekIndex);
                            weekEnd += week.substring(weekIndex + 1);
                        } else {
                            weekBegin += week;
                            weekEnd += week;
                        }

                        myClass.setWeekBegin(Integer.parseInt(weekBegin));
                        myClass.setWeekEnd(Integer.parseInt(weekEnd));
                        myClass.setClassroom(classroom);
                        myClass.setOrderBegin(Integer.parseInt(orderBegin));
                        myClass.setOrderEnd(Integer.parseInt(orderEnd));
                        myClass.setDay(Integer.parseInt(day));

                        if (course.getMyClasses() == null) {
                            ArrayList<MyClass> myClasses = new ArrayList<>();
                            myClasses.add(myClass);
                            course.setMyClasses(myClasses);
                        } else {
                            course.getMyClasses().add(myClass);
                        }
                    }
                }

            }
        }
        courseTable.setCourses(courses);
        return courseTable;
    }

    public static ArrayList<Grade> gradeParser(String html, String account) {
//        System.out.println(html);
        Document doc = Jsoup.parse(html);
        Elements tr = doc.getElementsByTag("tr");
        Element head = tr.remove(0);

        ArrayList<Grade> grades = new ArrayList<>();
        for (Element element : tr) {
            Grade grade = new Grade();
            String[] text = element.text().trim().split(" ");
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(text));
            if (text.length == 12) {
                arrayList.add(8, "");
            }
            arrayList.remove(0);
            arrayList.remove(1);
            arrayList.remove(4);
//            System.out.println(arrayList);

            grade.setTerm(arrayList.get(0));
            grade.setName(arrayList.get(1));
            grade.setScore(arrayList.get(2));
            grade.setCredit(arrayList.get(3));
            grade.setGradePoint(arrayList.get(4));
            grade.setTerm_again(arrayList.get(5));
            grade.setExam_type(arrayList.get(6) + " " + arrayList.get(7));
            grade.setCourse_type(arrayList.get(8) + " " + arrayList.get(9));
            grade.setAccount(account);

            grades.add(grade);
        }
        /*for (Grade grade : grades) {
            System.out.println(grade);
        }*/
        return grades;
    }

    public static CourseTable localCourseParser() throws Exception{
//        System.out.println(html);
        File input = new File("/data/data/com.yiyuanzhu.thinking/学期理论课表.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        Element timetable = doc.getElementById("timetable");
        Element datatable = doc.getElementById("datatable");

        String courseTableName = doc.getElementById("xnxq01id").getElementsByAttributeValue("selected", "selected").first().text();
        System.out.println(courseTableName);
        CourseTable courseTable = new CourseTable();
        courseTable.setTableName(courseTableName);

        ArrayList<Course> courses = new ArrayList<>();
        Elements kbcontent = doc.getElementsByClass("kbcontent");

        for (Element element : kbcontent) {
            String content = element.text();
            if (!("".equals(content)) && content != null) {
                String id = element.attr("id");
                String day = id.substring(id.indexOf("-") + 1, id.lastIndexOf("-"));
                /*System.out.println("=======");
                System.out.println(day);*/

//                一个格子有多节课，遍历每个格子里的单节课
                String[] classStrings = content.split("---------------------");
                for (String classString : classStrings) {
//                    每节课的信息
                    String classInfo = classString.trim();
//                    System.out.println(classString.trim());
                    String[] strings = classInfo.split(" ");
//                    System.out.println(strings.length);
                    String courseName = strings[0];
                    String teacher = strings[1];
                    String courseInfo = strings[4];

                    Course course = new Course();
                    course.setCourseName(courseName);
                    if (courses.contains(course) == false) {
                        course.setTeacher(teacher);
                        course.setCourseInfo(courseInfo);
                        courses.add(course);
                    } else {
                        course = courses.get(courses.indexOf(course));
                    }

                    String classroom = strings[3].substring(strings[3].indexOf("】") + 1);

                    String time = strings[2];
//                    System.out.println(time);

                    // 一门课被拆分成很多周
                    int orderBeginIndex = time.indexOf("[");
                    int orderIndex = time.lastIndexOf("-");
                    int orderEndIndex = time.indexOf("节");
                    String orderBegin = time.substring(orderBeginIndex + 1, orderIndex);
                    String orderEnd = time.substring(orderIndex + 1, orderEndIndex);
                    /*System.out.println(orderBegin);
                    System.out.println(orderEnd);*/

//                    1-2，5-6周拆成两节课
                    String[] weeks = time.substring(0, time.indexOf("(")).split(",");
                    for (String week : weeks) {
                        MyClass myClass = new MyClass();
//                        System.out.println(week);
                        String weekBegin = "";
                        String weekEnd = "";
                        if (week.contains("-")) {
                            int weekIndex = week.indexOf("-");
                            weekBegin += week.substring(0, weekIndex);
                            weekEnd += week.substring(weekIndex + 1);
                        } else {
                            weekBegin += week;
                            weekEnd += week;
                        }
                        /*System.out.println(weekBegin);
                        System.out.println(weekEnd);*/

                        myClass.setWeekBegin(Integer.parseInt(weekBegin));
                        myClass.setWeekEnd(Integer.parseInt(weekEnd));
                        myClass.setClassroom(classroom);
                        myClass.setOrderBegin(Integer.parseInt(orderBegin));
                        myClass.setOrderEnd(Integer.parseInt(orderEnd));
                        myClass.setDay(Integer.parseInt(day));

                        if (course.getMyClasses() == null) {
                            ArrayList<MyClass> myClasses = new ArrayList<>();
                            myClasses.add(myClass);
                            course.setMyClasses(myClasses);
                        } else {
                            course.getMyClasses().add(myClass);
                        }
                    }
                }

            }
        }
        courseTable.setCourses(courses);
        for (Course cours : courseTable.getCourses()) {
            System.out.println(cours);
            for (MyClass myClass : cours.getMyClasses()) {
                System.out.println(myClass);
            }
            System.out.println();
        }
        return courseTable;
    }

}
