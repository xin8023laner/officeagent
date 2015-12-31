package com.common.events;

import com.common.model.AgentCarrier;

public class ChooseSendCompanyEvent {

	private AgentCarrier agentCarrier;

	public ChooseSendCompanyEvent(AgentCarrier agentCarrier) {
		super();
		this.agentCarrier = agentCarrier;
	}

	public AgentCarrier getAgentCarrier() {
		return agentCarrier;
	}

	public void setAgentCarrier(AgentCarrier agentCarrier) {
		this.agentCarrier = agentCarrier;
	}
	
	
}
