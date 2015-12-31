package com.common.events;

public class ChangeTabEvent {
	private String tab;

	public ChangeTabEvent(String tab) {
		super();
		this.tab = tab;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}
	
}
