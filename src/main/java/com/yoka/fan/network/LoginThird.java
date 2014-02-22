package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.yoka.fan.utils.Constant;

public class LoginThird extends Request{

	public static final String TYPE_TENCENT = "tecent";
	
	public static final String TYPE_SINA = "sina";
	
	private String authType;
	
	
	
	private String token = Constant.token;
	
	private String uuid = Constant.uuid;
	
	private String third_token_info;
	
	
	public LoginThird(String authType, Info info) {
		this.authType = authType;
		this.third_token_info = new Gson().toJson(info); 
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
		return HOST+"user/auth_third_login";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("authType",authType));
		params.add(new BasicNameValuePair("third_token_info",third_token_info));
		params.add(new BasicNameValuePair("token",token));
		params.add(new BasicNameValuePair("uuid",uuid));
		return params;
	}
	

	
	public static class Info{
		
		public String access_token;
		
		public String refresh_token;
		
		public String openid;
		
		public String openkey;
		
		public String expires_in;
		
		public String seqid;
		
		public String data_name;
		
		public String data_email;
		
		public String data_nick;
		
		public String data_sex;
		
		public Integer data_province_code;
		
		public Integer data_birth_year;
		
		public Integer data_birth_month;
		
		public Integer data_birth_day;
		
		public String data_introduction;
		
		public String data_head;
		
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
