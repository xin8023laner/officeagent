package com.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 公共的工具类 Created by zhangruntao on 15/5/13.
 */
public class CommonUtils {

	private CommonUtils() {
	}

	;

	/**
	 * 检查电话号码的格式
	 * 
	 * @param phoneNum
	 * @return
	 */
	public static boolean isPhoneNum(String phoneNum) {
		Pattern p = Pattern
				.compile("^((1[0-9])|(1[0-9])|(1[0-9])|(1[0-9]))\\d{9}$");
		Matcher m = p.matcher(phoneNum);
		return m.matches();
	}

	/**
	 * 检查邮箱格式
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 检查邮政编码格式
	 * 
	 * @param postcode
	 * @return
	 */
	public static boolean isPostcode(String postcode) {
		String format = "\\p{Digit}{6}";
		if (postcode.matches(format)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 拨打电话
	 * 
	 * @param postcode
	 * @return
	 */
	public static void call(Context context, String phone) {
		Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
		call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(call);
	}

}
