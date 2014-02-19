package com.yoka.fan.wiget;

public class LinkModel {

	private String brand;
	
	private String type;
	
	private int left;
	
	private int top;
	
	public LinkModel(String brand, String type, int left, int top) {
		super();
		this.brand = brand;
		this.type = type;
		this.left = left;
		this.top = top;
	}

	public String getBrand() {
		return brand;
	}

	public String getTag() {
		return type;
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}
	
	
	
}
