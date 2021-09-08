package com.taidii.diibot.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	private static final int YESTERDY = -1;
	private static final int TOMORROWDAT = 1;
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
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
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

	public static long getStringToDate(String dateString, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date date = new Date();
		try{
			date = dateFormat.parse(dateString);
		} catch(ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回短时间格式 yyyy-MM-dd
	 */
	public static Date getNowDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取现在一分钟后的时间
	 */
	public static String getAfter1sTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date=new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, +1);
		date = calendar.getTime();
		return sdf.format(date);
	}

	/**
	 * 获取现在时间IOS
	 *
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDateIOS() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'Z'");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取时间 小时:分;秒 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeShort() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取时间 小时:分;秒 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeShort(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 *
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	public static Date strToLongDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 得到现在时间
	 *
	 * @return
	 */
	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}

	/**
	 *
	 */

	public static String getGTM(String dateTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT-08"));
		String dateValue = formatter.format(strToLongDate(dateTime));
		return dateValue;
	}

	public static String utc2Local(String utcTime) {
		try {
			if (TextUtils.isEmpty(utcTime)) {
				return "";
			}
			SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date gpsUTCDate = null;
			try {
				gpsUTCDate = utcFormater.parse(utcTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			localFormater.setTimeZone(TimeZone.getDefault());
			String localTime = localFormater.format(gpsUTCDate.getTime());
			return localTime;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String stampToDate(String GMT) {

		long lt = Long.parseLong(GMT)-8*3600000;
		String res = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			res = simpleDateFormat.format(lt);
		}catch (Exception e){
			e.printStackTrace();
		}

		return res;
	}

	public static String gmtToDate(String dateTime) {
		long time = DateUtil.getStringToDate(dateTime,"yyyy-MM-dd HH:mm:ss");
		return DateUtil.stampToDate(time+"");
	}


	/**
	 * 提取前几天的时间
	 *
	 * @param day
	 * @return
	 */
	public static Date getLastDate(long day) {
		Date date = new Date();
		long date_3_hm = date.getTime() - 1000 * 3600 * 24 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}

	/**
	 * 提取后几天的时间
	 *
	 * @param day
	 * @return
	 */

	public static Date getAfterDate(long day) {
		Date date = new Date();
		long date_3_hm = date.getTime() + 1000 * 3600 * 24 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}

	/**
	 * 得到现在小时
	 */
	public static String getHour() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String hour;
		hour = dateString.substring(11, 13);
		return hour;
	}

	/**
	 * 得到现在分钟
	 *
	 * @return
	 */
	public static String getTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String min;
		min = dateString.substring(14, 16);
		return min;
	}

	/**
	 * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
	 */
	public static String getTwoHour(String st1, String st2) {
		String[] kk = null;
		String[] jj = null;
		kk = st1.split(":");
		jj = st2.split(":");

		double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60;
		double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60;
		if ((y - u) > 0)
			return (y - u) * 60 + "";
		else
			return (y - u) * 60 * (-1) + "";

	}

	/**
	 * 得到二个日期间的间隔天数
	 */
	public static int getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			Date date = myFormatter.parse(sj1);
			Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return 0;
		}
		return (int) day;
	}

	/**
	 * 时间前推或后推分钟,其中JJ表示分钟.
	 */
	public static String getPreTime(String sj1, String jj) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mydate1 = "";
		try {
			Date date1 = format.parse(sj1);
			long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
			date1.setTime(Time * 1000);
			mydate1 = format.format(date1);
		} catch (Exception e) {
		}
		return mydate1;
	}

	/**
	 * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
	 */
	public static String getNextDay(String nowDate, int delay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String mdate = "";
			Date d = strToDate(nowDate);
			long myTime = (d.getTime() / 1000) + delay * 24 * 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 得到一个时间"XX月XX日"
	 */
	public static String getMonthAndDay(String nowdate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String mdate = "";
			Date d = strToDate(nowdate);
			mdate = (d.getMonth() + 1) + "月" + d.getDate() + "日";
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 得到一个时间"XXXX年XX月XX日"
	 */
	public static String getYearMonthDay(String nowdate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String mdate = "";
			Date d = strToDate(nowdate);
			mdate = d.getYear() + "年" + (d.getMonth() + 1) + "月" + d.getDate() + "日";
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 判断是否润年
	 *
	 * @param ddate
	 * @return
	 */
	public static boolean isLeapYear(String ddate) {

		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		 * 3.能被4整除同时能被100整除则不是闰年
		 */
		Date d = strToDate(ddate);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if ((year % 400) == 0)
			return true;
		else if ((year % 4) == 0) {
			if ((year % 100) == 0)
				return false;
			else
				return true;
		} else
			return false;
	}

	/**
	 * 返回美国时间格式 26 Apr 2006
	 *
	 * @param str
	 * @return
	 */
	public static String getEDate(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(str, pos);
		String j = strtodate.toString();
		String[] k = j.split(" ");
		return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
	}

	/**
	 * 获取一个月的最后一天
	 *
	 * @param dat
	 * @return
	 */
	public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
		String str = dat.substring(0, 8);
		String month = dat.substring(5, 7);
		int mon = Integer.parseInt(month);
		if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
			str += "31";
		} else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
			str += "30";
		} else {
			if (isLeapYear(dat)) {
				str += "29";
			} else {
				str += "28";
			}
		}
		return str;
	}

	/**
	 * 判断二个时间是否在同一个周
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * 产生周序列,即得到当前时间所在的年度是第几周
	 *
	 * @return
	 */
	public static String getSeqWeek() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1)
			week = "0" + week;
		String year = Integer.toString(c.get(Calendar.YEAR));
		return year + week;
	}

	/**
	 * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
	 *
	 * @param sdate
	 * @param num
	 * @return
	 */
	public static String getWeek(String sdate, String num) {
		// 再转换为时间
		Date dd = DateUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(dd);
		if (num.equals("1")) // 返回星期一所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		else if (num.equals("2")) // 返回星期二所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		else if (num.equals("3")) // 返回星期三所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		else if (num.equals("4")) // 返回星期四所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		else if (num.equals("5")) // 返回星期五所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		else if (num.equals("6")) // 返回星期六所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		else if (num.equals("0")) // 返回星期日所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}


	/**
	 * 根据一个日期，返回是星期几的数字
	 *
	 * @param sdate
	 * @return
	 */
	public static int getWeekNum(String sdate) {
		// 再转换为时间
		Date date = DateUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		 int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return hour;
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 *
	 * @param sdate
	 * @return
	 */
	public static String getWeek(String sdate) {
		// 再转换为时间
		Date date = DateUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	public static String getWeekStr(String sdate) {
		String str = "";
		str = DateUtil.getWeek(sdate);
		if ("1".equals(str)) {
			str = "星期日";
		} else if ("2".equals(str)) {
			str = "星期一";
		} else if ("3".equals(str)) {
			str = "星期二";
		} else if ("4".equals(str)) {
			str = "星期三";
		} else if ("5".equals(str)) {
			str = "星期四";
		} else if ("6".equals(str)) {
			str = "星期五";
		} else if ("7".equals(str)) {
			str = "星期六";
		}
		return str;
	}

	/**
	 * 两个时间之间的天数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	/**
	 * 比较两个时间，得到时间差秒
	 * @param dateSmall  2017-01-20 13:14:31
	 * @param dateBig  2017-01-20 13:14:31
	 * @return
	 */
	public static long getSecond(String dateSmall, String dateBig){
		long DateTime = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = format.parse(dateSmall);
			Date d2 = format.parse(dateBig);

			long time = d2.getTime()-d1.getTime();
			time = Math.abs(time);
			long sen = time % (1000*60);

			if (sen > 0){
				DateTime = sen;
			} else{
				return 0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return DateTime;
	}

	/**
	 * 根据一个日期，返回g该日期所在月的第一天是星期几
	 * 
	 * @param sdate
	 * @return
	 */
	public static int getWeekNumOfMonthStart(String sdate) {
		// 取该时间所在月的一号2015-01-01
		sdate = sdate.substring(0, 8) + "01";

		// 得到这个月的1号是星期几
		Date date = DateUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int u = c.get(Calendar.DAY_OF_WEEK);
		return u;
	}

	/**
	 * 形成如下的日历 ， 根据传入的一个时间返回一个结构 星期日 星期一 星期二 星期三 星期四 星期五 星期六 下面是当月的各个时间
	 * 此函数返回该日历第一行星期日所在的日期
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getNowMonth(String sdate) {
		// 取该时间所在月的一号2015-01-01
		sdate = sdate.substring(0, 8) + "01";

		// 得到这个月的1号是星期几
		Date date = DateUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int u = c.get(Calendar.DAY_OF_WEEK);
		String newday = DateUtil.getNextDay(sdate, (1 - u));
		return newday;
	}

	/**
	 * 获得通话时长的字符串，如80s->00:01:20
	 * 
	 * @param duration
	 * @return
	 */
	public static String getStrTime(long duration) {
		String time = null;
		long hour = 0;
		long minute = 0;
		long second = 0;

		if (duration < 59) {
			time = "00:00:" + (duration < 10 ? ("0" + duration) : duration);
		} else {
			minute = duration / 60;
			second = duration % 60;
			if (minute < 59) {
				time = "00:" + (minute < 10 ? ("0" + minute) : minute) + ":" + (second < 10 ? ("0" + second) : second);
			} else {
				hour = minute / 60;
				minute = minute % 60;

				time = hour + ":" + (minute < 10 ? ("0" + minute) : minute) + ":" + (second < 10 ? ("0" + second) : second);
			}
		}
		return time;
	}

	/**
	 * 获取上个月的时间
	 */
	public static Date getLastMothDate() {
		Date curMonthDate = new Date();
		Date lastMonthDate = getLastDate(curMonthDate.getDate() + 1);
		return lastMonthDate;
	}

	/**
	 * 第一个date是否大于第二个
	 * @param oneDate   第一个date
	 * @param twoDate	第二个date
	 * @return
	 */
	public static boolean compare1DateGraterThan2Date(String oneDate, String twoDate)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//如果时间格式没有对应会报异常
		if(oneDate.contains(" ") && oneDate.contains(":")){
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		
		Date startDate= null;
		Date endDate= null;
		try {
			startDate = format.parse(oneDate);
			endDate = format.parse(twoDate);
			long startSecend=startDate.getTime();
			long endSecend=endDate.getTime();
			return startSecend>endSecend;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 获取两个日期之间的小时数
	 * @param smallDate
	 * @param bigDate
	 * @return
	 */
	public static float getTwoDateBetwenHours(String smallDate, String bigDate){
		try{

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate= format.parse(smallDate);
			Date endDate= format.parse(bigDate);
			
			float betwenSecond = endDate.getTime() - startDate.getTime();
			float hours = betwenSecond/(1000f*60f*60f);
			return hours;
			
		}catch(Exception e){
			e.printStackTrace();
			return 0f;
		}
	}
	
	/**
	 * 第一个日期是否大于第二个日期
	 * @param oneDate   第一个date
	 * @param twoDate	第二个date
	 * @return
	 */
	public static boolean compare1DayGraterThan2Day(String oneDate, String twoDate)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate= null;
		Date endDate= null;
		try {
			startDate = format.parse(oneDate);
			endDate = format.parse(twoDate);
			long startSecend=startDate.getTime();
			long endSecend=endDate.getTime();
			return startSecend>endSecend;
		} catch (ParseException e) {
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * 判断当前日期是星期几<br>
	 * <br>
	 * @param pTime 修要判断的时间<br>
	 * @return dayForWeek 判断结果<br>
	 * @Exception 发生异常<br>
	 */
	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if(c.get(Calendar.DAY_OF_WEEK) == 1){
			dayForWeek = 7;
		}else{
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

    /**
     *  转换指定格式的日期
     * @param s         原始日期
     * @param format    格式
     */
    public static String formatNewStr(String s, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date=strToLongDate(s);
        return dateFormat.format(date);
    }

	/**
	 *  转换指定格式的日期
	 * @param s         原始日期
	 * @param format    格式
	 */
	public static String formatShortStr(String s, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date=strToDate(s);
		return dateFormat.format(date);
	}

    /**
     *  转换日期到毫秒数
     * @param s         原始日期
     */
    public static long formatStrToLong(String s){
        Date date=strToDate(s);
        return date.getTime();
    }

	/**
	 * 比较两个时间，得到时间差
	 * @param dateSmall  2017-01-20 13:14:31
	 * @param dateBig  2017-01-20 13:14:31
     * @return
     */
	public static String compareDate(String dateSmall, String dateBig){

		String text = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = format.parse(dateSmall);
			Date d2 = format.parse(dateBig);

			long time = d2.getTime()-d1.getTime();
			time = Math.abs(time);

			//time /= 1000;

			long alDaySecond = 60*60*24*1000;
			long day = time / alDaySecond;
			long hour= (time % alDaySecond)/(1000*60*60);
			long min = (time % (1000*60*60))/(1000*60);
			long sen = time % (1000*60);

			if(day > 0){
				text += (day+"D  ");
			}else if(hour > 0){

				text += (hour + "H  ");
			}else if (min > 0){
				text += (min+"m  ");
			}else if (sen > 0){
				text += (time+"s  ");
			} else{
				return dateSmall;
			}



		} catch (ParseException e) {
			e.printStackTrace();
		}

		return text;
	}

	/**
	 * 比较两个时间，得到时间差(视频时长)
	 * @param dateSmall  2017-01-20 13:14:31
	 * @param dateBig  2017-01-20 13:14:31
	 * @return
	 */
	public static String compareVideoDate(String dateSmall, String dateBig){

		String text = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = format.parse(dateSmall);
			Date d2 = format.parse(dateBig);

			long time = d2.getTime()-d1.getTime();
			time = Math.abs(time);

			//time /= 1000;

			long alDaySecond = 60*60*24*1000;
			long day = time / alDaySecond;
			long hour= (time % alDaySecond)/(1000*60*60);
			long min = (time % (1000*60*60))/(1000*60);
			long sen = time % (1000*60);

			if(day > 0){
				text += (day+"D  ");
			}else if(hour > 0){
				text += (hour + "H  ");
			}else if (min > 0){
				text += (min+"m  ");
			}else if (sen > 0){
				text += time;
			} else{
				return dateSmall;
			}



		} catch (ParseException e) {
			e.printStackTrace();
		}

		return text;
	}

	/**
	 * 比较两个时间，得到时间差
	 * @param dateSmall  2017-01-20 13:14:31
	 * @param dateBig  2017-01-20 13:14:31
	 * @return
	 */

	public static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 将字符串转位日期类型
	 *
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 判断是否为昨天(效率比较高)
	 */
	public static boolean IsYesterday(String day) throws ParseException {

		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);
		Calendar cal = Calendar.getInstance();
		Date date = getDateFormat().parse(day);
		cal.setTime(date);
		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);

			if (diffDay == -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断日期是否是当天
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static boolean IsToday(String day) throws ParseException {

		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);
		Calendar cal = Calendar.getInstance();
		Date date = getDateFormat().parse(day);
		cal.setTime(date);
		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);

			if (diffDay == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断日期是否是明天
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static boolean IsTomorrowday(String day) throws ParseException {

		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);
		Calendar cal = Calendar.getInstance();
		Date date = getDateFormat().parse(day);
		cal.setTime(date);
		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);

			if (diffDay == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断日期是否是后天
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static boolean IsAfterTomorrowDay(String day) throws ParseException {

		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);
		Calendar cal = Calendar.getInstance();
		Date date = getDateFormat().parse(day);
		cal.setTime(date);
		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);

			if (diffDay == 2) {
				return true;
			}
		}
		return false;
	}

	public static SimpleDateFormat getDateFormat() {
		if (null == DateLocal.get()) {
			DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
		}
		return DateLocal.get();
	}

	private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

	/**
	 * 时间戳转换成时间
	 */
	public String times(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		return times;
	}


	//出生日期字符串转化成Date对象
	public static Date parse(String strDate){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(strDate);
		}catch (ParseException e){
			return null;
		}
	}

	//由出生日期获得年龄
	public static int getAge(Date birthDay){
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) age--;
			}else{
				age--;
			}
		}
		return age;
	}

	public static String currentMonth() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化对象
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return sdf.format(calendar.getTime());

	}
}
