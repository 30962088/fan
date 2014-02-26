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

public class Coll extends Request{

	private String uuid = Constant.uuid;
	
	private int skip;
	
	private int limit;
	
	private String user_id;
	
	private String target_id;
	
	private String access_token;
	
	private List<ListItemData> listData;
	
	
	public Coll(int skip, int limit, String user_id, String target_id,
			String access_token) {
		this.skip = skip;
		this.limit = limit;
		this.user_id = user_id;
		this.target_id = target_id;
		this.access_token = access_token;
	}

	@Override
	public void onSuccess(String data) {
		try {
			data = new JSONObject(data).getString("list");
			Type listType = new TypeToken<List<ListItemData>>() {}.getType();
			Gson gson = new Gson();
			listData = gson.fromJson(data,listType);
			for(ListItemData item:listData){
				item.owner_name = null;
				item.owner_face = null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return HOST+"coll/colls";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("skip", ""+skip));
		params.add(new BasicNameValuePair("limit", ""+limit));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("target_id", target_id));
		params.add(new BasicNameValuePair("access_token",access_token));
		return params;
	}

}
