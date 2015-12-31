package com.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 30 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告. 31 * 32 * @author
 * user 33 * 34
 */
@SuppressLint("SdCardPath")
public class CrashHandlerUtils implements UncaughtExceptionHandler {

	public static final String TAG = "TEST";

	// CrashHandler 实例
	private static CrashHandlerUtils INSTANCE = new CrashHandlerUtils();

	// 程序的 Context 对象
	private Context mContext;

	// 系统默认的 UncaughtException 处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	// 用来存储设备信息和异常信息
	private static Map<String, String> infos = new HashMap<String, String>();

	// 用来显示Toast中的信息
	private static String error = "程序错误，额，不对，我应该说，服务器正在维护中，请稍后再试";

	private static final Map<String, String> regexMap = new HashMap<String, String>();

	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
			Locale.CHINA);

	/** 保证只有一个 CrashHandler 实例 */
	private CrashHandlerUtils() {
		//
	}

	/** 获取 CrashHandler 实例 ,单例模式 */
	public static CrashHandlerUtils getInstance() {
		initMap();
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;

		// 获取系统默认的 UncaughtException 处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		
		// 设置该 CrashHandler 为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
		Log.d("TEST", "Crash:init");
	}

	/**
	 * 当 UncaughtException 发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
			Log.d("TEST", "defalut");
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// 退出程序
			ExitAppUtils.getInstance().exit();
		}
	}

	/**
	 * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
	 * 
	 * @param ex
	 * @return true：如果处理了该异常信息；否则返回 false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(ex);
		sendPreviousReportsToServer();
		// 使用 Toast 来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);

			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中 *
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = getTraceInfo(ex);
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();

		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory()
						+ "/gtcrash/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}

			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}

		return null;
	}

	/**
	 * 获取错误报告文件名
	 * 
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		String path = Environment.getExternalStorageDirectory() + "/xyagentcrash/";

		File filesDir = new File(path);

		// list(FilenameFilter filter)
		// 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中满足指定过滤器的文件和目录
		return filesDir.list();
	}

	private void postReport(final File file) {
		// TODO 使用HTTP Post 发送错误报告到服务器
		// 这里不再详述,开发者可以根据OPhoneSDN上的其他网络操作
		// 教程来提交错误报告
		new Thread(){
			public void run() {
				upload(file);
			}
		}.start();
	}
	
    public void upload(File targetFile){  
        try {  
            String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线  
            URL url = new URL(Constant.UPLOADERRORLOG_SERVER);  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            conn.setRequestMethod("POST");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("Charsert", "UTF-8");   
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  
              
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线  
            
            String fname = targetFile.getAbsolutePath();
            File file = new File(fname);  
            StringBuilder sb = new StringBuilder();    
            sb.append("--");    
            sb.append(BOUNDARY);    
            sb.append("\r\n");    
            sb.append("Content-Disposition: form-data;name=\"myfiles\";filename=\""+ file.getName() + "\"\r\n");    
            sb.append("Content-Type:application/octet-stream\r\n\r\n");    
              
            byte[] data = sb.toString().getBytes();  
            out.write(data);  
            DataInputStream in = new DataInputStream(new FileInputStream(file));  
            int bytes = 0;  
            byte[] bufferOut = new byte[1024];  
            while ((bytes = in.read(bufferOut)) != -1) {  
                out.write(bufferOut, 0, bytes);  
            }  
            out.write("\r\n".getBytes()); //多个文件时，二个文件之间加入这个  
            in.close(); 
            
            out.write(end_data);  
            out.flush();    
            out.close();   
              
            // 定义BufferedReader输入流来读取URL的响应  
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                System.out.println(line);  
            }  
            boolean isSuccess = targetFile.delete();
            System.out.println(isSuccess?"错误日志删除成功":"错误日志删除失败");
        } catch (Exception e) {  
            System.out.println("发送POST请求出现异常！" + e);  
            e.printStackTrace();  
        }  
    }  

	/**
	 * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
	 */
	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(mContext);
	}

	/**
	 * 把错误报告发送给服务器,包含新产生的和以前没发送的.
	 * 
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx) {
		String[] crFiles = getCrashReportFiles(ctx);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				File cr = new File(Environment.getExternalStorageDirectory() + "/lyagentcrash/", fileName);
				postReport(cr);
//				cr.delete();// 删除已发送的报告
			}
			
		}
	}

	/**
	 * 整理异常信息
	 * 
	 * @param e
	 * @return
	 */
	public static StringBuffer getTraceInfo(Throwable e) {
		StringBuffer sb = new StringBuffer();

		sb.append(infos.toString());

		Throwable ex = e.getCause() == null ? e : e.getCause();
		StackTraceElement[] stacks = ex.getStackTrace();
		for (int i = 0; i < stacks.length; i++) {
			if (i == 0) {
				setError(ex.toString());
			}
			sb.append("class: ").append(stacks[i].getClassName())
					.append("; method: ").append(stacks[i].getMethodName())
					.append("; line: ").append(stacks[i].getLineNumber())
					.append(";  Exception: ").append(ex.toString() + "\n");
		}
		Log.d(TAG, sb.toString());
		return sb;
	}

	/**
	 * 设置错误的提示语
	 * 
	 * @param e
	 */
	public static void setError(String e) {
		Pattern pattern;
		Matcher matcher;
		for (Entry<String, String> m : regexMap.entrySet()) {
			Log.d(TAG, e + "key:" + m.getKey() + "; value:" + m.getValue());
			pattern = Pattern.compile(m.getKey());
			matcher = pattern.matcher(e);
			if (matcher.matches()) {
				error = m.getValue();
				break;
			}
		}
	}

	/**
	 * 初始化错误的提示语
	 */
	private static void initMap() {
		regexMap.put(".*NullPointerException.*", "哎呀，程序出错啦~");
		regexMap.put(".*ClassNotFoundException.*", "哎呀，程序出错啦~");
		regexMap.put(".*ArithmeticException.*", "哎呀，程序出错啦~");
		regexMap.put(".*ArrayIndexOutOfBoundsException.*", "哎呀，程序出错啦~");
		regexMap.put(".*IllegalArgumentException.*", "哎呀，程序出错啦~");
		regexMap.put(".*IllegalAccessException.*", "哎呀，程序出错啦~");
		regexMap.put(".*SecturityException.*", "哎呀，程序出错啦~");
		regexMap.put(".*NumberFormatException.*", "哎呀，程序出错啦~");
		regexMap.put(".*OutOfMemoryError.*", "哎呀，程序出错啦~");
		regexMap.put(".*StackOverflowError.*", "哎呀，程序出错啦~");
		regexMap.put(".*RuntimeException.*", "哎呀，程序出错啦~");

	}

}