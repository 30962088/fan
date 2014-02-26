package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yoka.fan.utils.Constant;

public class Info extends Request{

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
