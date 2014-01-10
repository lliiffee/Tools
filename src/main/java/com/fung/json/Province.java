package com.fung.json;

import java.util.List;

public class Province {
	
	private String name;
	private String id;
	private City[] citys;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public City[] getCitys() {
		return citys;
	}
	public void setCitys(City[] citys) {
		this.citys = citys;
	}
 
	
	

}
