package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yoka.fan.network.Info.Result;
import com.yoka.fan.utils.Constant;

public abstract class Fans extends Request{

	private String user_id;
	
	private String target_id;
	
	private int skip;
	
	private int limit;
	
	private String uuid = Constant.uuid;
	
	
	
	public Fans(String user_id, String target_id, int skip, int limit) {
		super();
		this.user_id = user_id;
		this.target_id = target_id;
		this.skip = skip;
		this.limit = limit;
	}
	
	protected abstract void onSuccess(List<Result> list);

	@Override
	public void onSuccess(String data) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Result> map = (Map<String, Result>)new Gson().fromJson(new JSONObject(data).getString("list"), new TypeToken<Map<String, Result>>(){}.getType());
			onSuccess(new ArrayList<Result>(map.values()));
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
		return HOST+"user/fans";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("target_id", target_id));
		params.add(new BasicNameValuePair("skip", ""+skip));
		params.add(new BasicNameValuePair("limit", ""+limit));
		params.add(new BasicNameValuePair("uuid",uuid));
		return params;
	}

}
