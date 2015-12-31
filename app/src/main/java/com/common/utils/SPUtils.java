package com.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.common.model.Agent;
import com.common.model.AgentCarrier;
import com.common.model.AgentReceiveTask;
import com.common.model.AgentUser;
import com.common.model.InterfaceStatics;

public class SPUtils {
	private SPUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * 
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void put(Context context, String key, Object object) {

		SharedPreferences sp = context.getSharedPreferences(
				Constant.SPKeys.MAIN_SHAREDPREF.name(), Context.MODE_PRIVATE
						+ Context.MODE_APPEND);
		SharedPreferences.Editor editor = sp.edit();

		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultObject) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.SPKeys.MAIN_SHAREDPREF.name(), Context.MODE_PRIVATE
						+ Context.MODE_APPEND);

		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.SPKeys.MAIN_SHAREDPREF.name(), Context.MODE_PRIVATE
						+ Context.MODE_APPEND);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.SPKeys.MAIN_SHAREDPREF.name(), Context.MODE_PRIVATE
						+ Context.MODE_APPEND);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.SPKeys.MAIN_SHAREDPREF.name(), Context.MODE_PRIVATE
						+ Context.MODE_APPEND);
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, ?> getAll(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.SPKeys.MAIN_SHAREDPREF.name(), Context.MODE_PRIVATE
						+ Context.MODE_APPEND);
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author zhy
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}

	private final static String CONFIGNAME = "config";

	public static AgentUser getUser(Context context) {

		AgentUser webUser = null;
		SharedPreferences gtConfig = context.getSharedPreferences(CONFIGNAME,
				Activity.MODE_PRIVATE);
		try {
			String userString = gtConfig.getString("user", "");
			byte[] userBytes = Base64.decode(userString.getBytes(),
					Base64.DEFAULT);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					userBytes);
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);

			webUser = (AgentUser) objectInputStream.readObject();
			objectInputStream.close();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return webUser;
	}

	public static void setUser(Context context, AgentUser user) {
		SharedPreferences gtConfig = context.getSharedPreferences(CONFIGNAME,
				Activity.MODE_PRIVATE);
		try {
			SharedPreferences.Editor editor = gtConfig.edit();

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					byteArrayOutputStream);
			objectOutputStream.writeObject(user);
			String userString = new String(Base64.encode(
					byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
			editor.putString("user", userString);
			editor.commit();
			objectOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Agent getAgent(Context context) {

		Agent agent = null;
		SharedPreferences gtConfig = context.getSharedPreferences(CONFIGNAME,
				Activity.MODE_PRIVATE);
		try {
			String userString = gtConfig.getString("agent", "");
			byte[] userBytes = Base64.decode(userString.getBytes(),
					Base64.DEFAULT);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					userBytes);
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);

			agent = (Agent) objectInputStream.readObject();
			objectInputStream.close();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return agent;
	}

	public static void setAgent(Context context, Agent agent) {
		SharedPreferences gtConfig = context.getSharedPreferences(CONFIGNAME,
				Activity.MODE_PRIVATE);
		try {
			SharedPreferences.Editor editor = gtConfig.edit();

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					byteArrayOutputStream);
			objectOutputStream.writeObject(agent);
			String userString = new String(Base64.encode(
					byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
			editor.putString("agent", userString);
			editor.commit();
			objectOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static InterfaceStatics getInterfaceStatics(Context context) {

		InterfaceStatics interfaceStatics = null;
		SharedPreferences gtConfig = context.getSharedPreferences(CONFIGNAME,
				Activity.MODE_PRIVATE);
		try {
			String userString = gtConfig.getString("interfaceStatics", "");
			byte[] userBytes = Base64.decode(userString.getBytes(),
					Base64.DEFAULT);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					userBytes);
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);

			interfaceStatics = (InterfaceStatics) objectInputStream
					.readObject();
			objectInputStream.close();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return interfaceStatics;
	}

	public static void setInterfaceStatics(Context context,
			InterfaceStatics interfaceStatics) {
		SharedPreferences gtConfig = context.getSharedPreferences(CONFIGNAME,
				Activity.MODE_PRIVATE);
		try {
			SharedPreferences.Editor editor = gtConfig.edit();

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					byteArrayOutputStream);
			objectOutputStream.writeObject(interfaceStatics);
			String userString = new String(Base64.encode(
					byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
			editor.putString("interfaceStatics", userString);
			editor.commit();
			objectOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static List<AgentCarrier> getAgentCarrierList(Context context) {
		List<AgentCarrier> list=null;
		SharedPreferences gtConfig = context.getSharedPreferences(Constant.SPKeys.MAIN_SHAREDPREF.name(),
				Activity.MODE_PRIVATE);
		String str = gtConfig.getString("agentCarrier", "");
		try {
			list=StringListUtils.String2SceneList(str);
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public static void setAgentCarrierList(Context context, List<AgentCarrier> list) {
		SharedPreferences gtConfig = context.getSharedPreferences(Constant.SPKeys.MAIN_SHAREDPREF.name(),
				Activity.MODE_PRIVATE);
		try {
			SharedPreferences.Editor editor = gtConfig.edit();

			String str_list = StringListUtils.SceneList2String(list);
			editor.putString("agentCarrier", str_list);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}