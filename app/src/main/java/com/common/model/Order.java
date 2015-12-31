package com.common.model;

import java.io.Serializable;

public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID[主键值]
	 * 
	 * @mbggenerated
	 */
	private Long orderId;

	/**
	 * 订单业务状态[1-待派送 2-配送中 3-已完成 ]
	 * 
	 * @mbggenerated
	 */
	private Integer serviceStatus;

	/**
	 * 电商名称
	 * 
	 * @mbggenerated
	 */
	private String orgName;

	/**
	 * 订单编号
	 * 
	 * @mbggenerated
	 */
	private String orderCode;

	/**
	 * 商品名称
	 * 
	 * @mbggenerated
	 */
	private String goodsName;

	/**
	 * 商品数量
	 * 
	 * @mbggenerated
	 */
	private Integer goodsCount;

	/**
	 * 订单价格
	 * 
	 * @mbggenerated
	 */
	private String orderPrice;

	/**
	 * 优惠价格
	 * 
	 * @mbggenerated
	 */
	private String preferentialPrice;

	/**
	 * 购买用户名
	 * 
	 * @mbggenerated
	 */
	private String receiverName;

	/**
	 * 收货人电话
	 * 
	 * @mbggenerated
	 */
	private String receierPhone;

	/**
	 * 收货人地址
	 * 
	 * @mbggenerated
	 */
	private String receiverAddress;

	/**
	 * 付款状态[1-已付 2-未付]
	 * 
	 * @mbggenerated
	 */
	private Integer payStatus;

	/**
	 * 创建时间
	 * 
	 * @mbggenerated
	 */
	private Long createTime;

	/**
	 * 代办点ID
	 */
	private Long agentId;

	/**
	 * 状态
	 */
	private String sendType;

	/**
	 * 验证码
	 */
	private String checkCode;

	/**
	 * 是否预售 1-否 2-是
	 */
	private Integer isPresell;

	public Order() {
		// TODO Auto-generated constructor stub
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(Integer serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getPreferentialPrice() {
		return preferentialPrice;
	}

	public void setPreferentialPrice(String preferentialPrice) {
		this.preferentialPrice = preferentialPrice;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceierPhone() {
		return receierPhone;
	}

	public void setReceierPhone(String receierPhone) {
		this.receierPhone = receierPhone;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public Integer getIsPresell() {
		return isPresell;
	}

	public void setIsPresell(Integer isPresell) {
		this.isPresell = isPresell;
	}

}