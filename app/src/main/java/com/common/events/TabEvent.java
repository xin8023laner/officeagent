package com.common.events;

public class TabEvent {
private int index;
private int size;
public int getIndex() {
	return index;
}
public void setIndex(int index) {
	this.index = index;
}
public int getSize() {
	return size;
}
public void setSize(int size) {
	this.size = size;
}
public TabEvent(int index, int size) {
	super();
	this.index = index;
	this.size = size;
}
}
