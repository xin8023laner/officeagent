package com.common.events;


/**
 * Created by zhangruntao on 15/5/19.
 */
public class ChangeMainTabByTagEvent {

	private String tag; // 单个扫描结果

	public ChangeMainTabByTagEvent(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
