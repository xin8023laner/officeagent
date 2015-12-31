package com.common.events;

import java.util.List;

import com.common.model.AgentWaybill;

/**
 * Created by zhangruntao on 15/5/19.
 */
public class InLibEvent {

	private List<AgentWaybill> agentWaybills;// 多个扫描结果集

	public InLibEvent(List<AgentWaybill> agentWaybills) {
		this.agentWaybills = agentWaybills;
	}

	public List<AgentWaybill> getAgentWaybills() {
		return agentWaybills;
	}

	public void setAgentWaybills(List<AgentWaybill> agentWaybills) {
		this.agentWaybills = agentWaybills;
	}

}
