package com.common.model;

import java.io.Serializable;

/**
 * 消息数据实体类
 * 
 * @author uimagine
 */
public class MsMessage implements Serializable {

	/**
	 * default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// Fields

	private int result;// 操作结果[0-成功 1-失败......]
	private Object data;// 实体类
	private String message;// 消息内容

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MsMessage [result=" + result + ", data=" + data + ", message="
				+ message + "]";
	}
	
}