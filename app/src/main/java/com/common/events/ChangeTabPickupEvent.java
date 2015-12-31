package com.common.events;

public class ChangeTabPickupEvent {
	private String tab;

	public ChangeTabPickupEvent(String tab) {
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
