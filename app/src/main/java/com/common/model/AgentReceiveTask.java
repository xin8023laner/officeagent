package com.common.model;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 代办点代收任务实体类
 * 
 * @author uimagine
 */
@Table(name="AgentReceiveTask")
public class AgentReceiveTask implements Serializable {

	/**
	 * default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id(column="id")
	private int id;
	// Fields
	@Column(column="taskId")
	private Long taskId;// 主键值
	@Column(column="agentId")
	private Long agentId;// 代办点id
	@Column(column="taskStatus")
	private Short taskStatus;// 任务状态[派件入库- 1 已入库：-2已妥投－3  快件返库 快件返库-5   已退件-6 派送中－4，7-未妥投]
	@Column(column="taskBatchNo")
	private String taskBatchNo;// 任务批次号
	@Column(column="taskOutBatchNo")
	private String taskOutBatchNo;// 扫出任务批次号
	@Column(column="courierCode")
	private String courierCode;// 操作快递员Code
	@Column(column="courierGtCode")
	private String courierGtCode;// 操作快递员贯通 code
	@Column(column="carrierSiteId")
	private Long carrierSiteId;// 所属站点ID
	@Column(column="carrierSiteName")
	private String carrierSiteName;// 所属站点名称
	@Column(column="carrierSiteCode")
	private String carrierSiteCode;// 所属站点code
	@Column(column="carrierSubId")
	private Long carrierSubId;// 所属加盟商ID
	@Column(column="carrierSubName")
	private String carrierSubName;// 所属加盟商name
	@Column(column="carrierSubCode")
	private String carrierSubCode;// 所属加盟商code
	@Column(column="carrierId")
	private Long carrierId;// 快递公司ID
	@Column(column="carrierCode")
	private String carrierCode;// 快递公司Code
	@Column(column="carrierName")
	private String carrierName;// 快递公司名称
	@Column(column="expressCode")
	private String expressCode;// 快递单号
	@Column(column="receiverName")
	private String receiverName;// 收件人姓名
	@Column(column="receiverPhone")
	private String receiverPhone;// 收件人电话
	@Column(column="receiverAddress")
	private String receiverAddress;// 收件人详细地址
	@Column(column="waybillCode")
	private String waybillCode;// 贯通订单号
	@Column(column="cost")
	private Double cost;// 快递费用
	@Column(column="scanTime")
	private Long scanTime;// 扫描时间
	@Column(column="updateTime")
	private Long updateTime;// 更新时间
	@Column(column="scanOutTime")
	private Long scanOutTime;//出库时间
	@Column(column="agentUserCode")
	private String agentUserCode;// 操作人Code
	@Column(column="agentUserId")
	private Long agentUserId;// 代办点操作人员ID
	@Column(column="agentUserName")
	private String agentUserName;// 代办点操作人员name
	@Column(column="userRejectDesc")
	private String userRejectDesc;// 操作人员拒收任务原因
	@Column(column="receiverInsureCode")
	private String receiverInsureCode;// 收件确认码
	@Column(column="location")
	private String location;// 快件存放位置
	@Column(column="statusDesc")
	private String statusDesc;// 拒收原因
	@Column(column="isDelete")
	private Short isDelete;// 是否已删除[1-未删除 2-已删除]
	@Column(column="returnTime")
	private Long returnTime;//回库时间
	@Column(column="shelfNo")
	private String shelfNo;//货架编号
	@Column(column="courierName")
	private String courierName;//快递员的名字
	@Column(column="markType")
	private Integer markType;//是否为代办员自揽      0-自揽     1-系统派发
	@Column(column="moveStatus")
	private Integer moveStatus;//是否已移库               0-未移动  2-已移动
	@Column(column="cargoCost")
	private Double cargoCost;// 货款
	@Column(column="sendType")
	private Integer sendType;//派送方式  1-派送    2-自提   3-前台
	@Column(column="successMsg")
	private Integer successMsg;//短信发送是否成功 0-成功    1-失败
	@Column(column="weight")
	private Double weight;//重量
	@Column(column="type")
	private Integer type;//用CaptureActivity中type区分
	@Column(column="stype")
	private String stype;
	

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(Integer successMsg) {
		this.successMsg = successMsg;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Double getCargoCost() {
		return cargoCost;
	}

	public void setCargoCost(Double cargoCost) {
		this.cargoCost = cargoCost;
	}

	public Integer getMarkType() {
		return markType;
	}

	public void setMarkType(Integer markType) {
		this.markType = markType;
	}

	public Integer getMoveStatus() {
		return moveStatus;
	}

	public void setMoveStatus(Integer moveStatus) {
		this.moveStatus = moveStatus;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public Long getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Long returnTime) {
		this.returnTime = returnTime;
	}

	public String getShelfNo() {
		return shelfNo;
	}

	public void setShelfNo(String shelfNo) {
		this.shelfNo = shelfNo;
	}

	public Long getScanOutTime() {
		return scanOutTime;
	}

	public void setScanOutTime(Long scanOutTime) {
		this.scanOutTime = scanOutTime;
	}

	public String getAgentUserName() {
		return agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}

	public String getUserRejectDesc() {
		return userRejectDesc;
	}

	public void setUserRejectDesc(String userRejectDesc) {
		this.userRejectDesc = userRejectDesc;
	}

	public Long getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public AgentReceiveTask(Long taskId, Long agentId, Short taskStatus,
			String taskBatchNo, String taskOutBatchNo, String courierCode,
			String courierGtCode, Long carrierSiteId, String carrierSiteName,
			String carrierSiteCode, Long carrierSubId, String carrierSubName,
			String carrierSubCode, Long carrierId, String carrierCode,
			String carrierName, String expressCode, String receiverName,
			String receiverPhone, String receiverAddress, String waybillCode,
			Double cost, Long scanTime, Long updateTime, Long scanOutTime, String agentUserCode,
			Long agentUserId, String agentUserName, String userRejectDesc,
			String receiverInsureCode, String location, String statusDesc,
			Short isDelete,Long returnTime, String shelfNo,String courierName,Integer markType,
			Integer moveStatus,Double cargoCost,Integer sendType,Integer successMsg,Double weight,Integer type) {
		super();
		this.taskId = taskId;
		this.agentId = agentId;
		this.taskStatus = taskStatus;
		this.taskBatchNo = taskBatchNo;
		this.taskOutBatchNo = taskOutBatchNo;
		this.courierCode = courierCode;
		this.courierGtCode = courierGtCode;
		this.carrierSiteId = carrierSiteId;
		this.carrierSiteName = carrierSiteName;
		this.carrierSiteCode = carrierSiteCode;
		this.carrierSubId = carrierSubId;
		this.carrierSubName = carrierSubName;
		this.carrierSubCode = carrierSubCode;
		this.carrierId = carrierId;
		this.carrierCode = carrierCode;
		this.carrierName = carrierName;
		this.expressCode = expressCode;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.receiverAddress = receiverAddress;
		this.waybillCode = waybillCode;
		this.cost = cost;
		this.scanTime = scanTime;
		this.updateTime = updateTime;
		this.scanOutTime = scanOutTime;
		this.agentUserCode = agentUserCode;
		this.agentUserId = agentUserId;
		this.agentUserName = agentUserName;
		this.userRejectDesc = userRejectDesc;
		this.receiverInsureCode = receiverInsureCode;
		this.location = location;
		this.statusDesc = statusDesc;
		this.isDelete = isDelete;
		this.returnTime = returnTime;
		this.shelfNo = shelfNo;
		this.courierName = courierName;
		this.markType = markType;
		this.moveStatus = moveStatus;
		this.cargoCost = cargoCost;
		this.sendType = sendType;
		this.successMsg = successMsg;
		this.weight = weight;
		this.type=type;
	}

	public String getCourierGtCode() {
		return courierGtCode;
	}

	public void setCourierGtCode(String courierGtCode) {
		this.courierGtCode = courierGtCode;
	}

	public Long getCarrierSiteId() {
		return carrierSiteId;
	}

	public void setCarrierSiteId(Long carrierSiteId) {
		this.carrierSiteId = carrierSiteId;
	}

	public String getCarrierSiteName() {
		return carrierSiteName;
	}

	public void setCarrierSiteName(String carrierSiteName) {
		this.carrierSiteName = carrierSiteName;
	}

	public String getCarrierSiteCode() {
		return carrierSiteCode;
	}

	public void setCarrierSiteCode(String carrierSiteCode) {
		this.carrierSiteCode = carrierSiteCode;
	}

	public Long getCarrierSubId() {
		return carrierSubId;
	}

	public void setCarrierSubId(Long carrierSubId) {
		this.carrierSubId = carrierSubId;
	}

	public String getCarrierSubName() {
		return carrierSubName;
	}

	public void setCarrierSubName(String carrierSubName) {
		this.carrierSubName = carrierSubName;
	}

	public String getCarrierSubCode() {
		return carrierSubCode;
	}

	public void setCarrierSubCode(String carrierSubCode) {
		this.carrierSubCode = carrierSubCode;
	}

	public Short getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Short isDelete) {
		this.isDelete = isDelete;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	// Empty Constructor
	public AgentReceiveTask() {
		super();
	}

	// Property accessors

	public Long getTaskId() {
		return taskId;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Short getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Short taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskBatchNo() {
		return taskBatchNo;
	}

	public void setTaskBatchNo(String taskBatchNo) {
		this.taskBatchNo = taskBatchNo;
	}

	public String getCourierCode() {
		return courierCode;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
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

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
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

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getWaybillCode() {
		return waybillCode;
	}

	public void setWaybillCode(String waybillCode) {
		this.waybillCode = waybillCode;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Long getScanTime() {
		return scanTime;
	}

	public void setScanTime(Long scanTime) {
		this.scanTime = scanTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getAgentUserCode() {
		return agentUserCode;
	}

	public void setAgentUserCode(String agentUserCode) {
		this.agentUserCode = agentUserCode;
	}

	public String getReceiverInsureCode() {
		return receiverInsureCode;
	}

	public void setReceiverInsureCode(String receiverInsureCode) {
		this.receiverInsureCode = receiverInsureCode;
	}

	public String getTaskOutBatchNo() {
		return taskOutBatchNo;
	}

	public void setTaskOutBatchNo(String taskOutBatchNo) {
		this.taskOutBatchNo = taskOutBatchNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}