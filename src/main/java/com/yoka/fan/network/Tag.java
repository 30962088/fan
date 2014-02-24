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

public class Tag extends Request{

	private String uuid = Constant.uuid;
	
	private String access_token = Constant.user.access_token;
	
	private String tags;
	
	private String tag_type;
	
	private int refresh = REFRESH_NO_CACHE;
	
	private int limit;
	
	private int skip;
	
	
	private List<ListItemData> listData;
	
	
	public Tag(String tags, String tag_type, int skip,int limit) {
		super();
		this.tags = tags;
		this.tag_type = tag_type;
		this.limit = limit;
		this.skip = skip;
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

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return HOST+"coll/search";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid",uuid));
		params.add(new BasicNameValuePair("access_token	",access_token));
		params.add(new BasicNameValuePair("limit",""+limit));
		params.add(new BasicNameValuePair("refresh",""+refresh));
		params.add(new BasicNameValuePair("skip",""+skip));
		params.add(new BasicNameValuePair("tag_type",tag_type));
		params.add(new BasicNameValuePair("tags",tags));
		return params;
	}

}
