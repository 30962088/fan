package com.yoka.fan.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yoka.fan.network.Request;

public class ChangeHead extends Request{

	private String uuid = Constant.uuid;
	
	private String user_id;
	
	private File uploadimg;
	
	private String access_token;
	
	private String fileUrl;
	
	public ChangeHead(String user_id, File uploadimg,
			String access_token) {
		this.user_id = user_id;
		this.uploadimg = uploadimg;
		this.access_token = access_token;
	}

	@Override
	public void onSuccess(String data) {
		try {
			JSONObject object  = new JSONObject(data);
			fileUrl =  object.getString(user_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getFileUrl() {
		return fileUrl;
	}
	
	@Override
	protected Map<String, File> fillFiles() {
		Map<String, File> map = new HashMap<String, File>();
		map.put("uploadimg", uploadimg);
		return map;
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
		return HOST+"user/changeHead";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("access_token", access_token));
		return params;
	}

}
