package com.yoka.fan;

import org.json.JSONException;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.yoka.fan.network.Login;
import com.yoka.fan.network.Register.Result;
import com.yoka.fan.network.ThirdLogin;
import com.yoka.fan.network.ThirdLogin.TokenInfo;
import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.ShareUtils.OnLoginListener;
import com.yoka.fan.utils.ShareUtils.OperateListener;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.utils.ShareUtils.TWeibo;
import com.yoka.fan.wiget.LoadingPopup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity2 implements OnClickListener {

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
		mWeiboAuth = new WeiboAuth(this, Constant.WEIBO_APP_KEY,
				Constant.WEIBO_REDIRECT_URL, Constant.SCOPE);
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
			onQWeiboLogin();
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

	private void onLoginSuccess(Result result) {
		User user = result.toUser();
		User.fillInfo(user);
		User.saveUser(user);
		startActivity(new Intent(LoginActivity.this,
				RecommandListActivity.class));
		MainActivity.getInstance().login(User.readUser());
	}

	private void onLogin() {
		final String username = usernameView.getText().toString();
		final String password = passwordView.getText().toString();
		LoadingPopup.show(context);
		new Thread(new Runnable() {

			@Override
			public void run() {
				new Login(username, password) {

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
				LoadingPopup.hide(context);
			}
		}).start();

	}

	private void onWeiboLogin() {
		mWeiboAuth.anthorize(new WeiboAuthListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Bundle values) {
				// 从 Bundle 中解析 Token
				Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
	            if (mAccessToken.isSessionValid()) {
	                
	                // 保存 Token 到 SharedPreferences
//	                AccessTokenKeeper.writeAccessToken(WBAuthActivity.this, mAccessToken);
	                
	            } else {
	                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
	                String code = values.getString("code");
	                String message = "签名失败";
	                if (!TextUtils.isEmpty(code)) {
	                    message = message + "\nObtained the code: " + code;
	                }
	                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	            }
				
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void onQWeiboLogin2(final WeiboToken t) {
		TWeibo weibo = new TWeibo(context);
		weibo.getUserInfo(t.accessToken, new OperateListener<ModelResult>() {

			@Override
			public void onSuccess(final ModelResult result) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						ThirdLogin request;
						try {
							request = new ThirdLogin(ThirdLogin.TYPE_TENCENT,
									TokenInfo.toInfo(result, t));
							request.request();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();

			}
		});
	}

	private void onQWeiboLogin() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				User user = User.readUser();
				final TWeibo weibo = new TWeibo(context);
				if (user == null || user.qweibo == null) {
					weibo.login(new OnLoginListener<WeiboToken>() {

						@Override
						public void onSuccess(final WeiboToken t) {
							onQWeiboLogin2(t);
						}

						@Override
						public void onError(String msg) {

						}
					});
				}else{
					onQWeiboLogin2(user.qweibo);
				}
			}
		}).start();

	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "登录";
	}

}
