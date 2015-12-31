package com.gt.inteface;

import android.view.View;

public interface OnViewClickListener {
	public void OnViewClicked(View v,int type, Object obj);
	public void doSomeThing(View v, int type, Object obj);
}