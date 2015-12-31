package com.common.model;

import java.io.Serializable;

public class AppLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long time;//记录时间
	private String content;//日志内容
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
