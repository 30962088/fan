package com.yoka.fan.wiget;

import java.util.List;

public class CommonListModel {
	
	private String name;
	
	private String photo;
	
	private String datetime;
	
	private int star;
	
	private boolean stared;
	
	private int comment;
	
	private LinkModel linkModel;
	
	private List<String> Tags;
	
	public CommonListModel() {
		// TODO Auto-generated constructor stub
	}
	

	public CommonListModel(String name, String photo, String datetime,
			int star, boolean stared, int comment, LinkModel linkModel, List<String> tags) {
		super();
		this.name = name;
		this.photo = photo;
		this.datetime = datetime;
		this.star = star;
		this.stared = stared;
		this.comment = comment;
		this.linkModel = linkModel;
		Tags = tags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public boolean isStared() {
		return stared;
	}

	public void setStared(boolean stared) {
		this.stared = stared;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public void setLinkModel(LinkModel linkModel) {
		this.linkModel = linkModel;
	}
	public LinkModel getLinkModel() {
		return linkModel;
	}
	
	public List<String> getTags() {
		return Tags;
	}

	public void setTags(List<String> tags) {
		Tags = tags;
	}
	
	
	
	
	
	
	
}
