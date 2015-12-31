package com.common.model;

import java.io.Serializable;

/**
 * 代办点运单实体类
 * 
 * @author CaoXuYang
 */
public class AgentWaybill implements Serializable {

	/**
	 * 私有或共有属性的注释。
	 */
	private static final long serialVersionUID = 1L;

	// Fields
	private Long waybillId;// 主键值
	private Long AgentId;// 代办点Id
	private String waybillCode;// 贯通系统运单号
	private Integer waybillType;// 运单类型[1-快递公司接口运单 2-电子运单 3-用户查询运单 4-贯通自定义流程运单]
	private Integer waybillState;// 运单状态 [0-已提交 4-在途 6-已完成]
	private String expressCode;// 快递单号
	private String waybillStateDesc;// 运单状态描述
	private String source;// 发件来源[web网站-gtweb Android手机APP(登录前)-Android_Offline
							// Android手机APP(登录后)-Android_Online
							// IOS手机APP(登录前)-IOS_Offline
							// IOS手机APP(登录后)-IOS_Online];
	private Long senderId;// 发件人ID
	private String senderName;// 发件人名称
	private Long senderAddressId;// 发件人地址ID
	private String senderTelphone;// 发件人手机号码
	private String senderPhone;// 发件人固定电话
	private String senderProvinceName;// 发件地址省名称
	private String senderCityName;// 发件地址市名称
	private String senderDistrictName;// 发件地址区名称
	private String senderProvinceId;// 发件地址省ID
	private String senderCityId;// 发件地址市ID
	private String senderDistrictId;// 发件地址区ID
	private String senderAddress;// 发件地址详细信息
	private String senderOrgName;// 发件机构名称
	private String senderPostCode;// 发件地址邮政编码
	private Long receiverId;// 收件人ID
	private String receiverName;// 收件人名称
	private Long receiverAddressId;// 收件人地址ID
	private String receiverTelphone;// 收件人手机号码
	private String receiverPhone;// 收件人固定电话
	private String receiverProvinceName;// 收件地址省名称
	private String receiverCityName;// 收件地址市名称
	private String receiverDistrictName;// 收件地址区名称
	private String receiverProvinceId;// 收件地址省ID
	private String receiverCityId;// 收件地址市ID
	private String receiverDistrictId;// 收件地址区ID
	private String receiverAddress;// 收件地址详细信息
	private String receiverOrgName;// 收件机构名称
	private String receiverPostCode;// 收件地址邮政编码
	private Long carrierId;// 承运商id
	private String carrierCode;// 承运商编号
	private String carrierName;// 承运商名称
	private String carrierPhone;// 承运商电话
	private String waybillPicName;// 图片名称
	private Long orderTime;// 下单时间
	private Long updateTime;// 修改时间
	private String weight;// 快件重量
	private String appointmentTime;// 预约取件时间[时间段]
	private String remark;// 快件备注
	private Integer expressType;// 快件类型[1-函件 2-包裹]
	private Integer expressRequirement;// 寄递要求[1-普通 2-经济 3-加急]
	private double insuranceAmount;// 保险金额[根据不同快递公司决定]
	private double insuranceCost;// 保险费[根据不同快递公司决定]
	private double insureAmount;// 保价金额[根据不同快递公司决定]
	private double insureCost;// 保价费[根据不同快递公司决定]
	private String expressSpecifications;// 快件规格[根据不同快递公司决定]
	private String expressSize;// 快件尺寸[根据不同快递公司决定]
	private Integer payType;// 支付方式[1-现付 2-到付 3-协议 根据不同快递公司决定]
	private String protocolNumber;// 快递公司协议单号
	private double freight;// 运费
	private double totalFreight;// 总运费
	private double packageCost;// 包装费[根据不同快递公司决定]
	private double urgentCost;// 加急费[根据不同快递公司决定]
	private Integer isNeedPackage;// 是否需要包装[1-不需要 2-需要]
	private Integer urgentType;// 加急类型[根据不同快递公司决定]
	private String senderInsureCode;// 取件确认码
	private String receiverInsureCode;// 投递确认码
	private Integer isSenderDelete;// 发件人是否已删除[1-未删除 2-已删除]
	private Integer isReceiverDelete;// 收件人是否已删除[1-未删除 2-已删除]
	private Integer isSenderEvaluate;// 发件人是否已评价[1-未评价 2-已评价]
	private Integer isReceiverEvaluate;// 收件人是否已评价[1-未评价 2-已评价]
	private Integer isPrintLabel;// 客户是否打印标签[1-否 2-是]
	private String takerTelphone;// 取件员手机号码
	private String takerName;// 取件员姓名
	private String takerNumber;// 取件员工号
	private String deliverTelphone;// 投递员手机号码
	private String deliverName;// 投递员姓名
	private String deliverNumber;// 投递员工号
	private Integer isNoair;// 是否禁航
	private Long queryUserId;// 查询用户ID
	private Long queryTime;// 查询时间
	private Integer isScore;// 是否赠送积分1:赠送，2:未赠送
	private String expressOrderCode; // 快递公司订单号 为未合作快递公司发件时所用
	private String gtInfoDesc; // 贯通物流信息描述

	// Empty Constructor
	public AgentWaybill() {
		super();
	}

	// Full Constructor
	public AgentWaybill(Long waybillId, Long AgentId, String waybillCode,
			Integer waybillType, Integer waybillState, String expressCode,
			String waybillStateDesc, String source, Long senderId,
			String senderName, Long senderAddressId, String senderTelphone,
			String senderPhone, String senderProvinceName,
			String senderCityName, String senderDistrictName,
			String senderProvinceId, String senderCityId,
			String senderDistrictId, String senderAddress,
			String senderOrgName, String senderPostCode, Long receiverId,
			String receiverName, Long receiverAddressId,
			String receiverTelphone, String receiverPhone,
			String receiverProvinceName, String receiverCityName,
			String receiverDistrictName, String receiverProvinceId,
			String receiverCityId, String receiverDistrictId,
			String receiverAddress, String receiverOrgName,
			String receiverPostCode, Long carrierId, String carrierCode,
			String carrierName, String carrierPhone, String waybillPicName,
			Long orderTime, Long updateTime, String weight,
			String appointmentTime, String remark, Integer expressType,
			Integer expressRequirement, double insuranceAmount,
			double insuranceCost, double insureAmount, double insureCost,
			String expressSpecifications, String expressSize, Integer payType,
			String protocolNumber, double freight, double totalFreight,
			double packageCost, double urgentCost, Integer isNeedPackage,
			Integer urgentType, String senderInsureCode,
			String receiverInsureCode, Integer isSenderDelete,
			Integer isReceiverDelete, Integer isSenderEvaluate,
			Integer isReceiverEvaluate, Integer isPrintLabel,
			String takerTelphone, String takerName, String takerNumber,
			String deliverTelphone, String deliverName, String deliverNumber,
			Integer isNoair, Long queryUserId, Long queryTime, Integer isScore,
			String expressOrderCode, String gtInfoDesc) {
		super();
		this.waybillId = waybillId;
		this.AgentId = AgentId;
		this.waybillCode = waybillCode;
		this.waybillType = waybillType;
		this.waybillState = waybillState;
		this.expressCode = expressCode;
		this.waybillStateDesc = waybillStateDesc;
		this.source = source;
		this.senderId = senderId;
		this.senderName = senderName;
		this.senderAddressId = senderAddressId;
		this.senderTelphone = senderTelphone;
		this.senderPhone = senderPhone;
		this.senderProvinceName = senderProvinceName;
		this.senderCityName = senderCityName;
		this.senderDistrictName = senderDistrictName;
		this.senderProvinceId = senderProvinceId;
		this.senderCityId = senderCityId;
		this.senderDistrictId = senderDistrictId;
		this.senderAddress = senderAddress;
		this.senderOrgName = senderOrgName;
		this.senderPostCode = senderPostCode;
		this.receiverId = receiverId;
		this.receiverName = receiverName;
		this.receiverAddressId = receiverAddressId;
		this.receiverTelphone = receiverTelphone;
		this.receiverPhone = receiverPhone;
		this.receiverProvinceName = receiverProvinceName;
		this.receiverCityName = receiverCityName;
		this.receiverDistrictName = receiverDistrictName;
		this.receiverProvinceId = receiverProvinceId;
		this.receiverCityId = receiverCityId;
		this.receiverDistrictId = receiverDistrictId;
		this.receiverAddress = receiverAddress;
		this.receiverOrgName = receiverOrgName;
		this.receiverPostCode = receiverPostCode;
		this.carrierId = carrierId;
		this.carrierCode = carrierCode;
		this.carrierName = carrierName;
		this.carrierPhone = carrierPhone;
		this.waybillPicName = waybillPicName;
		this.orderTime = orderTime;
		this.updateTime = updateTime;
		this.weight = weight;
		this.appointmentTime = appointmentTime;
		this.remark = remark;
		this.expressType = expressType;
		this.expressRequirement = expressRequirement;
		this.insuranceAmount = insuranceAmount;
		this.insuranceCost = insuranceCost;
		this.insureAmount = insureAmount;
		this.insureCost = insureCost;
		this.expressSpecifications = expressSpecifications;
		this.expressSize = expressSize;
		this.payType = payType;
		this.protocolNumber = protocolNumber;
		this.freight = freight;
		this.totalFreight = totalFreight;
		this.packageCost = packageCost;
		this.urgentCost = urgentCost;
		this.isNeedPackage = isNeedPackage;
		this.urgentType = urgentType;
		this.senderInsureCode = senderInsureCode;
		this.receiverInsureCode = receiverInsureCode;
		this.isSenderDelete = isSenderDelete;
		this.isReceiverDelete = isReceiverDelete;
		this.isSenderEvaluate = isSenderEvaluate;
		this.isReceiverEvaluate = isReceiverEvaluate;
		this.isPrintLabel = isPrintLabel;
		this.takerTelphone = takerTelphone;
		this.takerName = takerName;
		this.takerNumber = takerNumber;
		this.deliverTelphone = deliverTelphone;
		this.deliverName = deliverName;
		this.deliverNumber = deliverNumber;
		this.isNoair = isNoair;
		this.queryUserId = queryUserId;
		this.queryTime = queryTime;
		this.isScore = isScore;
		this.expressOrderCode = expressOrderCode;
		this.gtInfoDesc = gtInfoDesc;
	}

	// Property accessors

	public Long getWaybillId() {
		return waybillId;
	}

	public Integer getIsScore() {
		return isScore;
	}

	public Long getAgentId() {
		return AgentId;
	}

	public void setAgentId(Long agentId) {
		AgentId = agentId;
	}

	public void setIsScore(Integer isScore) {
		this.isScore = isScore;
	}

	public void setWaybillId(Long waybillId) {
		this.waybillId = waybillId;
	}

	public String getCarrierPhone() {
		return carrierPhone;
	}

	public void setCarrierPhone(String carrierPhone) {
		this.carrierPhone = carrierPhone;
	}

	public Long getSenderAddressId() {
		return senderAddressId;
	}

	public void setSenderAddressId(Long senderAddressId) {
		this.senderAddressId = senderAddressId;
	}

	public Long getReceiverAddressId() {
		return receiverAddressId;
	}

	public void setReceiverAddressId(Long receiverAddressId) {
		this.receiverAddressId = receiverAddressId;
	}

	public String getWaybillCode() {
		return waybillCode;
	}

	public void setWaybillCode(String waybillCode) {
		this.waybillCode = waybillCode;
	}

	public Integer getWaybillType() {
		return waybillType;
	}

	public void setWaybillType(Integer waybillType) {
		this.waybillType = waybillType;
	}

	public Integer getWaybillState() {
		return waybillState;
	}

	public void setWaybillState(Integer waybillState) {
		this.waybillState = waybillState;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getWaybillStateDesc() {
		return waybillStateDesc;
	}

	public void setWaybillStateDesc(String waybillStateDesc) {
		this.waybillStateDesc = waybillStateDesc;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
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

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
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

	public String getSenderProvinceId() {
		return senderProvinceId;
	}

	public void setSenderProvinceId(String senderProvinceId) {
		this.senderProvinceId = senderProvinceId;
	}

	public String getSenderCityId() {
		return senderCityId;
	}

	public void setSenderCityId(String senderCityId) {
		this.senderCityId = senderCityId;
	}

	public String getSenderDistrictId() {
		return senderDistrictId;
	}

	public void setSenderDistrictId(String senderDistrictId) {
		this.senderDistrictId = senderDistrictId;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getSenderOrgName() {
		return senderOrgName;
	}

	public void setSenderOrgName(String senderOrgName) {
		this.senderOrgName = senderOrgName;
	}

	public String getSenderPostCode() {
		return senderPostCode;
	}

	public void setSenderPostCode(String senderPostCode) {
		this.senderPostCode = senderPostCode;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverTelphone() {
		return receiverTelphone;
	}

	public void setReceiverTelphone(String receiverTelphone) {
		this.receiverTelphone = receiverTelphone;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
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

	public String getReceiverProvinceId() {
		return receiverProvinceId;
	}

	public void setReceiverProvinceId(String receiverProvinceId) {
		this.receiverProvinceId = receiverProvinceId;
	}

	public String getReceiverCityId() {
		return receiverCityId;
	}

	public void setReceiverCityId(String receiverCityId) {
		this.receiverCityId = receiverCityId;
	}

	public String getReceiverDistrictId() {
		return receiverDistrictId;
	}

	public void setReceiverDistrictId(String receiverDistrictId) {
		this.receiverDistrictId = receiverDistrictId;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverOrgName() {
		return receiverOrgName;
	}

	public void setReceiverOrgName(String receiverOrgName) {
		this.receiverOrgName = receiverOrgName;
	}

	public String getReceiverPostCode() {
		return receiverPostCode;
	}

	public void setReceiverPostCode(String receiverPostCode) {
		this.receiverPostCode = receiverPostCode;
	}

	public Long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getWaybillPicName() {
		return waybillPicName;
	}

	public void setWaybillPicName(String waybillPicName) {
		this.waybillPicName = waybillPicName;
	}

	public Long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Long orderTime) {
		this.orderTime = orderTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getExpressType() {
		return expressType;
	}

	public void setExpressType(Integer expressType) {
		this.expressType = expressType;
	}

	public Integer getExpressRequirement() {
		return expressRequirement;
	}

	public void setExpressRequirement(Integer expressRequirement) {
		this.expressRequirement = expressRequirement;
	}

	public double getInsuranceAmount() {
		return insuranceAmount;
	}

	public void setInsuranceAmount(double insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}

	public double getInsuranceCost() {
		return insuranceCost;
	}

	public void setInsuranceCost(double insuranceCost) {
		this.insuranceCost = insuranceCost;
	}

	public double getInsureAmount() {
		return insureAmount;
	}

	public void setInsureAmount(double insureAmount) {
		this.insureAmount = insureAmount;
	}

	public double getInsureCost() {
		return insureCost;
	}

	public void setInsureCost(double insureCost) {
		this.insureCost = insureCost;
	}

	public String getExpressSpecifications() {
		return expressSpecifications;
	}

	public void setExpressSpecifications(String expressSpecifications) {
		this.expressSpecifications = expressSpecifications;
	}

	public String getExpressSize() {
		return expressSize;
	}

	public void setExpressSize(String expressSize) {
		this.expressSize = expressSize;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public double getFreight() {
		return freight;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}

	public double getTotalFreight() {
		return totalFreight;
	}

	public void setTotalFreight(double totalFreight) {
		this.totalFreight = totalFreight;
	}

	public double getPackageCost() {
		return packageCost;
	}

	public void setPackageCost(double packageCost) {
		this.packageCost = packageCost;
	}

	public double getUrgentCost() {
		return urgentCost;
	}

	public void setUrgentCost(double urgentCost) {
		this.urgentCost = urgentCost;
	}

	public Integer getIsNeedPackage() {
		return isNeedPackage;
	}

	public void setIsNeedPackage(Integer isNeedPackage) {
		this.isNeedPackage = isNeedPackage;
	}

	public Integer getUrgentType() {
		return urgentType;
	}

	public void setUrgentType(Integer urgentType) {
		this.urgentType = urgentType;
	}

	public String getSenderInsureCode() {
		return senderInsureCode;
	}

	public void setSenderInsureCode(String senderInsureCode) {
		this.senderInsureCode = senderInsureCode;
	}

	public String getReceiverInsureCode() {
		return receiverInsureCode;
	}

	public void setReceiverInsureCode(String receiverInsureCode) {
		this.receiverInsureCode = receiverInsureCode;
	}

	public Integer getIsSenderDelete() {
		return isSenderDelete;
	}

	public void setIsSenderDelete(Integer isSenderDelete) {
		this.isSenderDelete = isSenderDelete;
	}

	public Integer getIsReceiverDelete() {
		return isReceiverDelete;
	}

	public void setIsReceiverDelete(Integer isReceiverDelete) {
		this.isReceiverDelete = isReceiverDelete;
	}

	public Integer getIsSenderEvaluate() {
		return isSenderEvaluate;
	}

	public void setIsSenderEvaluate(Integer isSenderEvaluate) {
		this.isSenderEvaluate = isSenderEvaluate;
	}

	public Integer getIsReceiverEvaluate() {
		return isReceiverEvaluate;
	}

	public void setIsReceiverEvaluate(Integer isReceiverEvaluate) {
		this.isReceiverEvaluate = isReceiverEvaluate;
	}

	public Integer getIsPrintLabel() {
		return isPrintLabel;
	}

	public void setIsPrintLabel(Integer isPrintLabel) {
		this.isPrintLabel = isPrintLabel;
	}

	public String getTakerTelphone() {
		return takerTelphone;
	}

	public void setTakerTelphone(String takerTelphone) {
		this.takerTelphone = takerTelphone;
	}

	public String getTakerName() {
		return takerName;
	}

	public void setTakerName(String takerName) {
		this.takerName = takerName;
	}

	public String getTakerNumber() {
		return takerNumber;
	}

	public void setTakerNumber(String takerNumber) {
		this.takerNumber = takerNumber;
	}

	public String getDeliverTelphone() {
		return deliverTelphone;
	}

	public void setDeliverTelphone(String deliverTelphone) {
		this.deliverTelphone = deliverTelphone;
	}

	public String getDeliverName() {
		return deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}

	public String getDeliverNumber() {
		return deliverNumber;
	}

	public void setDeliverNumber(String deliverNumber) {
		this.deliverNumber = deliverNumber;
	}

	public Integer getIsNoair() {
		return isNoair;
	}

	public void setIsNoair(Integer isNoair) {
		this.isNoair = isNoair;
	}

	public Long getQueryUserId() {
		return queryUserId;
	}

	public void setQueryUserId(Long queryUserId) {
		this.queryUserId = queryUserId;
	}

	public Long getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Long queryTime) {
		this.queryTime = queryTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getExpressOrderCode() {
		return expressOrderCode;
	}

	public void setExpressOrderCode(String expressOrderCode) {
		this.expressOrderCode = expressOrderCode;
	}

	public String getGtInfoDesc() {
		return gtInfoDesc;
	}

	public void setGtInfoDesc(String gtInfoDesc) {
		this.gtInfoDesc = gtInfoDesc;
	}

}