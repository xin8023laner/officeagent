package com.common.events;

import com.common.model.Agent;

/**
 * Created by zhangruntao on 15/5/19.
 */
public class ChoiseAgentEvent {

	private Agent agent;

	public ChoiseAgentEvent(Agent agent) {
		this.agent = agent;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

}
