package com.common.model;

import java.io.Serializable;

/**
 * 消息数据实体类
 * 
 * @author uimagine
 */
public class Msg implements Serializable {

	/**
	 * default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// Fields

	private long time;// 操作结果[0-成功 1-失败......]
	private String msg;// 消息内容
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	

}