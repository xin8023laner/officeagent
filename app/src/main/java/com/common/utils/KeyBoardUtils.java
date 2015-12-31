package com.common.utils;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

//打开或关闭软键盘
public class KeyBoardUtils {

    private KeyBoardUtils() {
    }

    ;

    /**
     * 打开软键盘
     *
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
    /**
     * 关闭软键盘，显示光标
     */
    public static void closeKeybordShowCursor(Activity mContext,EditText... mEditText) {
    	mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	
    	try {  
            Class<EditText> cls = EditText.class;  
           Method setShowSoftInputOnFocus;  
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",  
                    boolean.class);  
            setShowSoftInputOnFocus.setAccessible(true);  
            if(mEditText.length>0){
            	for(EditText editText:mEditText){
            		setShowSoftInputOnFocus.invoke(editText, false);  
            	}
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }   
    }
}
