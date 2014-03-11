package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.yoka.fan.utils.Constant;

public class CollDetail extends Request{

	public static class Result{
		private Map<String, Goods> linkGoods;
		private List<Goods> linkGoodsType;
		public Map<String, Goods> getLinkGoods() {
			return linkGoods;
		}
		public List<Goods> getLinkGoodsType() {
			return linkGoodsType;
		}
		
	}
	
	public static class Goods{
		private String brand;
		private String tags;
		private String url;
		private String type_url;
		private double price;
		private String img;
		public String getBrand() {
			return brand;
		}
		public String getTags() {
			return tags;
		}
		public String getUrl() {
			return url;
		}
		public String getType_url() {
			return type_url;
		}
		public double getPrice() {
			return price;
		}
		public String getImg() {
			return img;
		}
		
	}
	
	private String uuid = Constant.uuid;
	
	private String coll_id;
	
	private Result result;
	
	public Result getResult() {
		return result;
	}
	
	public CollDetail(String coll_id) {
		this.coll_id = coll_id;
	}

	@Override
	public void onSuccess(String data) {
		result = new Gson().fromJson(data, Result.class);
		
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
		return HOST+"coll/detail";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("coll_id", coll_id));
		return params;
	}

}
