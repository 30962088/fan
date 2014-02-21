package com.yoka.fan.wiget;

import java.util.List;

public class LinkModel{
	
	private String url;
	
	private int width;
	
	private int height;
	
	private List<Link> linkList;
	
	private boolean showLink = false;
		
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
		return "http://e.hiphotos.baidu.com/image/w%3D1366%3Bcrop%3D0%2C0%2C1366%2C768/sign=90d5b862cebf6c81f73728eb8a088a56/77094b36acaf2eddaf9b15e38f1001e939019355.jpg";
	}
	
	public List<Link> getLinkList() {
		return linkList;
	}
	
	public int getWidth() {
		return 300;
	}
	
	public int getHeight() {
		return 500;
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


