package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yoka.fan.utils.Constant;

public class CollDel extends Request{

	private String uuid = Constant.uuid;
	
	private String coll_id;
	
	private String user_id;
	
	private String access_token;
	
	
	
	public CollDel(String coll_id, String user_id, String access_token) {
		super();
		this.coll_id = coll_id;
		this.user_id = user_id;
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
		return HOST+"coll/del";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("coll_id", coll_id));
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("access_token", access_token));
		return params;
	}

}
