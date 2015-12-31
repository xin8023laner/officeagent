package com.common.events;

import com.common.model.AgentReceiveTask;

public class ScanResultEvent {

	private AgentReceiveTask agentReceiveTask;
	private int type;

	public static final int TYPE_SCANREMOVE=1;
	public static final int TYPE_SCANBULU=2;

	public ScanResultEvent(AgentReceiveTask agentReceiveTask, int type) {
		super();
		this.agentReceiveTask = agentReceiveTask;
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public AgentReceiveTask getAgentReceiveTask() {
		return agentReceiveTask;
	}

	public void setAgentReceiveTask(AgentReceiveTask agentReceiveTask) {
		this.agentReceiveTask = agentReceiveTask;
	}

}
