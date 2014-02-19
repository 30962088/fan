package com.yoka.fan.wiget;

import java.util.List;

public class CommonListModel {
	
	private String name;
	
	private String photo;
	
	private String datetime;
	
	private int star;
	
	private boolean stared;
	
	private int comment;
	
	private List<LinkModel> linkList;
	
	private String img;
	
	private List<String> Tags;
	
	public CommonListModel() {
		// TODO Auto-generated constructor stub
	}
	

	public CommonListModel(String name, String photo, String datetime,
			int star, boolean stared, int comment, List<LinkModel> linkList,
			String img, List<String> tags) {
		super();
		this.name = name;
		this.photo = photo;
		this.datetime = datetime;
		this.star = star;
		this.stared = stared;
		this.comment = comment;
		this.linkList = linkList;
		this.img = img;
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

	public List<LinkModel> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<LinkModel> linkList) {
		this.linkList = linkList;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public List<String> getTags() {
		return Tags;
	}

	public void setTags(List<String> tags) {
		Tags = tags;
	}
	
	
	
	
	
	
	
}
