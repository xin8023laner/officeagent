package com.common.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class ViewUtils {

	private View rootView;
    private OnClickListener	onClickListener;
    //以viewId作为key缓存View
    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public ViewUtils(View view) {
        this.rootView = view;
        
       
    }
    public ViewUtils(Context context ,int	layoutId) {
    	rootView= LayoutInflater.from(context).inflate(layoutId,
				null);
    	
    }
    public ViewUtils(Context context ,int	layoutId,OnClickListener	onClickListener) {
    	rootView= LayoutInflater.from(context).inflate(layoutId,
    			null);
    	this.onClickListener = onClickListener;
    }
 
    public ViewUtils(View view,OnClickListener	onClickListener) {
    	this.rootView = view;
    	this.onClickListener = onClickListener;
    }

    public void   bindOnClickListener(OnClickListener	onClickListener) {
    	this.onClickListener = onClickListener;
    }
    public View   getRootView() {
		return rootView;
    	
    }
    public void   resetRootView(View view) {
    	viewMap.clear();
    	this.rootView = view;
    	
    }
    public void   clearViews() {
    	viewMap.clear();
    }
    
    
    public <T> T view(int id, Class<T> clazz) {
    	
    	if (viewMap.get(id) == null) {

			viewMap.put(id, rootView.findViewById(id));
		}
        return (T) viewMap.get(id);
    }
    
	public View view(int resid) {
		if (viewMap.get(resid) == null) {

			viewMap.put(resid, rootView.findViewById(resid));
		}
		return viewMap.get(resid);

	}

    public void setViewText(int id, String text) {
        view(id, TextView.class).setText(text);
      
    }
    
    public void setViewTextColor(int id,int color){
    		view(id, TextView.class).setTextColor(color);
    		
    }

    public String getViewText(int id) {
        return view(id, TextView.class).getText().toString();
    }

    public String[] getViewText(int... id) {
        String[] str = null;
        if (id.length > 0) {
            str = new String[id.length];
            for (int i = 0; i < id.length; i++) {
                str[i] = getViewText(id[i]);
            }
        }
        return str;
    }

    public void setOnClickListener(int id) {
    	if(null==onClickListener){
    		Log.e("ViewUtis","请调用bindOnClickListener去绑定onClickListener");
    		return;
    	}
        view(id, View.class).setOnClickListener(onClickListener);
    }

    public void setOnClickListener(int... id) {
        if (id.length > 0) {
            for (int i = 0; i < id.length; i++) {
                setOnClickListener(id[i]);
            }
        }
    }

    public void setOnItemClickListener(int id, AdapterView.OnItemClickListener onItemClickListener) {
        view(id, ListView.class).setOnItemClickListener(onItemClickListener);
    }

    public void setBackgroundResource(int id, int resId) {
        view(id, View.class).setBackgroundResource(resId);
    }

    public void setBackgroundDrawable(int id, Drawable draw) {
        view(id, View.class).setBackgroundDrawable(draw);
    }

    public void setBackgroundColor(int id, int colorId) {
        view(id, View.class).setBackgroundColor(colorId);
    }

    public void setImageResource(int id, int resId) {
        view(id, ImageView.class).setImageResource(resId);
    }

    public void setImageDrawable(int id, Drawable drawable) {
        view(id, ImageView.class).setImageDrawable(drawable);
    }

    public void setImageBitmap(int id, Bitmap bitmap) {
        view(id, ImageView.class).setImageBitmap(bitmap);
    }

    public void setGone(int id) {
        view(id, View.class).setVisibility(View.GONE);
    }

    public void setGone(int... id) {
        if (id.length > 0) {
            for (int i = 0; i < id.length; i++) {
                setGone(id[i]);
            }
        }
    }

    public void setVisiable(int id) {
        view(id, View.class).setVisibility(View.VISIBLE);
    }

    public void setVisiable(int... id) {
        if (id.length > 0) {
            for (int i = 0; i < id.length; i++) {
                setVisiable(id[i]);
            }
        }
    }

    public void setInVisiable(int id) {
        view(id, View.class).setVisibility(View.INVISIBLE);
    }

    public void setInVisiable(int... id) {
        if (id.length > 0) {
            for (int i = 0; i < id.length; i++) {
                setInVisiable(id[i]);
            }
        }
    }

    public void setEnable(int id, boolean enable) {
        view(id, View.class).setEnabled(enable);
    }

    public void setEnable(boolean enable, int... id) {
        if (id.length > 0) {
            for (int i = 0; i < id.length; i++) {
                setEnable(id[i], enable);
            }
        }
    }

    public void setFocusable(int id, boolean focusable) {
        view(id, View.class).setFocusable(focusable);
    }

    public void setFocusableInTouchMode(int id, boolean focusable) {
        view(id, View.class).setFocusableInTouchMode(focusable);
    }

    public void setClickable(int id, boolean clickable) {
        view(id, View.class).setClickable(clickable);
    }
}
