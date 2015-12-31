package com.common.events;

import com.common.model.AgentReceiveTask;
import com.common.model.TaskWaybill;

public class CallEvent {
	private int type;
	private TaskWaybill taskWayBill;
	private AgentReceiveTask agentReceiveTask;
	
	public static final int Send=0;
	public static final int Pickup=1;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public TaskWaybill getTaskWayBill() {
		return taskWayBill;
	}
	public void setTaskWayBill(TaskWaybill taskWayBill) {
		this.taskWayBill = taskWayBill;
	}
	public AgentReceiveTask getAgentReceiveTask() {
		return agentReceiveTask;
	}
	public void setAgentReceiveTask(AgentReceiveTask agentReceiveTask) {
		this.agentReceiveTask = agentReceiveTask;
	}
	public static int getSend() {
		return Send;
	}
	public static int getPickup() {
		return Pickup;
	}
	public CallEvent(int type, TaskWaybill taskWayBill,
			AgentReceiveTask agentReceiveTask) {
		super();
		this.type = type;
		this.taskWayBill = taskWayBill;
		this.agentReceiveTask = agentReceiveTask;
	}
}
