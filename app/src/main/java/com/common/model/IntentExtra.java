package com.common.model;
import java.io.Serializable;

/**
 * Intent传值实体类
 * @author catcry
 */
public class IntentExtra implements Serializable {
	public final static String intent="intent";
	public final static String result="result";
	/**
	 * default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	// Fields
	
	private int type;// 操作类型
	private Object value;// 数据
	
	// Empty Constructor
	public IntentExtra() {
		super();
	}

	// Full Constructor
	public IntentExtra(int type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	
}