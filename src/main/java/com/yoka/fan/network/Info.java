package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.yoka.fan.utils.Constant;

public class Info extends Request{
	
	public static class OtherInfo{
		private BaseInfo base_info;
		public BaseInfo getBase_info() {
			return base_info;
		}
	}
	
	public static class BaseInfo{
		private String birthday;
		private String sex;
		private String job;
		public String getBirthday() {
			return birthday;
		}
		public String getSex() {
			return sex;
		}
		public String getJob() {
			return job;
		}
		
	}
	
	public static class Result{
		private String _id;
		private String follows;
		private String followers;
		private String show_specials;
		private String show_count;
		private String head_url;
		private String nick;
		private OtherInfo other_info;
		public String getFollowers() {
			return followers;
		}
		public String getFollows() {
			return follows;
		}
		public String getId() {
			return _id;
		}
		public String getShow_specials() {
			return show_specials;
		}
		public String getHead_url() {
			return IMG_HOST+head_url;
		}
		public String getHeadUrl() {
			return head_url;
		}
		public String getNick() {
			return nick;
		}
		public String getShow_count() {
			return show_count;
		}
		public OtherInfo getOther_info() {
			return other_info;
		}
	}
	

	private String access_token;
	
	private String target_ids;
	
	private String user_id;
	
	private String uuid = Constant.uuid;
	
	
	
	
	public Info(String access_token, String user_id,String target_ids) {
		this.access_token = access_token;
		this.target_ids = target_ids;
		this.user_id = user_id;
	}
	
	public Info(String access_token, String user_id,String[] target_ids) {
		this.access_token = access_token;
		this.target_ids = StringUtils.join(target_ids, ',');
		this.user_id = user_id;
	}
	
	
	public void onSuccess(Map<String, Result> result){};

	
	private Map<String, Result> result;
	
	@Override
	public void onSuccess(String data) {
		result = (Map<String, Result>)new Gson().fromJson(data, new TypeToken<Map<String, Result>>(){}.getType());
		onSuccess(result);
		
	}
	
	public Map<String, Result> getResult() {
		return result;
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
		return HOST+"user/info";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", access_token));
		params.add(new BasicNameValuePair("target_ids", target_ids));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("uuid", uuid));
		return params;
	}
	

}
