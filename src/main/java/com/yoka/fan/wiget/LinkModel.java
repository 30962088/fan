package com.yoka.fan.wiget;

import java.util.List;

public class LinkModel{
	
	private String url;
	
	private int width;
	
	private int height;
	
	private List<Link> linkList;
	
	private boolean showLink = true;
		
	public LinkModel(String url,int width,int height, List<Link> linkList) {
		this.width = width;
		this.height = height;
		this.url = url;
		this.linkList = linkList;
	}
	
	public void setShowLink(boolean showLink) {
		this.showLink = showLink;
	}
	
	public boolean isShowLink() {
		return showLink;
	}

	public String getUrl() {
		return url;
	}
	
	public List<Link> getLinkList() {
		return linkList;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public static class Link {
		
		private String id;
		
		private String name;
		
		private float left;
		
		private float top;
		
		
		
		public Link(String id,String name, float left, float top) {
			this.name = name;
			this.left = left;
			this.top = top;
		}
		
		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public float getLeft() {
			return left;
		}

		public float getTop() {
			return top;
		}
		
		
		
	}
	
}


