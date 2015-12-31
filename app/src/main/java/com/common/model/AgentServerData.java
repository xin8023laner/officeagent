package com.common.model;

public class AgentServerData {

	private Long dataId;//服务器Id
	private Long agentId;//代办点Id
	private Long serverUrl;//ip地址
	public Long getDataId() {
		return dataId;
	}
	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	public Long getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(Long serverUrl) {
		this.serverUrl = serverUrl;
	}
	
}
