package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.User;

public class Tickling extends Request{

	private String uuid = Constant.uuid;
	
	private String user_id;
	
	private String content;
	
	private String contact;
	
	
	
	public Tickling(String content, String contact) {
		super();
		User user = User.readUser();
		if(user != null){
			user_id = user.id;
		}
		this.content = content;
		this.contact = contact;
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
		return HOST+"other/tickling";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("contact", contact));
		if(user_id != null){
			params.add(new BasicNameValuePair("user_id", user_id));
		}
		return params;
	}

}
