package com.taidii.diibot.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class DateUtils {

    private static SimpleDateFormat[] dateFormats = null;
    private static SimpleDateFormat[] gMTDateFormats = null;

    static {
        final String[] possibleDateFormats = {
                "yyyy-MM-dd HH:mm",
                "MM/dd/yyyy HH:mm:ss a",
                "EEE, dd MMM yyyy HH:mm:ss zzz", // RFC_822
                "EEE, dd MMM yyyy HH:mm:ss z", // RFC_822
                "EEE, dd MMM yyyy HH:mm zzzz",
                "yyyy-MM-dd'T'HH:mm:ssZ",
                "yyyy-MM-dd'T'HH:mm:sszzzz",
                "yyyy-MM-dd'T'HH:mm:ss z",
                "yyyy-MM-dd'T'HH:mm:ssz", // ISO_8601
                "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HHmmss.SSSz",
                "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
                "yyyy/MM/dd", "yyyy.MM.dd", "yyyy'年'MM'月'dd'日'",
                "EEE,dd MMM yyyy HH:mm:ss zzz", // 容错
                "EEE, dd MMM yyyy HH:mm:ss", // RFC_822
                "dd MMM yyyy HH:mm:ss zzz", // 容错
                "dd MM yyyy HH:mm:ss zzz", // 容错
                "EEE, dd MM yyyy HH:mm:ss", // RFC_822
                "dd MM yyyy HH:mm:ss", // 容错
                "EEE MMM dd HH:mm:ss zzz yyyy" // bokee 的时间格式 Tue Mar 28
                // 02:25:45 CST 2006
        };

        dateFormats = new SimpleDateFormat[possibleDateFormats.length];
        gMTDateFormats = new SimpleDateFormat[possibleDateFormats.length];
        TimeZone gmtTZ = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.getDefault();
        for (int i = 0; i < possibleDateFormats.length; i++) {
            dateFormats[i] = new SimpleDateFormat(possibleDateFormats[i],
                    locale);
            gMTDateFormats[i] = new SimpleDateFormat(possibleDateFormats[i],
                    locale);
            gMTDateFormats[i].setTimeZone(gmtTZ);
        }
    }

    /**
     * 获取字符串的日期格式
     *
     * @param strdate 格式串
     * @return 格式化的格式串
     */
    public static Date getDate(String strdate) {
        if (strdate == null)
            return null;
        strdate = strdate.trim();
        if (strdate.length() > 10) {

            if ((strdate.substring(strdate.length() - 5).indexOf("+") == 0 || strdate
                    .substring(strdate.length() - 5).indexOf("-") == 0)
                    && strdate.substring(strdate.length() - 5).indexOf(":") == 2) {

                String sign = strdate.substring(strdate.length() - 5,
                        strdate.length() - 4);

                strdate = strdate.substring(0, strdate.length() - 5) + sign
                        + "0" + strdate.substring(strdate.length() - 4);
            }

            String dateEnd = strdate.substring(strdate.length() - 6);
            if ((dateEnd.indexOf("-") == 0 || dateEnd.indexOf("+") == 0)
                    && dateEnd.indexOf(":") == 3) {

                if ("GMT".equals(strdate.substring(strdate.length() - 9,
                        strdate.length() - 6))) {

                } else {
                    String oldDate = strdate;
                    String newEnd = dateEnd.substring(0, 3)
                            + dateEnd.substring(4);
                    strdate = oldDate.substring(0, oldDate.length() - 6)
                            + newEnd;

                }
            }
        }
        int i = 0;
        Date result = null;
        while (i < dateFormats.length) {
            try {
                result = dateFormats[i].parse(strdate);
                break;
            } catch (java.text.ParseException eA) {
                i++;
            }
        }
        return result;
    }

    /**
     * 获取字符串的日期格式
     *
     * @param strdate 格式串
     * @return 格式化的格式串
     */
    public static Date getGMTDate(String strdate) {
        if (strdate == null)
            return null;
        strdate = strdate.trim();
        if (strdate.length() > 10) {

            if ((strdate.substring(strdate.length() - 5).indexOf("+") == 0 || strdate
                    .substring(strdate.length() - 5).indexOf("-") == 0)
                    && strdate.substring(strdate.length() - 5).indexOf(":") == 2) {

                String sign = strdate.substring(strdate.length() - 5,
                        strdate.length() - 4);

                strdate = strdate.substring(0, strdate.length() - 5) + sign
                        + "0" + strdate.substring(strdate.length() - 4);
            }

            String dateEnd = strdate.substring(strdate.length() - 6);
            if ((dateEnd.indexOf("-") == 0 || dateEnd.indexOf("+") == 0)
                    && dateEnd.indexOf(":") == 3) {

                if ("GMT".equals(strdate.substring(strdate.length() - 9,
                        strdate.length() - 6))) {

                } else {
                    String oldDate = strdate;
                    String newEnd = dateEnd.substring(0, 3)
                            + dateEnd.substring(4);
                    strdate = oldDate.substring(0, oldDate.length() - 6)
                            + newEnd;

                }
            }
        }
        int i = 0;
        Date result = null;
        while (i < gMTDateFormats.length) {
            try {
                result = gMTDateFormats[i].parse(strdate);
                break;
            } catch (java.text.ParseException eA) {
                i++;
            }
        }
        return result;
    }


    /**
     * 根据时间加或减天数
     *
     * @param date 指定时间
     * @param day  天数
     * @return 添加day后的时间
     */
    public static Date addDay(Date date, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return calendar.getTime();
    }

    /**
     * 删除时间中的秒，精确到分钟
     *
     * @param dateStr
     * @return
     */
    public static String formatDate(String dateStr) {
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = format1.parse(dateStr);
            dateStr = format2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 指定格式日期转字符串
     *
     * @param dDate   date类型日期
     * @param pattern 格式
     * @return String类型日期
     */
    public static String formatDate(Date dDate, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern,
                java.util.Locale.getDefault());
        String dateString = null;
        try {
            if (dDate != null) {
                formatter.setLenient(false);
                dateString = formatter.format(dDate);
            }
        } catch (Exception ex) {
            System.out.println("Date" + dDate + "to String is error");
        }
        return dateString;
    }

    public static String transform(String from) {
        String time = "";

        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 本地时区
        Calendar nowCal = Calendar.getInstance();
        TimeZone localZone = nowCal.getTimeZone();
        // 设定SDF的时区为本地
        simple.setTimeZone(localZone);

        // 把字符串转化为Date对象，然后格式化输出这个Date
        // 时间string解析成GMT时间
        Date fromDate = getDate(from);
        // GMT时间转成当前时区的时间
        time = simple.format(fromDate);
        return time;
    }

    public static String getFirstDayOfMonth(Date date) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        // 获取当月第一天和最后一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday;
        // 获取前月的第一天
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());

        return firstday;
    }

    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        final Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        final long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        final long time2 = cal.getTimeInMillis();
        final long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days)) + 1;
    }

    public static Date strToDate(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
                Date date = sdf.parse(str);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public static String getWeekByDateStr(String strDate, boolean isShort) {
        int year = Integer.parseInt(strDate.substring(0, 4));
        int month = Integer.parseInt(strDate.substring(5, 7));
        int day = Integer.parseInt(strDate.substring(8, 10));

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);

        String week = "";
        int weekIndex = c.get(Calendar.DAY_OF_WEEK);

        String[] mWeekString;
        if (Utils.getCurrentLanguage().contains("zh")) {
            mWeekString = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        } else {
            if (isShort) {
                mWeekString = new String[]{"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};

            } else {
                mWeekString = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

            }
        }


        switch (weekIndex) {
            case 1:
                week = mWeekString[0];
                break;
            case 2:
                week = mWeekString[1];
                break;
            case 3:
                week = mWeekString[2];
                break;
            case 4:
                week = mWeekString[3];
                break;
            case 5:
                week = mWeekString[4];
                break;
            case 6:
                week = mWeekString[5];
                break;
            case 7:
                week = mWeekString[6];
                break;
        }
        return week;
    }

    public static long compareDate(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            long diff = endDate.getTime() - startDate.getTime();
            return diff;
        } catch (ParseException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return 0;
    }

    public static Map<String, Calendar> getRangDate() {
        Map<String, Calendar> map = new HashMap<String, Calendar>();
        Calendar calendar = Calendar.getInstance();
        try {
            String startYear = "2015-01";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date startdate = sdf.parse(String.valueOf(startYear));
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startdate);
            map.put("start", startCalendar);

            int endYear = calendar.get(Calendar.YEAR) + 5;
            Date endDate = sdf.parse(String.valueOf(endYear) + "-" + "12");
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            map.put("end", endCalendar);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static long getDistanceDays(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     */
    public static int isDateOneBigger(String str1, String str2) {
        if (str1.length() < 11) {
            str1 = str1 + " 00:00:00";
        }
        if (str2.length() < 11) {
            str2 = str2 + " 00:00:00";
        }
        int isBigger = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = -1;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = 1;
        } else {
            isBigger = 0;
        }
        return isBigger;
    }

    public static int isCalendarDateOneBigger(String str1, String str2, int type1, int type2, int calendateType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (calendateType) {
            case 0:
                if (str1.length() < 11 || str2.length() < 11) {
                    str1 = str1.substring(0, 10);
                    str2 = str2.substring(0, 10);
                    sdf = new SimpleDateFormat("yyyy-MM-dd");

                }
                break;
            case 1:
                if (str1.length() < 11 || str2.length() < 11 || containEnglish(str1) || containEnglish(str2)) {
                    str1 = str1.substring(0, 10);
                    str2 = str2.substring(0, 10);
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                }
                break;
        }


        int isBigger = 0;
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            if (calendateType == 0) {
                isBigger = 1;
            } else {
                isBigger = -1;
            }
        } else if (dt1.getTime() < dt2.getTime()) {
            if (calendateType == 0) {
                isBigger = -1;
            } else {
                isBigger = 1;
            }
        } else {
            if (type1 > type2) {
                isBigger = -1;
            } else if (type1 < type2) {
                isBigger = 1;
            } else if (type1 == type2) {
                isBigger = 0;
            }

        }
        return isBigger;
    }

    private static boolean containEnglish(String str) {
        String regEx = ".*[a-zA-Z]+.*";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        boolean b = matcher.matches();
        return b;
    }

    public static String parseDate(int number) {
        int minute = ((number / 100) / 60) % 60;
        int second = (number / 100) % 60;
        int m_second = (number % 100);

        String strMinute, strSecond, strMSecond;
        if (minute < 10)
            strMinute = "0" + minute;
        else
            strMinute = "" + minute;
        if (second < 10)
            strSecond = "0" + second;
        else
            strSecond = "" + second;
        if (m_second < 10)
            strMSecond = "0" + m_second;
        else
            strMSecond = "" + m_second;

        return strMinute + ":" + strSecond + ":" + strMSecond;

    }

    /**
     * 判断时间是否在时间段内
     *
     * @return
     */
    public static boolean belongCalendar() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        try {
            Date nowTime = df.parse(df.format(new Date()));
            Date beginTime = df.parse("00:01");
            Date endTime = df.parse("13:00");

            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);

            Calendar begin = Calendar.getInstance();
            begin.setTime(beginTime);

            Calendar end = Calendar.getInstance();
            end.setTime(endTime);

            if (date.after(begin) && date.before(end)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
