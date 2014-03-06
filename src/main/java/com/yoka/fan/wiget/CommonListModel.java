package com.yoka.fan.wiget;

import java.util.List;

public class CommonListModel {
	
	private String id;
	
	private String name;
	
	private String user_id;
	
	private String photo;
	
	private String datetime;
	
	private int star;
	
	private boolean stared;
	
	private int comment;
	
	private boolean showLinked = false;
	
	private LinkModel linkModel;
	
	private List<String> Tags;
	
	private String descr;
	
	public CommonListModel() {
		// TODO Auto-generated constructor stub
	}
	

	public CommonListModel(String name, String photo, String datetime,
			int star, boolean stared, int comment, LinkModel linkModel, List<String> tags,String desc) {
		super();
		this.name = name;
		this.photo = photo;
		this.datetime = datetime;
		this.star = star;
		this.stared = stared;
		this.comment = comment;
		this.linkModel = linkModel;
		this.Tags = tags;
		this.descr = desc;
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
	
	
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getDescr() {
		return descr;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_id() {
		return user_id;
	}
	

	public void setShowLinked(boolean showLinked) {
		this.showLinked = showLinked;
	}
	
	public boolean isShowLinked() {
		return showLinked;
	}
	
}
