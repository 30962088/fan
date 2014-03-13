package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import com.google.gson.Gson;
import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.User;

public abstract class Register extends Request{

	private String email;
	
	private String password;
	
	private String token = Constant.token;
	
	private String username;
	
	private String uuid = Constant.uuid;
	
	private Result result;
	

	
	public static class Result{
		public String access_token;
		public Binds binds;
		public int first;
		public String head_url;
		public int isCheckMail;
		public long lastLoginTime;
		public int loginApi;
		public String nick;
		public int type;
		public String uid;
		public String user_id;
		public String yokausername;
		
		public static class Binds{
			public int YOKA;
		}
		
		public User toUser(){
			User user = User.readUser();
			if(user == null){
				user = new User();
			}
			user.id = user_id;
			user.nickname = nick;
			user.photo = head_url;
			user.access_token = access_token;
			User.saveUser(user);
			return user;
		}
		
	}
	
	public Register( String username,String email, String password) {

		this.email = email;
		this.password = password;
		this.username = username;
	}

	public Result getResult() {
		return result;
	}
	
	protected abstract void onSuccess(Result result); 
	
	@Override
	public void onSuccess(String data) {
		onSuccess(new Gson().fromJson(data, Result.class));
		
	}

	@Override
	public void onError(int code, String msg) {
		
		
	}


	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return HOST+"user/regyoka";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("token", token));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("uuid", uuid));
		return params;
	}

	@Override
	public void onResultError(int code, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServerError(int code, String msg) {
		// TODO Auto-generated method stub
		
	}

}
