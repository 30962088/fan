package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yoka.fan.utils.Constant;

public class Modify extends Request{
	
	private String access_token;
	
	private String job;
	
	private String nick;
	
	private int sex;
	
	private String user_id;
	
	private String uuid = Constant.uuid;
	
	

	public Modify(String access_token, String job, String nick, int sex,
			String user_id) {
		this.access_token = access_token;
		this.job = job;
		this.nick = nick;
		this.sex = sex;
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
		return HOST+"user/mod";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", access_token));
		params.add(new BasicNameValuePair("job", job));
		params.add(new BasicNameValuePair("nick", nick));
		params.add(new BasicNameValuePair("sex", ""+sex));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("uuid", uuid));
		return params;
	}

}
