package com.yiyuanzhu.thinking;

import com.yiyuanzhu.thinking.pojo.Course;
import com.yiyuanzhu.thinking.pojo.Grade;
import com.yiyuanzhu.thinking.pojo.MyClass;
import com.yiyuanzhu.thinking.pojo.CourseTable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Pair;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
class MyCookies implements CookieJar {
    HashMap<String, List<Cookie>> cookieStore;

    public MyCookies() {
        super();
    }

    public MyCookies(HashMap<String, List<Cookie>> cookieStore) {
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        if (cookieStore.get(httpUrl.host()) == null) {
//            List<Cookie>是由Array.asList转化而来，不能直接add,remove
            ArrayList<Cookie> arrayList = new ArrayList<>(list);
            cookieStore.put(httpUrl.host(), arrayList);
        } else {
            for (Cookie cookie : list) {
                cookieStore.get(httpUrl.host()).add(cookie);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> cookies = cookieStore.get(httpUrl.host());
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }
}

public class ExampleUnitTest {
    public void print(Request request, Response response, HashMap<String, List<Cookie>> cookieStore) {
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

    @Test
    public void addition_isCorrect() {
        String userAccount = "180505021";
        String password = "hn095573";
        String encodeUrl = "http://kdjw.hnust.edu.cn//Logon.do?method=logon&flag=sess";
        String dataStr = null;
        HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        MyCookies myCookies = new MyCookies(cookieStore);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(myCookies)
                .followRedirects(false)
                .build();

        Request encodeRequest = new Request.Builder()
                .url(encodeUrl)
                .build();

        System.out.println(1);
        try (Response response = client.newCall(encodeRequest).execute()) {
            if (response.isSuccessful()) {
                print(encodeRequest, response, cookieStore);
                dataStr = response.body().string();
                System.out.println(dataStr);

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
                System.out.println("encoded = " + encoded);

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
                System.out.println(2);
                print(loginRequest, loginResponse, cookieStore);
                System.out.println(loginResponse.code());

                String redirectUrl = loginResponse.header("Location");
                Request redirectRequest = new Request.Builder()
                        .url(redirectUrl)
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
                        .build();

                Response redirectResponse = client.newCall(redirectRequest).execute();
                System.out.println(redirectResponse.code());
                System.out.println(3);
                print(redirectRequest, redirectResponse, cookieStore);

                String homeUrl = "http://kdjw.hnust.edu.cn/jsxsd/framework/xsMain.jsp";
                Request homeRequest = new Request.Builder()//创建Request 对象。
                        .url(homeUrl)
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
                        .build();

                Response homeResponse = client.newCall(homeRequest).execute();
                System.out.println(homeResponse.code());
                System.out.println(4);
                print(homeRequest, homeResponse, cookieStore);
//                System.out.println(homeResponse.body().string());

                String courseUrl = "http://kdjw.hnust.edu.cn/jsxsd/xskb/xskb_list.do";
                Request courseRequest = new Request.Builder()//创建Request 对象。
                        .url(courseUrl)
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
                        .build();

                Response courseResponse = client.newCall(courseRequest).execute();
                System.out.println(courseResponse.code());
                System.out.println(5);
                print(courseRequest, courseResponse, cookieStore);

//                System.out.println(courseResponse.body().string());

                String gradeUrl = "http://kdjw.hnust.edu.cn/jsxsd/kscj/cjcx_list";
                Request gradeRequest = new Request.Builder()//创建Request 对象。
                        .url(gradeUrl)
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
                        .build();
                Response gradeResponse = client.newCall(gradeRequest).execute();

                System.out.println(gradeResponse.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void redirect() throws Exception{
        String userAccount = "1805050213";
        String password = "hn095573";
        String encodeUrl = "http://kdjw.hnust.edu.cn//Logon.do?method=logon&flag=sess";
        String dataStr = null;
        HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        MyCookies myCookies = new MyCookies(cookieStore);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(myCookies)
                .build();

        Request encodeRequest = new Request.Builder()
                .url(encodeUrl)
                .build();

//        System.out.println(1);
        Response response = client.newCall(encodeRequest).execute();
        if (response.isSuccessful()) {
            print(encodeRequest, response, cookieStore);
            dataStr = response.body().string();
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
            System.out.println(loginResponse.code());
            System.out.println(loginResponse.request().url().toString());

            String courseUrl = "http://kdjw.hnust.edu.cn/jsxsd/xskb/xskb_list.do";
            Request courseRequest = new Request.Builder()//创建Request 对象。
                    .url(courseUrl)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
                    .build();

            Response courseResponse = client.newCall(courseRequest).execute();
//            System.out.println(courseResponse.code());

            System.out.println(courseResponse.body().string());

            FormBody.Builder formBody2 = new FormBody.Builder();//创建表单请求体
            formBody2.add("cj0701id", "");
            formBody2.add("zc", "");
            formBody2.add("demo", "");
            formBody2.add("xnxq01id", "2019-2020-2");
            formBody2.add("sfFD", "1");
            formBody2.add("wkbkc", "1");
            formBody2.add("kbjcmsid", "0B841F8A531A4C05BE8DB7DB4B40AEF1");

            Request courseRequest2 = new Request.Builder()//创建Request 对象。
                    .url(courseUrl)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
                    .post(formBody2.build())//传递请求体
                    .build();

            Response courseResponse2 = client.newCall(courseRequest2).execute();

            System.out.println(courseResponse2.body().string());
        }
    }

    public static ArrayList<Grade> gradeParser(String html, String account) {
        System.out.println(html);
        Document doc = Jsoup.parse(html);
        Elements tr = doc.getElementsByTag("tr");
        System.out.println(tr.size());
        /*for (Element element : tr) {
            System.out.println(element);
        }*/
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
            System.out.println(arrayList);

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
        for (Grade grade : grades) {
            System.out.println(grade);
        }
        return grades;
    }
    @Test
    public void course() throws Exception{
        String userAccount = "1805050213";
        File input = new File("F:/课程/安卓课设/湖南科技大学_files/xskb_list.html");
        Document doc = Jsoup.parse(input, "UTF-8");
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
                    System.out.println(classInfo);
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
                    System.out.println(courseName);
                    System.out.println(time);
                    String order = time.substring(time.indexOf(")"));
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
        for (Course course : courses) {
            System.out.println(course.getCourseName());
            for (MyClass myClass : course.getMyClasses()) {
                System.out.println(myClass);
            }
        }
    }


    @Test
    public void grade() throws Exception {
        File input = new File("C:\\Users\\26438\\Desktop\\学生个人考试成绩.html");
        Document doc = Jsoup.parse(input, "UTF-8");

//        doc.getElemntBy

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
            grades.add(grade);
        }
        for (Grade grade : grades) {
            System.out.println(grade);
        }
    }

}