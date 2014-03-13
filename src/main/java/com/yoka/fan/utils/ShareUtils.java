package com.yoka.fan.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.UsersAPI;
import com.tencent.weibo.sdk.android.api.UserAPI;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.tencent.weibo.sdk.android.model.AccountModel;
import com.tencent.weibo.sdk.android.model.BaseVO;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;


public class ShareUtils {
	
	
	
	public static interface OperateListener<T>{
		public void onSuccess(T t);
		public void onError(String msg);
	}
	
	public static class Weibo{
		
		private WeiboAuth mWeiboAuth;
		
		private Context context;
		
		public Weibo(Context context) {
			this.context = context;
			mWeiboAuth = new WeiboAuth(context, Constant.WEIBO_APP_KEY,
					Constant.WEIBO_REDIRECT_URL, Constant.SCOPE);
		}
		
		public void getUser(Oauth2AccessToken accessToken,final OperateListener<JSONObject> operateListener){
			UsersAPI api = new UsersAPI(accessToken);
			api.show(Long.parseLong(accessToken.getUid()), new RequestListener() {
				
				@Override
				public void onIOException(IOException e) {
					Utils.tip(context, e.getMessage());
					if(operateListener != null){
						operateListener.onError(e.getMessage());
					}
				}
				
				@Override
				public void onError(WeiboException e) {
					Utils.tip(context, e.getMessage());
					if(operateListener != null){
						operateListener.onError(e.getMessage());
					}
					
				}
				
				@Override
				public void onComplete4binary(ByteArrayOutputStream responseOS) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(String response) {
					if(operateListener != null){
						try {
							operateListener.onSuccess(new JSONObject(response));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			});
		}
		
		public void login(final OperateListener<Oauth2AccessToken> onLoginListener){
			mWeiboAuth.anthorize(new WeiboAuthListener() {
				
				@Override
				public void onWeiboException(WeiboException arg0) {
					if(onLoginListener != null){
						onLoginListener.onError(arg0.getMessage());
					}
					
				}
				
				@Override
				public void onComplete(Bundle values) {
					// 从 Bundle 中解析 Token
					Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
		            if (mAccessToken.isSessionValid()) {
		               onLoginListener.onSuccess(mAccessToken);
		                
		            } else {
		                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
		                String code = values.getString("code");
		                String message = "签名失败";
		                if (!TextUtils.isEmpty(code)) {
		                    message = message + "\nObtained the code: " + code;
		                }
		                Utils.tip(context, message);
		            }
					
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
	}
	
	public static class TWeibo{
		
		private AccountModel account;
		
		private Context context;
		
		private String requestFormat = "json";
		
		public TWeibo(Context context) {
			this.context = context;
		}
		
		public void getUserInfo(String accessToken,final OperateListener<ModelResult> listener){
			AccountModel account = new AccountModel(accessToken);
			UserAPI userAPI = new UserAPI(account);
			userAPI.getUserInfo(context, requestFormat, new HttpCallback() {
				
				@Override
				public void onResult(Object object) {
					ModelResult result = (ModelResult) object;
					if(listener != null){
						listener.onSuccess(result);
					}
				}
			}, null, BaseVO.TYPE_JSON);
		}
		
		public void login(final OperateListener<WeiboToken> listener){
			
			AuthHelper.register(context, Constant.TWEIBO_APP_KEY, Constant.TWEIBO_APP_SECRET, new OnAuthListener() {

				//如果当前设备没有安装腾讯微博客户端，走这里
				@Override
				public void onWeiBoNotInstalled() {
					Intent i = new Intent(context,Authorize.class);
					context.startActivity(i);
				}

				//如果当前设备没安装指定版本的微博客户端，走这里
				@Override
				public void onWeiboVersionMisMatch() {
					Intent i = new Intent(context,Authorize.class);
					context.startActivity(i);
				}

				//如果授权失败，走这里
				@Override
				public void onAuthFail(int result, String err) {
					Utils.tip(context,  "result : " + err);
					if(listener != null){
						listener.onError(err);
					}
					AuthHelper.unregister(context);
				}

				//授权成功，走这里
				//授权成功后，所有的授权信息是存放在WeiboToken对象里面的，可以根据具体的使用场景，将授权信息存放到自己期望的位置，
				//在这里，存放到了applicationcontext中
				@Override
				public void onAuthPassed(String name, WeiboToken token) {
					if(listener != null){
						listener.onSuccess(token);
					}
					
					AuthHelper.unregister(context);
				}
			});
			AuthHelper.auth(context, "");
		}
	}

	
	
}
