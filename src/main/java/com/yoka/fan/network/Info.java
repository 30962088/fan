package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yoka.fan.utils.Constant;

public abstract class Info extends Request{
	
	public static class Result{
		private String _id;
		private int follows;
		private int followers;
		private int show_specials;
		private int show_count;
		private String head_url;
		private String nick;
		public int getFollowers() {
			return followers;
		}
		public int getFollows() {
			return follows;
		}
		public String getId() {
			return _id;
		}
		public int getShow_specials() {
			return show_specials;
		}
		public String getHead_url() {
			return IMG_HOST+head_url;
		}
		public String getHeadUrl() {
			return head_url;
		}
		public String getNick() {
			return nick;
		}
		public int getShow_count() {
			return show_count;
		}
	}

	private String access_token;
	
	private String target_ids;
	
	private String user_id;
	
	private String uuid = Constant.uuid;
	
	
	
	
	public Info(String access_token, String user_id,String target_ids) {
		this.access_token = access_token;
		this.target_ids = target_ids;
		this.user_id = user_id;
	}
	
	public Info(String access_token, String user_id,String[] target_ids) {
		this.access_token = access_token;
		this.target_ids = StringUtils.join(target_ids, ',');
		this.user_id = user_id;
	}
	
	
	public abstract void onSuccess(Map<String, Result> result);

	@Override
	public void onSuccess(String data) {
		onSuccess((Map<String, Result>)new Gson().fromJson(data, new TypeToken<Map<String, Result>>(){}.getType()));
		
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
		return HOST+"user/info";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", access_token));
		params.add(new BasicNameValuePair("target_ids", target_ids));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("uuid", uuid));
		return params;
	}
	

}
