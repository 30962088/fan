package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yoka.fan.utils.Constant;

public class UnLike extends Request{

	private String coll_id;
	
	public UnLike(String coll_id) {
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
		params.add(new BasicNameValuePair("uuid", Constant.uuid));
		params.add(new BasicNameValuePair("user_id", Constant.user.id));
		params.add(new BasicNameValuePair("coll_id", coll_id));
		params.add(new BasicNameValuePair("access_token", Constant.user.access_token));
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
