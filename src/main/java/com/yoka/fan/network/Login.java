package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.yoka.fan.network.Register.Result;
import com.yoka.fan.utils.Constant;

public abstract class Login extends Request{

	private String username;
	
	private String password;
	
	private String token = Constant.token;
	
	private String uuid = Constant.uuid;
	
	
	
	public Login(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void onSuccess(String data) {
		
		onSuccess(new Gson().fromJson(data, Result.class));
		
	}
	
	protected abstract void onSuccess(Result result);

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
		return HOST+"user/loginyoka";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("token", token));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		return params;
	}

}
