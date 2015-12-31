package com.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * 时间工具类
 * 
 * @author way
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {
	/**
	 * 根据时间戳转换字符串时间格式
	 * 
	 * @param time
	 * @return
	 */
	public static String parseTimeToString(long time, String formater) {
		SimpleDateFormat format = new SimpleDateFormat(formater);
		return format.format(new Date(time));
	}

	/**
	 * 根据字符串时间格式转换系统时间戳
	 * 
	 * @param strTime
	 * @return
	 */
	public static long parseStringToMillis(String strTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date d = format.parse(strTime);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 得到日期
	 * 
	 * @param strTime
	 * @return
	 */
	public static String getData(long strTime) {
		
		return parseTimeToString(strTime,"MM-dd");
	}
	/**
	 * 得到时间
	 * 
	 * @param strTime
	 * @return
	 */
	public static String getTime(long strTime) {
		
		return parseTimeToString(strTime,"HH:mm");
	}

	/**
	 * 获取聊天时间
	 * 
	 * @param timesamp
	 * @return
	 */
	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + parseTimeToString(timesamp, "HH:mm");
			break;
		case 1:
			result = "昨天 " + parseTimeToString(timesamp, "HH:mm");
			break;
		case 2:
			result = "前天 " + parseTimeToString(timesamp, "HH:mm");
			break;

		default:
			// result = temp + "天前 ";
			result = parseTimeToString(timesamp, "yyyy-MM-dd HH:mm:dd");
			break;
		}

		return result;
	}
}
