package com.common.model;

import java.io.Serializable;
/**
 * 软件更新实体类
 * @author WangHuiYong
 *
 */
public class Software implements Serializable {
	/**
	 * default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	// Fields
	private Long versionId;//主键值
	private Integer versionType;//版本类型[1-Android 2-IOS]
	private double versionNumber;//版本号
	private String versionName;//版本名字
	private Long createTime;//创建时间
	private Long updateTime;//更新时间
	

	// Empty Constructor
	public Software() {
		super();
	}
	
	// Full Constructor
	public Software(Long versionId, Integer versionType, double versionNumber,
			String versionName, Long createTime, Long updateTime) {
		super();
		this.versionId = versionId;
		this.versionType = versionType;
		this.versionNumber = versionNumber;
		this.versionName = versionName;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	
	// Property accessors
	public Long getVersionId() {
		return versionId;
	}
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}
	public Integer getVersionType() {
		return versionType;
	}
	public void setVersionType(Integer versionType) {
		this.versionType = versionType;
	}
	public double getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(double versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
