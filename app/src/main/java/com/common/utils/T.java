package com.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 *
 * @author way
 *
 */
public class T {
    // Toast
    private static Toast toast;


    public static void showShort(Context context,int messageRes){
        showShort(context, context.getString(messageRes));
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (context==null) {
            return;
        }
        if (null == toast) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }


    public static void showLong(Context context,int  messageRes){
        showLong(context,context.getString(messageRes));
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }


    public static void show(Context context,int messageRes, int duration){
        show(context, context.getString(messageRes), duration);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (null == toast) {
            toast = Toast.makeText(context, message, duration);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /** Hide the toast, if any. */
    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }
}
