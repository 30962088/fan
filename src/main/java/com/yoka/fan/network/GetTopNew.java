package com.yoka.fan.network;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yoka.fan.utils.Constant;

public class GetTopNew extends Request{

	public static final int REFRESH_CACHE = 1;
	
	public static final int REFRESH_NO_CACHE = 2;
	
	public static final int REFRESH_READ_CACHE = 0;
	
	
	private String uuid = Constant.uuid;
	
	private int limit;
	
	private int skip;
	
	private int refresh = REFRESH_NO_CACHE;
	
	private String topic_type = "";
	
	private List<ListItemData> listData;
	
	
	public GetTopNew( int skip, int limit, int refresh) {
		super();
		this.limit = limit;
		this.skip = skip;
		this.refresh = refresh;
	}

	public GetTopNew(int skip,int limit) {
		super();
		this.limit = limit;
		this.skip = skip;
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return HOST+"coll/get_top_new";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("limit", ""+limit));
		params.add(new BasicNameValuePair("skip", ""+skip));
		params.add(new BasicNameValuePair("refresh", ""+refresh));
		params.add(new BasicNameValuePair("topic_type", topic_type));
		return params;
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
	
	public List<ListItemData> getListData() {
		return listData;
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

	
	






}
