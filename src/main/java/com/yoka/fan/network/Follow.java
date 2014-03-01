package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yoka.fan.utils.Constant;

public class Follow extends Request{
	
	private String uuid = Constant.uuid;
	
	private String user_id;
	
	private String target_ids;
	
	private String access_token;

	public Follow(String user_id, List<String> target_ids, String access_token) {
		this.user_id = user_id;
		this.target_ids = StringUtils.join(target_ids, ',');
		this.access_token = access_token;
	}
	
	

	public Follow(String user_id, String target_id, String access_token) {
		super();
		this.user_id = user_id;
		this.target_ids = target_id;
		this.access_token = access_token;
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
		return HOST+"user/follow";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("target_ids", target_ids));
		params.add(new BasicNameValuePair("access_token", access_token));
		return params;
	}

}
