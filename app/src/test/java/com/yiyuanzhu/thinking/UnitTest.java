package com.yiyuanzhu.thinking;

import android.util.Log;

import com.yiyuanzhu.thinking.pojo.User;
import com.yiyuanzhu.thinking.utils.GlobalInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kotlin.Pair;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Integer.parseInt;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
class MyCookies2 implements CookieJar {
    HashMap<String, List<Cookie>> cookieStore;

    public MyCookies2() {
        super();
    }

    public MyCookies2(HashMap<String, List<Cookie>> cookieStore) {
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        cookieStore.put(httpUrl.host(), list);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> cookies = cookieStore.get(httpUrl.host());
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }
}

public class UnitTest {
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
        String userAccount = "1805050213";
        String password = "hn095573";
        String encodeUrl = "http://kdjw.hnust.edu.cn//Logon.do?method=logon&flag=sess";
        String dataStr = null;
        HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        MyCookies2 myCookies = new MyCookies2(cookieStore);
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
                formBody.add("userPassword", password);
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
                System.out.println(homeResponse.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test8() {
        String userAccount ="1805050225";
        String password = "ffgghhh";
        String dataStr = "p6Ls01N52131Ei4PZ20fq13x684Cayd6x8baUEj17Cp#33233322111133133122";
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
    }

    @Test
    public void test7() {
    }
}