package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yoka.fan.utils.Constant;

public class Like extends Request{

	
	
	private String user_id;
	private String access_token;
	private String coll_id;
	private String uuid = Constant.uuid;
	
	
	
	

	public Like(String user_id, String access_token, String coll_id) {
		super();
		this.user_id = user_id;
		this.access_token = access_token;
		this.coll_id = coll_id;
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
		return HOST+"coll/like";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("coll_id", coll_id));
		params.add(new BasicNameValuePair("access_token", access_token));
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
