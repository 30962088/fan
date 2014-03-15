package com.yoka.fan.network;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yoka.fan.network.Info.Result;
import com.yoka.fan.utils.Constant;

public abstract class Recommand extends Request{

	private int num = 12;
	
	private String uuid = Constant.uuid;
	
	public abstract void onSuccess(List<Result> result);
	
	@Override
	public void onSuccess(String data) {
		try {
			String list = new JSONObject(data).getString("list");
			onSuccess((List<Result>) new Gson().fromJson(list, new TypeToken<List<Result>>() {}.getType()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
		return HOST+"user/recommend";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("num", ""+num));
		params.add(new BasicNameValuePair("uuid", uuid));
		return params;
	}

}
