package com.gt.inteface;


public class AdapterItemClickEvent {
	private Object object;
	private int position;
	private int type;

	public static final int RECEIVETASKTYPE_BACK=0;
	public static final int RECEIVETASKTYPE_SUBMIT=1;

	public AdapterItemClickEvent(Object object, int position, int type) {
		super();
		this.object = object;
		this.position = position;
		this.type = type;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}