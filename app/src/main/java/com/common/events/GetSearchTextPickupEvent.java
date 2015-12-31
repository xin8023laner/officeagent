package com.common.events;

import java.util.List;

import android.widget.ListView;

/**
 * Created by zhangruntao on 15/5/19.
 */
public class GetSearchTextPickupEvent {

    private String result; //单个扫描结果


    private ListView lv;


    public GetSearchTextPickupEvent(String result) {
    	super();
    	this.result = result;
    }
	public GetSearchTextPickupEvent(String result, ListView lv) {
		super();
		this.result = result;
		this.lv = lv;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public ListView getLv() {
		return lv;
	}


	public void setLv(ListView lv) {
		this.lv = lv;
	}


	
}
