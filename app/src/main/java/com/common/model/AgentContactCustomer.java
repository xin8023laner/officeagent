package com.common.model;

import java.io.Serializable;

/**
 * APP联系客户实体类
 * @author zhangcan
 *
 */
public class AgentContactCustomer implements Serializable{

	/**
	 * default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private Long customerId;//主键id
	private Long agentUserId;//带派员
	private String receiverName;//收件人姓名
	private String receiverPhone;//收件人电话
	private String expressCode;//快递单号
	private String carrierName;//快递公司名称
	private Long carrierId;//快递公司id
	private Integer isFinsh;//是否已完成
	private Long contactTime;//联系时间
	private Long agentId;
	private Long taskId;
	private String statusDesc;// 拒收原因
	private Integer sendType;//派送方式  1-派送    2-自提   3-前台
	private String receiverAddress;// 收件人详细地址
	
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Long getContactTime() {
		return contactTime;
	}
	public void setContactTime(Long contactTime) {
		this.contactTime = contactTime;
	}
	public Integer getIsFinsh() {
		return isFinsh;
	}
	public void setIsFinsh(Integer isFinsh) {
		this.isFinsh = isFinsh;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getAgentUserId() {
		return agentUserId;
	}
	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getExpressCode() {
		return expressCode;
	}
	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	public String getCarrierName() {
		return carrierName;
	}
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	public Long getCarrierId() {
		return carrierId;
	}
	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}
	
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	public AgentContactCustomer(Long customerId, Long agentUserId,
			String receiverName, String receiverPhone, String expressCode,
			String carrierName, Long carrierId,Integer isFinsh,Long contactTime,Long agentId) {
		super();
		this.customerId = customerId;
		this.agentUserId = agentUserId;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.expressCode = expressCode;
		this.carrierName = carrierName;
		this.carrierId = carrierId;
		this.isFinsh = isFinsh;
		this.contactTime = contactTime;
		this.agentId=agentId;
	}
	public AgentContactCustomer(){
		super();
	}
	
}
