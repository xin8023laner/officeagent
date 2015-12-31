package com.common.events;

import com.common.model.AgentContactCustomer;

public class ChangeContactPhoneEvent {
	private int position;
	private AgentContactCustomer agentContactCustomer;
	private int type;
	public static final int TYPE_CONSTANTS_EXCEP=1;
	public static final int TYPE_CONSTANTS_EDITPHONE=2;
	public static final int TYPE_CONSTANTS_SUPPLEINFO=3;
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public AgentContactCustomer getAgentContactCustomer() {
		return agentContactCustomer;
	}
	public void setAgentContactCustomer(AgentContactCustomer agentContactCustomer) {
		this.agentContactCustomer = agentContactCustomer;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public ChangeContactPhoneEvent(int position,
			AgentContactCustomer agentContactCustomer, int type) {
		super();
		this.position = position;
		this.agentContactCustomer = agentContactCustomer;
		this.type = type;
	}
	public ChangeContactPhoneEvent() {
		super();
	}
	
}
