package com.yoka.fan.wiget;

import java.util.List;

public class LinkModel{
	
	private String url;
	
	private List<Link> linkList;
		
	public LinkModel(String url, List<Link> linkList) {

		this.url = url;
		this.linkList = linkList;
	}

	public String getUrl() {
		return url;
	}
	
	public List<Link> getLinkList() {
		return linkList;
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


