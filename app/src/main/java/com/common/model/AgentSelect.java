package com.common.model;

/**
 * Created by Administrator on 2015/5/28.
 */
public class AgentSelect {
    private String abbreviation;
    private String address;
    private Long agentId;
    private String cityId;
    private String cityName;
    private String code;
    private String createTime;
    private String creatorId;
    private String creatorName;
    private String districtId;
    private String districtName;
    private double lat;
    private double lng;
    private String name;
    private String phone;
    private String property;
    private String provinceId;
    private String provinceName;
    private String remark;
    private String serviceDesc;
    private String serviceRange;
    private String serviceTime;
    private Integer status;
    private Integer type;
    private String updateTime;

    public AgentSelect() {
    }

    public AgentSelect(String abbreviation, String address, Long agentId, String cityId, String cityName, String code, String createTime, String creatorId, String creatorName, String districtId, String districtName, double lat, double lng, String name, String phone, String property, String provinceId, String provinceName, String remark, String serviceDesc, String serviceRange, String serviceTime, Integer status, Integer type, String updateTime) {
        this.abbreviation = abbreviation;
        this.address = address;
        this.agentId = agentId;
        this.cityId = cityId;
        this.cityName = cityName;
        this.code = code;
        this.createTime = createTime;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.phone = phone;
        this.property = property;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.remark = remark;
        this.serviceDesc = serviceDesc;
        this.serviceRange = serviceRange;
        this.serviceTime = serviceTime;
        this.status = status;
        this.type = type;
        this.updateTime = updateTime;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getServiceRange() {
        return serviceRange;
    }

    public void setServiceRange(String serviceRange) {
        this.serviceRange = serviceRange;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
