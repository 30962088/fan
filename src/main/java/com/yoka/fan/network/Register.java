package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yoka.fan.utils.Constant;

public class Register extends Request{

	private String email;
	
	private String password;
	
	private String token = Constant.token;
	
	private String username;
	
	private String uuid = Constant.uuid;
	
	
	
	public Register(String username,String email, String password) {
		super();
		this.email = email;
		this.password = password;
		this.username = username;
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
