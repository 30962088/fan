package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.yoka.fan.utils.Category;
import com.yoka.fan.utils.Constant;

public class CategoryRequest extends Request{

	private String uuid = Constant.uuid;
	
	private int refresh = 1;
	
	private Category category;
	
	@Override
	public void onSuccess(String data) {
		
		category = new Gson().fromJson(data, Category.class);
		
	}
	

	
	public Category getCategory() {
		return category;
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
		return HOST+"good/syc_cata_brand_color";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("refresh", ""+refresh));
		return params;
	}

}
