package com.taidii.diibot.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.TouchDelegate;
import android.view.View;
import android.view.Window;

import com.taidii.diibot.app.BaseApplication;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {
    // return object's member data and its value in string format
    public final static String logObject(Object obj) {
        return JsonUtils.toJson(obj);
    }

    public final static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isAvailable();
    }

    public final static int dip2px(float dpValue) {
        return (int) (dpValue * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 获取系统当前使用的言
     * @return "zh"中文，"en"英文
     */
    public final static String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 得到状态栏的高度
     * @param activity
     * @return
     */
    public final static int getStatusBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    //获取状态栏高度
    public final static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到除状态栏以外屏幕的高度
     * @param activity
     * @return
     */
    public final static int getWindowVisibleDisplayFrameHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        View v = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        Rect rect = new Rect();
        v.getWindowVisibleDisplayFrame(rect);
        return rect.height();
    }

    public final static String encodeUrl(String url) {
        String encodedUrl = "";
        try {
            encodedUrl = URLEncoder.encode(url, "UTF-8")
                    .replaceAll("\\+", "%20").replaceAll("%3A", ":")
                    .replaceAll("%2F", "/").replaceAll("%3F", "?");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedUrl;
    }

    public final static String convertYMD2DMY(String dateStr) {
        if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return "";
        }
        String[] res = dateStr.split("-");
        return new StringBuilder().append(res[2]).append("-").append(res[1]).append("-").append
                (res[0]).toString();
    }

    public final static int getKeyboardHeight(Activity paramActivity) {
        int height = ScreenUtils.getScreenHeight(paramActivity) - getStatusBarHeight
                (paramActivity) - getAppHeight(paramActivity);
        if (height == 0) {
            height = SharePrefUtils.getInt("KeyboardHeight", 787 + dip2px(38));//787为默认软键盘高度 基本差不离
        } else {
            SharePrefUtils.saveInt("KeyboardHeight", height);
        }
        return height;
    }

    /**
     * 可见屏幕高度
     **/
    public final static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    /**
     * 扩大view的点击区域
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */

    public static void expandViewTouchDelegate(final View view, final int top,
                                               final int bottom, final int left, final int right) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }
    /**
    * 判断是否为中文
    *
    **/
    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }


    public static String PickUpNum(String str){
        if (null != str){
            String regEx="[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return String.valueOf(m.replaceAll("").trim());
        }
        return null;
    }

    public static String utc2Local(String utcTime) {
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//UTC时间格式
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当地时间格式
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }
    public static String ToDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                    c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                    c[i] = (char) (c[i] - 65248);

            }
        }
        return new String(c);
    }

    /**
     * 判断字符串中是否有超链接，若有，则返回超链接。
     * @param str
     * @return
     */
    public static String[] judgeString(String str){
        Matcher m = Pattern.compile("(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)").matcher(str);
        String[] url = new String[str.length()/5];
        int count = 0;
        while(m.find()){
            count++;
            url[count] = m.group();
        }
        return url;
    }

    //把一个字符串中的大写转为小写，小写转换为大写
    public static String exChange(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append(Character.toLowerCase(c));
                }else if(Character.isLowerCase(c)){
                    sb.append(Character.toUpperCase(c));
                }
            }
        }
        return sb.toString();
    }

    //字符中的大写改为小写
    public static String exChangeLower(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append(Character.toLowerCase(c));
                }else if(Character.isLowerCase(c)){
                    sb.append(c);
                }else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

}
