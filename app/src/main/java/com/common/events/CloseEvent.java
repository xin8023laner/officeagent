package com.common.events;


/**
 * Created by zhangruntao on 15/5/19.
 */
public class CloseEvent {

	private String msg;

	public CloseEvent(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


}
