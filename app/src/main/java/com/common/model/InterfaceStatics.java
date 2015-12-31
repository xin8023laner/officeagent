package com.common.model;

import java.io.Serializable;

//统计接口查询调用的数据
public class InterfaceStatics implements Serializable {

	private static final long serialVersionUID = 1L;

	public Integer out;// 派送 总件数

	public Integer total;// 揽件总件数

	public Integer send;// 今日派送 总件数

	public Integer lanjian;// 今日揽件总件数

	public Integer noticeLeft;// 今日未妥投总件数

	public Integer getSend() {
		return send;
	}

	public void setSend(Integer send) {
		this.send = send;
	}

	public Integer getLanjian() {
		return lanjian;
	}

	public void setLanjian(Integer lanjian) {
		this.lanjian = lanjian;
	}

	public Integer getNoticeLeft() {
		return noticeLeft;
	}

	public void setNoticeLeft(Integer noticeLeft) {
		this.noticeLeft = noticeLeft;
	}

	public Integer getOut() {
		return out;
	}

	public void setOut(Integer out) {
		this.out = out;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
