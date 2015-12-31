package com.common.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 本地端的消息推送实体类
 * @author zhangruntao
 *
 */
@Table(name="PushModel")
public class PushModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id(column="id")
	private int id;
	
	@Column(column="type")
	private int type; //推送消息类型
	@Column(column="content")
	private String content; //推送消息内容
	@Column(column="extra")
	private String extra;//额外拓展内容
	@Column(column="date")
	private long date; //推送时间
	@Column(column="isRead")
	private boolean isRead; //是否阅读过
	@Column(column="senderId")
	private int senderId; //发送人Id
	@Column(column="waybillId")
	private long waybillId;//物流id
	
	
	public static final int TYPE_ACTIVE = 1;//活动
	public static final int TYPE_LOGISTIC = 2;//物流
	public static final int TYPE_CHAT = 3;//聊天
	
	
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public long getWaybillId() {
		return waybillId;
	}
	public void setWaybillId(long waybillId) {
		this.waybillId = waybillId;
	}

}
