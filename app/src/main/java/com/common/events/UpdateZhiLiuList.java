package com.common.events;

import com.common.model.AgentReceiveTask;

public class UpdateZhiLiuList {

	private AgentReceiveTask agentReceiveTask;

	public AgentReceiveTask getAgentReceiveTask() {
		return agentReceiveTask;
	}

	public void setAgentReceiveTask(AgentReceiveTask agentReceiveTask) {
		this.agentReceiveTask = agentReceiveTask;
	}

	public UpdateZhiLiuList(AgentReceiveTask agentReceiveTask) {
		super();
		this.agentReceiveTask = agentReceiveTask;
	}
}
