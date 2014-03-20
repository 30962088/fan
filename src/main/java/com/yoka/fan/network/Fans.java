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

public  class Fans extends Request{

	private String user_id;
	
	private String target_id;
	
	private int skip;
	
	private int limit;
	
	private String uuid = Constant.uuid;
	
	
	
	public static class FansResult{
		private List<Result> results;
		private int total;
		
		public FansResult(List<Result> results, int total) {
			super();
			this.results = results;
			this.total = total;
		}
		public List<Result> getResults() {
			return results;
		}
		public int getTotal() {
			return total;
		}
	}
	
	private FansResult result;
	
	public Fans(String user_id, String target_id, int skip, int limit) {
		super();
		this.user_id = user_id;
		this.target_id = target_id;
		this.skip = skip;
		this.limit = limit;
	}
	
	public FansResult getResults() {
		return result;
	}

	@Override
	public void onSuccess(String data) {
		try {
			@SuppressWarnings("unchecked")
			JSONObject object = new JSONObject(data);
			Map<String, Result> map = (Map<String, Result>)new Gson().fromJson(object.getString("list"), new TypeToken<Map<String, Result>>(){}.getType());
			List<Result> results = new ArrayList<Result>(map.values());
			result = new FansResult(results, object.getInt("total"));
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
