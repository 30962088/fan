package com.yoka.fan.network;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.RelativeDateFormat;

public class CommentList extends Request{

	public static class Result{
		
		private String _id;
		
		private String creator_id;
		
		private long utime;
		
		private String u_thumb;
		
		private String u_nick;
		
		private String txt;
		
		public String get_id() {
			return _id;
		}
		
		public String getCreator_id() {
			return creator_id;
		}

		public long getUtime() {
			return utime;
		}

		public String getU_thumb() {
			return u_thumb;
		}

		public String getU_nick() {
			return u_nick;
		}
		
		public String getDateFormater(){
			return RelativeDateFormat.format(new Date(utime));
		}

		public String getTxt() {
			return txt;
		}
		
		
		
	}
	
	private String uuid = Constant.uuid;
	
	private String user_id;
	
	private String access_token;
	
	private String coll_id;
	
	private int skip;
	
	private int limit;
	
	private String goods_id = "";
	
	private List<Result> results;
	
	public CommentList(String user_id, String access_token, String coll_id,
			int skip, int limit) {
		super();
		this.user_id = user_id;
		this.access_token = access_token;
		this.coll_id = coll_id;
		this.skip = skip;
		this.limit = limit;
	}
	
	public List<Result> getResults() {
		return results;
	}

	@Override
	public void onSuccess(String data) {
		
		try {
			results = new Gson().fromJson(new JSONObject(data).getString("list"), new TypeToken<List<Result>>() {}.getType());
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return HOST+"comm/getlist";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("access_token", access_token));
		params.add(new BasicNameValuePair("coll_id", ""));
		params.add(new BasicNameValuePair("skip", ""+skip));
		params.add(new BasicNameValuePair("limit", ""+limit));
		params.add(new BasicNameValuePair("goods_id", ""));
		return params;
	}

}
