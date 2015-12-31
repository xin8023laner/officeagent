/**
 * 2015-12-16PickupCallEvent.javaAdministrator
 */
package com.common.model;

/**
 * @author Administrator
 *         2015-12-16
 * 
 */
public class SendCallEvent {
	private AgentReceiveTask agentReceiveTask;

	public SendCallEvent() {
		super();
	}

	public SendCallEvent(AgentReceiveTask agentReceiveTask) {
		super();
		this.agentReceiveTask = agentReceiveTask;
	}

	public AgentReceiveTask getAgentReceiveTask() {
		return agentReceiveTask;
	}

	public void setAgentReceiveTask(AgentReceiveTask agentReceiveTask) {
		this.agentReceiveTask = agentReceiveTask;
	}
	
}
