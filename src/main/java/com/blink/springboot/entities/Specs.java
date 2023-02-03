package com.blink.springboot.entities;

import java.io.Serializable;

public class Specs implements Serializable {
	private String type;
	private Integer cnt;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getCnt() {
		return cnt;
	}
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
}
