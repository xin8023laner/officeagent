package com.common.model;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 代办点接入快递公司实体类
 * 
 * @author uimagine
 */
@Table(name="AgentCarrier")
public class AgentCarrier implements Serializable {

	/**
	 * default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id(column="id")
	private int id;
	// Fields
	@Column(column="agentCarrierId")
	private Long agentCarrierId;// 主键值
	@Column(column="type")
	private Integer type; // 快递公司类型[1-直营快递公司 2-快递公司加盟商]
	@Column(column="level")
	private Integer level;// 快递公司级别[1-快递公司 2-加盟商或站点 3-站点]
	@Column(column="agentId")
	private Long agentId;// 代办点ID
	@Column(column="agentCarrierStatus")
	private Integer agentCarrierStatus;// 子商状态[1-可用 2-不可用]
	@Column(column="agentCarrierCode")
	private String agentCarrierCode;// 子商编码
	@Column(column="agentCarrierName")
	private String agentCarrierName;// 子商民称
	@Column(column="agentCarrierPhone")
	private String agentCarrierPhone;// 子商电话
	@Column(column="agentServiceTime")
	private String agentServiceTime;// 子商服务时间
	@Column(column="agentServiceRange")
	private String agentServiceRange;// 子商服务范围
	@Column(column="parentCarrierId")
	private Long parentCarrierId;// 快递公司上级ID
	@Column(column="parentCarrierCode")
	private String parentCarrierCode;// 快递公司上级Code
	@Column(column="parentCarrierName")
	private String parentCarrierName;// 快递公司上级名称
	@Column(column="provinceId")
	private String provinceId;// 省ID
	@Column(column="cityId")
	private String cityId;// 市ID
	@Column(column="districtId")
	private String districtId;// 区ID
	@Column(column="provinceName")
	private String provinceName;// 省名称
	@Column(column="cityName")
	private String cityName;// 市名称
	@Column(column="districtName")
	private String districtName;// 区名称
	@Column(column="address")
	private String address;// 详细地址
	@Column(column="peropoty")
	private String peropoty;// 模糊查询参数
	@Column(column="gtCode")
	private String gtCode; // 贯通code

	// Property accessors

	public AgentCarrier() {
		// TODO Auto-generated constructor stub
	}

	public AgentCarrier(Long agentId, String agentCarrierPhone,
			Long parentCarrierId, String parentCarrierCode,
			String parentCarrierName) {
		super();
		this.agentId = agentId;
		this.agentCarrierPhone = agentCarrierPhone;
		this.parentCarrierId = parentCarrierId;
		this.parentCarrierCode = parentCarrierCode;
		this.parentCarrierName = parentCarrierName;
	}

	public AgentCarrier(Long agentCarrierId, Integer type, Integer level,
			Long agentId, Integer agentCarrierStatus, String agentCarrierCode,
			String agentCarrierName, String agentCarrierPhone,
			String agentServiceTime, String agentServiceRange,
			Long parentCarrierId, String parentCarrierCode,
			String parentCarrierName, String provinceId, String cityId,
			String districtId, String provinceName, String cityName,
			String districtName, String address, String peropoty, String gtCode) {
		super();
		this.agentCarrierId = agentCarrierId;
		this.type = type;
		this.level = level;
		this.agentId = agentId;
		this.agentCarrierStatus = agentCarrierStatus;
		this.agentCarrierCode = agentCarrierCode;
		this.agentCarrierName = agentCarrierName;
		this.agentCarrierPhone = agentCarrierPhone;
		this.agentServiceTime = agentServiceTime;
		this.agentServiceRange = agentServiceRange;
		this.parentCarrierId = parentCarrierId;
		this.parentCarrierCode = parentCarrierCode;
		this.parentCarrierName = parentCarrierName;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.districtId = districtId;
		this.provinceName = provinceName;
		this.cityName = cityName;
		this.districtName = districtName;
		this.address = address;
		this.peropoty = peropoty;
		this.gtCode = gtCode;
	}

	public Long getAgentCarrierId() {
		return agentCarrierId;
	}

	public void setAgentCarrierId(Long agentCarrierId) {
		this.agentCarrierId = agentCarrierId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Integer getAgentCarrierStatus() {
		return agentCarrierStatus;
	}

	public void setAgentCarrierStatus(Integer agentCarrierStatus) {
		this.agentCarrierStatus = agentCarrierStatus;
	}

	public String getAgentCarrierCode() {
		return agentCarrierCode;
	}

	public void setAgentCarrierCode(String agentCarrierCode) {
		this.agentCarrierCode = agentCarrierCode;
	}

	public String getAgentCarrierName() {
		return agentCarrierName;
	}

	public void setAgentCarrierName(String agentCarrierName) {
		this.agentCarrierName = agentCarrierName;
	}

	public String getAgentCarrierPhone() {
		return agentCarrierPhone;
	}

	public void setAgentCarrierPhone(String agentCarrierPhone) {
		this.agentCarrierPhone = agentCarrierPhone;
	}

	public String getAgentServiceTime() {
		return agentServiceTime;
	}

	public void setAgentServiceTime(String agentServiceTime) {
		this.agentServiceTime = agentServiceTime;
	}

	public String getAgentServiceRange() {
		return agentServiceRange;
	}

	public void setAgentServiceRange(String agentServiceRange) {
		this.agentServiceRange = agentServiceRange;
	}

	public Long getParentCarrierId() {
		return parentCarrierId;
	}

	public void setParentCarrierId(Long parentCarrierId) {
		this.parentCarrierId = parentCarrierId;
	}

	public String getParentCarrierCode() {
		return parentCarrierCode;
	}

	public void setParentCarrierCode(String parentCarrierCode) {
		this.parentCarrierCode = parentCarrierCode;
	}

	public String getParentCarrierName() {
		return parentCarrierName;
	}

	public void setParentCarrierName(String parentCarrierName) {
		this.parentCarrierName = parentCarrierName;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPeropoty() {
		return peropoty;
	}

	public void setPeropoty(String peropoty) {
		this.peropoty = peropoty;
	}

	public String  getGtCode() {
		return gtCode;
	}

	public void setGtCode(String gtCode) {
		this.gtCode = gtCode;
	}

}