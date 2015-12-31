package com.common.model;
/**
 * WheelView基础实体类模板
 * @author zhangruntao
 *
 */
public class WheelBaseItem {
	private String id;
	private String name;
	private String code;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName(){
		return name;
	}
	public String getCode(){
		return code;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setCode(String code){
		this.code=code;
	}
}
