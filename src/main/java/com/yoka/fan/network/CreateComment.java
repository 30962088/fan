package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.yoka.fan.utils.Constant;

public class CreateComment extends Request{

	private String uuid = Constant.uuid;
	
	private String user_id;
	
	private String access_token;
	
	private String coll_id;
	
	private String content;
	
	private int comment_type = 2;
	
	private String goods_id = "";
	
	public static class Result{
		public static class Id{
			private String $id;
			public String get$id() {
				return $id;
			}
		}
		private Id _id;
		private long create_date;
		private String txt;
		public Result() {
			// TODO Auto-generated constructor stub
		}
		public Id get_id() {
			return _id;
		}
		public long getCreate_date() {
			return create_date;
		}
		public String getTxt() {
			return txt;
		}
		
		
	}
	
	private Result result;
	
	public CreateComment(String user_id, String access_token, String coll_id,
			String content) {
		super();
		this.user_id = user_id;
		this.access_token = access_token;
		this.coll_id = coll_id;
		this.content = content;

	}
	
	

	@Override
	public void onSuccess(String data) {
		result = new Gson().fromJson(data, Result.class);
	}
	
	public Result getResult() {
		return result;
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
		return HOST+"comm/create";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("access_token", access_token));
		params.add(new BasicNameValuePair("coll_id", coll_id));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("comment_type", ""+comment_type));
		params.add(new BasicNameValuePair("goods_id", coll_id));
		return params;
	}

}
