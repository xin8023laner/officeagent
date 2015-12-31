package com.common.model;

public class PickUpBySelf {

	private Long pickUpBySelfId;//自提点id
	private Long agentId;//代办点id
	private String pickUpBySelfName;//自提点名称
	private String pickUpBySelfAddress;//自提点地址
	private Integer status;//自提点状态 1-可用 2-不可用
	public Long getPickUpBySelfId() {
		return pickUpBySelfId;
	}
	public void setPickUpBySelfId(Long pickUpBySelfId) {
		this.pickUpBySelfId = pickUpBySelfId;
	}
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	public String getPickUpBySelfName() {
		return pickUpBySelfName;
	}
	public void setPickUpBySelfName(String pickUpBySelfName) {
		this.pickUpBySelfName = pickUpBySelfName;
	}
	public String getPickUpBySelfAddress() {
		return pickUpBySelfAddress;
	}
	public void setPickUpBySelfAddress(String pickUpBySelfAddress) {
		this.pickUpBySelfAddress = pickUpBySelfAddress;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
