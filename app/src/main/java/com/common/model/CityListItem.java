package com.common.model;

/**
 * 选择省市区实体类
 * @author zhangruntao
 *
 */

public class CityListItem{
	private String name;
	private String pcode;
	
	public String getName(){
		return name;
	}
	public String getPcode(){
		return pcode;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setPcode(String pcode){
		this.pcode=pcode;
	}
	
	@Override
	public String toString() {
		return "CityListItem [name=" + name + ", pcode=" + pcode + "]";
	}
	
}
