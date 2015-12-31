package com.common.model;

import java.io.Serializable;

public class TaskWaybill implements Serializable {

	/**
	 * 私有或共有属性的注释。
	 */
	private static final long serialVersionUID = 1L;

	private Integer status;
	private String waybillCode;// 贯通系统运单号
	private String expressCode;// 快递单号
	private String senderName;// 发件人名称
	private String senderTelphone;// 发件人手机号码
	private String senderPhone;// 发件人固定电话
	private String senderProvinceName;// 发件地址省名称
	private String senderCityName;// 发件地址市名称
	private String senderDistrictName;// 发件地址区名称
	private String senderAddress;// 发件地址详细信息
	private String receiverName;// 收件人名称
	private String receiverTelphone;// 收件人手机号码
	private String receiverProvinceName;// 收件地址省名称
	private String receiverCityName;// 收件地址市名称
	private String receiverDistrictName;// 收件地址区名称
	private String receiverAddress;// 收件地址详细信息
	private String weight;// 快件重量
	private String remark;// 快件备注
	private Long taskId;// 任务主键值
	private Long agentUserId;// 操作人Id
	private String agentUserCode;// 操作人Code
	private Long sendTime;// 发件时间
	private String appointmentTime;// 预约取件时间[时间段]
	private String carrierName;// 快递公司名称
	private Long carrierId;// 快递公司id
	private double freight;// 运费
	private String commodityName;
	private String gtLabel;// 贯通小标签

	public TaskWaybill() {
		// TODO Auto-generated constructor stub
	}

	public String getWaybillCode() {
		return waybillCode;
	}

	public void setWaybillCode(String waybillCode) {
		this.waybillCode = waybillCode;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderTelphone() {
		return senderTelphone;
	}

	public void setSenderTelphone(String senderTelphone) {
		this.senderTelphone = senderTelphone;
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getSenderProvinceName() {
		return senderProvinceName;
	}

	public void setSenderProvinceName(String senderProvinceName) {
		this.senderProvinceName = senderProvinceName;
	}

	public String getSenderCityName() {
		return senderCityName;
	}

	public void setSenderCityName(String senderCityName) {
		this.senderCityName = senderCityName;
	}

	public String getSenderDistrictName() {
		return senderDistrictName;
	}

	public void setSenderDistrictName(String senderDistrictName) {
		this.senderDistrictName = senderDistrictName;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverTelphone() {
		return receiverTelphone;
	}

	public void setReceiverTelphone(String receiverTelphone) {
		this.receiverTelphone = receiverTelphone;
	}

	public String getReceiverProvinceName() {
		return receiverProvinceName;
	}

	public void setReceiverProvinceName(String receiverProvinceName) {
		this.receiverProvinceName = receiverProvinceName;
	}

	public String getReceiverCityName() {
		return receiverCityName;
	}

	public void setReceiverCityName(String receiverCityName) {
		this.receiverCityName = receiverCityName;
	}

	public String getReceiverDistrictName() {
		return receiverDistrictName;
	}

	public void setReceiverDistrictName(String receiverDistrictName) {
		this.receiverDistrictName = receiverDistrictName;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public String getAgentUserCode() {
		return agentUserCode;
	}

	public void setAgentUserCode(String agentUserCode) {
		this.agentUserCode = agentUserCode;
	}

	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public double getFreight() {
		return freight;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getGtLabel() {
		return gtLabel;
	}

	public void setGtLabel(String gtLabel) {
		this.gtLabel = gtLabel;
	}

	public Long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}

}
