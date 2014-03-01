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
import com.yoka.fan.utils.Constant;

public class CollRecommand extends Request{

	private String uuid = Constant.uuid;
	
	private int skip;
	
	private int limit;
	
	private int refresh = 2;
	
	private List<ListItemData> listData;
	
	public List<ListItemData> getListData() {
		return listData;
	}
	
	public CollRecommand(int skip, int limit) {
		super();
		this.skip = skip;
		this.limit = limit;
	}

	@Override
	public void onSuccess(String data) {
		try {
			data = new JSONObject(data).getString("list");
			Type listType = new TypeToken<List<ListItemData>>() {}.getType();
			Gson gson = new Gson();
			listData = gson.fromJson(data,listType);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
		return HOST+"coll/recommend";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("refresh", ""+refresh));
		params.add(new BasicNameValuePair("skip", ""+skip));
		params.add(new BasicNameValuePair("limit", ""+limit));
		return params;
	}

}
