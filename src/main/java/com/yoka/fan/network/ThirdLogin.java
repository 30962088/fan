package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.yoka.fan.network.Register.Result;
import com.yoka.fan.utils.Constant;

public class ThirdLogin extends Request{

	public static class TokenInfo{
		private String access_token;
		private String data_birth_day;
		private String data_birth_month;
		private String data_birth_year;
		private String data_email;
		private String data_head;
		private String data_introduction;
		private String data_name;
		private String data_nick;
		private String data_province_code;
		private String data_sex;
		private String expires_in;
		private String openid;
		private String openkey;
		private String refresh_token;
		private String seqid;
		
		public static TokenInfo toInfo(ModelResult result,WeiboToken token) throws JSONException{
			TokenInfo info = new TokenInfo();
			JSONObject res = new JSONObject(result.getObj().toString());
			JSONObject object = res.getJSONObject("data");
			info.access_token = token.accessToken;
			info.openid = token.openID;
			info.openkey = token.omasKey;
			info.refresh_token = token.refreshToken;
			info.expires_in = String.valueOf(token.expiresIn);
			info.seqid = res.getString("seqid");
			info.data_birth_year = object.getString("birth_year");
			info.data_birth_month = object.getString("birth_month");
			info.data_birth_day = object.getString("birth_day");
			info.data_email = object.getString("email");
			info.data_head = object.getString("head");
			info.data_introduction = object.getString("introduction");
			info.data_name = object.getString("name");
			info.data_nick = object.getString("nick");
			info.data_province_code = object.getString("province_code");
			info.data_sex = object.getString("sex");
			return info;
		}
	}
	
	public static class WeiboTokenInfo{
		private String access_token;
		private String code;
		private String description;
		private String expires_in;
		private String gender;
		private String id;
		private String location;
		private String name;
		private String profile_image_url;
		private String remind_in;
		private String screen_name;
		
		public static WeiboTokenInfo toInfo(Oauth2AccessToken token,JSONObject user) throws JSONException{
			WeiboTokenInfo info = new WeiboTokenInfo();
			info.access_token = token.getToken();
			info.code = "";
			info.description = user.getString("description");
			info.expires_in =  String.valueOf(token.getExpiresTime());
			info.gender = user.getString("gender");
			info.id = user.getString("id");
			info.location = user.getString("location");
			info.name = user.getString("name");
			info.profile_image_url = user.getString("profile_image_url");
			info.remind_in = "";
			info.screen_name = user.getString("screen_name");
			return info;
		}
		
	}
	
	public static final String TYPE_TENCENT = "tecent";
	
	public static final String TYPE_SINA = "sina";
	
	private String authType;
	
	private String third_token_info;
	
	private String uuid = Constant.uuid;
	
	private String token = Constant.token;
	
	private Result result;
	
	public ThirdLogin(String authType, String third_token_info) {
		super();
		this.authType = authType;
		this.third_token_info = third_token_info;
	}
	
	

	@Override
	public void onSuccess(String data) {
		result = new Gson().fromJson(data, Result.class);
		
	}
	
	public Result getResult() {
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
		return HOST+"user/auth_third_login";
	}

	@Override
	public List<NameValuePair> fillParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("authType", authType));
		params.add(new BasicNameValuePair("third_token_info", new Gson().toJson(third_token_info)));
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("token", token));
		return params;
	}

}
