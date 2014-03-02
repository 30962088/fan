package com.yoka.fan.network;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class CollSave extends Request{

	public static class Link{
		private String brand;
		private String color;
		private String left;
		private String top;
		private String type;
		private float price;
		private String url;
		public Link(String brand, String color, float left, float top,
				String type, float price, String url) {
			this.brand = brand;
			this.color = color;
			this.left = left*100+"%";
			this.top = top*100+"%";
			this.type = type;
			this.price = price;
			this.url = url;
		}
		public String getBrand() {
			return brand;
		}
		public String getColor() {
			return color;
		}
		public String getLeft() {
			return left;
		}
		public String getTop() {
			return top;
		}
		public String getType() {
			return type;
		}
		public float getPrice() {
			return price;
		}
		public String getUrl() {
			return url;
		}
		
		public float getTopFloat(){
			return Float.parseFloat(top.substring(0,top.length()-2))/100;
		}
		public float getLeftFloat(){
			return Float.parseFloat(left.substring(0,left.length()-2))/100;
		}
		
		
	}
	
	private Map<String, Link> link_goods = new HashMap<String, CollSave.Link>();
	
	private String access_token;
	
	private String description;
	
	private String coll_id="";
	
	private File uploadimg;
	
	private int width;
	
	private int height;
	
	private String user_id;
	
	private String uuid;
	
	public CollSave(Map<String, Link> link_goods, String access_token,
			String description, File uploadimg, int width, int height,
			String user_id) {
		this.link_goods = link_goods;
		this.access_token = access_token;
		this.description = description;
		this.uploadimg = uploadimg;
		this.width = width;
		this.height = height;
		this.user_id = user_id;
	}

	@Override
	public void onSuccess(String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int code, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResultError(int code, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServerError(int code, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return HOST+"coll/save";
	}
	
	@Override
	protected Map<String, File> fillFiles() {
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("uploadimg", uploadimg);
		return map;
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("link_goods", new JSONObject(link_goods).toString()));
		params.add(new BasicNameValuePair("access_token", access_token));
		params.add(new BasicNameValuePair("description", description));
		params.add(new BasicNameValuePair("coll_id", coll_id));
		params.add(new BasicNameValuePair("width", ""+width));
		params.add(new BasicNameValuePair("height", ""+height));
		params.add(new BasicNameValuePair("user_id", user_id));
		return params;
	}

}
