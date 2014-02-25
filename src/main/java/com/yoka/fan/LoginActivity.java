package com.yoka.fan;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yoka.fan.network.Login;
import com.yoka.fan.network.LoginThird;
import com.yoka.fan.network.LoginThird.Info;
import com.yoka.fan.network.Register.Result;
import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity2 implements OnClickListener {

	private QQAuth mQQAuth;
	private Tencent mTencent;
	
	private WeiboAuth mWeiboAuth;

	private Context context;
	
	private EditText usernameView;
	
	private EditText passwordView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.context = this;
		setContentView(R.layout.login_layout);
		
		mWeiboAuth = new WeiboAuth(this, Constant.APP_KEY, Constant.REDIRECT_URL, Constant.SCOPE);
		mQQAuth = QQAuth.createInstance("222222", this);
		mTencent = Tencent.createInstance("222222", this);
		findViewById(R.id.login_qq_btn).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
		findViewById(R.id.login_weibo_btn).setOnClickListener(this);
		findViewById(R.id.login_btn).setOnClickListener(this);
		usernameView = (EditText) findViewById(R.id.username);
		passwordView = (EditText) findViewById(R.id.password);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;

		case R.id.login_qq_btn:
			onQQClickLogin();
			break;
		case R.id.login_weibo_btn:
			onWeiboLogin();
			break;
		case R.id.login_btn:
			onLogin();
			break;
		default:
			break;
		}

	}
	
	private void onLoginSuccess(Result result){
		User.saveUser(result.toUser());
		finish();
		MainActivity.getInstance().login(User.readUser());
	}
	
	private void onLogin() {
		final String username = usernameView.getText().toString();
		final String password = passwordView.getText().toString();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new Login(username,password) {
					
					@Override
					protected void onSuccess(final Result result) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								onLoginSuccess(result);
								
							}
						});
						
						
					}
					public void onError(int code, String msg) {
						
						Utils.tip(context, msg);
						
					};
				}.request();
				
			}
		}).start();
		
	}
	
	

	private void onWeiboLogin(){
		mWeiboAuth.anthorize(new WeiboAuthListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				Log.d("zzm", "exception:"+arg0);
				
			}
			
			@Override
			public void onComplete(Bundle values) {
				Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
	            if (mAccessToken.isSessionValid()) {
	               
	            } else {
	                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
	                String code = values.getString("code");
	                String message = "微博应用签名失败";
	                if (!TextUtils.isEmpty(code)) {
	                    message = message + "\nObtained the code: " + code;
	                }
	                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
	            }
				
			}
			
			@Override
			public void onCancel() {
				Log.d("zzm", "cancel");
				
			}
		});
	}

	private void onQQClickLogin() {

		if (!mQQAuth.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onComplete(Object response) {
					final Info info = new Info();
					JSONObject json = (JSONObject) response;
					try {
						info.access_token= json.getString("access_token");
						info.openid = json.getString("openid");
						info.expires_in = json.getString("expires_in");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					UserInfo userInfo = new UserInfo(context, mQQAuth.getQQToken());
					userInfo.getUserInfo(new IUiListener() {
						
						@Override
						public void onError(UiError arg0) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onComplete(Object response) {
							JSONObject json = (JSONObject)response;
							try {
								info.data_head = json.getString("figureurl_qq_2");
								info.data_sex = json.getString("gender");
								info.data_nick = json.getString("nickname");
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										LoginThird loginThird = new LoginThird(LoginThird.TYPE_TENCENT, info);
										loginThird.request();
										System.out.println(loginThird.getData());
										
									}
								}).start();
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						
						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							
						}
					});

				}

				@Override
				public void onError(UiError error) {
					// TODO Auto-generated method stub

				}
			};
			// mQQAuth.login(this, "all", listener);
			mTencent.loginWithOEM(this, "all", listener, "10000144",
					"10000144", "xxxx");
		} else {

		}
	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "登录";
	}

}
