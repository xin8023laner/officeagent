package com.common.model;

import java.io.Serializable;

/**
 * 用户实体类
 * 
 * @author zhangruntao
 *
 */
public class AgentUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// TestUser
	public int yueShou; // 月收
	public int yueSong; // 月送

	// Fields

	private Long agentUserId;// 主键值
	private Integer status;// 用户状态[1-可用 2-不可用]
	private Long agentId;// 代办点ID
	private String agentUserCode;// 用户编号
	private String agentUserName;// 用户名称
	private String agentUserPswd;// 用户密码
	private String telphone;// 手机号码
	private String remark;// 个性签名
	private Long createTime;// 创建时间
	private Long updateTime;// 更新时间
	private String peropoty;
	private String nickName;
	private String realName;
	private String birthday;
	private String email;
	private Integer sex; //0：男  1：女
	private String photoName;
	private String token;

	// Empty Construtor
	public AgentUser() {
		super();
	}

	// Full Constructor
	public AgentUser(Long agentUserId, Integer status, Long agentId,
			String agentUserCode, String agentUserName, String agentUserPswd,
			String telphone, String remark, Long createTime, Long updateTime) {
		super();
		this.agentUserId = agentUserId;
		this.status = status;
		this.agentId = agentId;
		this.agentUserCode = agentUserCode;
		this.agentUserName = agentUserName;
		this.agentUserPswd = agentUserPswd;
		this.telphone = telphone;
		this.remark = remark;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	
	public String getPhotoName() {
		return photoName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	
	public Long getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getAgentUserCode() {
		return agentUserCode;
	}

	public void setAgentUserCode(String agentUserCode) {
		this.agentUserCode = agentUserCode;
	}

	public String getAgentUserName() {
		return agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}

	public String getAgentUserPswd() {
		return agentUserPswd;
	}

	public void setAgentUserPswd(String agentUserPswd) {
		this.agentUserPswd = agentUserPswd;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getPeropoty() {
		return peropoty;
	}

	public void setPeropoty(String peropoty) {
		this.peropoty = peropoty;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
}