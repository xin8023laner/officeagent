package com.common.model;

/**
 * Created by Administrator on 2015/5/28.
 */
public class UserInfo {
    private String account;
    private Integer agentId;
    private String agentUserCode;
    private Integer agentUserId;
    private String agentUserName;
    private String agentUserPswd;
    private String email;
    private String telphone;

    public UserInfo() {
    }

    public UserInfo(String telphone, Integer agentId, String agentUserCode, Integer agentUserId, String agentUserName, String agentUserPswd, String email, String account) {
        this.telphone = telphone;
        this.agentId = agentId;
        this.agentUserCode = agentUserCode;
        this.agentUserId = agentUserId;
        this.agentUserName = agentUserName;
        this.agentUserPswd = agentUserPswd;
        this.email = email;
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentUserCode() {
        return agentUserCode;
    }

    public void setAgentUserCode(String agentUserCode) {
        this.agentUserCode = agentUserCode;
    }

    public Integer getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(Integer agentUserId) {
        this.agentUserId = agentUserId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
}
