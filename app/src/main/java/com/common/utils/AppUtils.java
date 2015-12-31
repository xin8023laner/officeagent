package com.common.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.model.Agent;
import com.common.model.AgentCarrier;
import com.common.model.AgentUser;
import com.gt.o2o.util.GtDigestUtils;
import com.lidroid.xutils.http.RequestParams;

//跟App相关的辅助类
public class AppUtils {

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本号信息]
	 * 
	 * @param context
	 * @return 当前应用的版本号
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/** 获取用户手机的系统版本，返回的是API Level */
	public static int getSDKVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * whether application is in background 程序是否在后台.
	 * <ul>
	 * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
	 * </ul>
	 * 
	 * @param context
	 * @return if application is in background return true, otherwise return
	 *         false 如果程序在后台则返回true，反之false
	 */
	public static boolean isApplicationInBackground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
		if (taskList != null && !taskList.isEmpty()) {
			ComponentName topActivity = taskList.get(0).topActivity;
			if (topActivity != null
					&& !topActivity.getPackageName().equals(
							context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 构建参数
	 */
	public static int getCode(String requestCode) {

		return requestCode.codePointAt(requestCode.length());
	}

	/**
	 * 
	 * 构建参数
	 */
	public static RequestParams createParams(String requestCode,
			String paramsValues) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("platform", "Android");
		params.addBodyParameter("appKey", Constant.APP_KEY_ANDROID);
		params.addBodyParameter("requestCode", requestCode);
//		params.addBodyParameter("timestamp",
//				String.valueOf(System.currentTimeMillis()));
		params.addBodyParameter("timestamp","1448433905339");

		params.addBodyParameter("params", paramsValues);
		params.addBodyParameter("sign",
				AppUtils.getSign(requestCode, paramsValues));

		return params;
	}

	/**
	 * 
	 * 得到签名
	 */
	public static String getSign(String requestCode, String params) {
		// 构造数据签名需要使用的参数 Map
		Map<String, String> signMap = new HashMap<String, String>();
		signMap.put("platform", "Android");
		signMap.put("requestCode", requestCode);
		signMap.put("params", params);

		// 生成数据签名
		String sign = null;
		try {
			sign = GtDigestUtils.createDigestSign(Constant.APP_KEY_ANDROID,
					Constant.APP_SECRET_ANDROID, signMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * whether the app whost package's name is packageName is on the top of the
	 * stack 根据传入的packageName参数和堆栈里第一个程序包名称比较，如果相同，说明在栈顶，反之在其他位置
	 * <ul>
	 * <strong>Attentions:</strong>
	 * <li>You should add <strong>android.permission.GET_TASKS</strong> in
	 * manifest</li>
	 * </ul>
	 * 
	 * @param context
	 * @param packageName
	 * @return if params error or task stack is null, return null, otherwise
	 *         retun whether the app is on the top of stack 如果在栈顶则返回true，反之false
	 */
	public static Boolean isTopActivity(Context context, String packageName) {
		if (context == null
				|| (packageName == null || packageName.trim().length() == 0)) {
			return null;
		}

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager
				.getRunningTasks(1);
		if (tasksInfo.isEmpty()) {
			return null;
		}
		try {
			return packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * [判断是否登录]
	 * 
	 * @param context
	 * @return 是否登录
	 */
	public static boolean isLogined(Context context) {

		if (SPUtils.getUser(context) != null
				&& SPUtils.getAgent(context) != null) {
			return true;
		}

		return false;
	}

	/**
	 * [登录操作]
	 * 
	 * @param context
	 */
	public static void login(Context context, AgentUser user, Agent agent) {
		SPUtils.setUser(context, user);
		SPUtils.setAgent(context, agent);
	}

	/**
	 * [登出操作]
	 * 
	 * @param context
	 */
	public static void loginOut(Context context) {
		SPUtils.setUser(context, null);
		SPUtils.setAgent(context, null);
	}
	public static List<AgentCarrier> getAgentCarriers(Context context){
		Long agentId=SPUtils.getAgent(context).getAgentId();
		List<AgentCarrier> agentCarriers=new ArrayList<AgentCarrier>();
		AgentCarrier agentCarrier;
		 agentCarrier=new AgentCarrier(agentId,"400-1111-119",Long.valueOf(1182), "yousu","优速快递");
		 agentCarriers.add(agentCarrier);
		 agentCarrier=new AgentCarrier(agentId,"400-100-0001",Long.valueOf(1183), "quanfeng","全峰快递");
		 agentCarriers.add(agentCarrier);
		 agentCarrier=new AgentCarrier(agentId,"021-69777888",Long.valueOf(1184), "yuantong","圆通速递");
		 agentCarriers.add(agentCarrier);
		 agentCarrier=new AgentCarrier(agentId,"400-889-5543",Long.valueOf(1185), "shentong","申通快递");
		 agentCarriers.add(agentCarrier);
		 agentCarrier=new AgentCarrier(agentId,"11183",Long.valueOf(1186), "ems","EMS");
		 agentCarriers.add(agentCarrier);
		 agentCarrier=new AgentCarrier(agentId,"021-62963636",Long.valueOf(1187), "huitong","百世汇通");
		 agentCarriers.add(agentCarrier);
		 agentCarrier=new AgentCarrier(agentId,"929992292",Long.valueOf(1179), "1195060656","韵达快递");
		 agentCarriers.add(agentCarrier);
//		 agentCarrier=new AgentCarrier(Long.valueOf(1180), "", "国通快递","699665156");
//		 agentCarriers.add(agentCarrier);
		 agentCarrier=new AgentCarrier(agentId,"23343423",Long.valueOf(1181), "shunfeng","顺丰速运");
		 agentCarriers.add(agentCarrier);
		 agentCarrier=new AgentCarrier(agentId,"400-6789-000",Long.valueOf(1181), "zjs","宅急送");
		 agentCarriers.add(agentCarrier);
		 agentCarrier=new AgentCarrier(agentId,"400-830-5555",Long.valueOf(1190), "debang","德邦物流");
		 agentCarriers.add(agentCarrier);
//		 agentCarrier=new AgentCarrier(Long.valueOf(1189), "", "乐捷递","lejiedi");
//		 agentCarriers.add(agentCarrier);
//		 agentCarrier=new AgentCarrier(Long.valueOf(1188), "", "东方快递","coe");
//		 agentCarriers.add(agentCarrier);
		return agentCarriers;
		
	}
}
