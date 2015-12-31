/**
 * 2015-12-16PickupCallEvent.javaAdministrator
 */
package com.common.model;

/**
 * @author Administrator
 *         2015-12-16
 * 
 */
public class PickupCallEvent {
	private TaskWaybill taskWaybill;

	
	public PickupCallEvent() {
		super();
	}

	public PickupCallEvent(TaskWaybill taskWaybill) {
		super();
		this.taskWaybill = taskWaybill;
	}

	public TaskWaybill getTaskWaybill() {
		return taskWaybill;
	}

	public void setTaskWaybill(TaskWaybill taskWaybill) {
		this.taskWaybill = taskWaybill;
	}
	
	
}
