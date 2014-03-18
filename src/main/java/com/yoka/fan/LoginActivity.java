package com.yoka.fan;

import java.net.Authenticator.RequestorType;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.yoka.fan.network.Login;
import com.yoka.fan.network.Request;
import com.yoka.fan.network.Register.Result;
import com.yoka.fan.network.ThirdLogin;
import com.yoka.fan.network.ThirdLogin.TokenInfo;
import com.yoka.fan.network.ThirdLogin.WeiboTokenInfo;
import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.ShareUtils.OperateListener;
import com.yoka.fan.utils.ShareUtils.Weibo;
import com.yoka.fan.utils.ShareAccount;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.utils.ShareUtils.TWeibo;
import com.yoka.fan.utils.ShareAccount.SINAToken;
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

	
	private static final int REQ_REGISTER = 1;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register:
			startActivityForResult(new Intent(this, RegisterActivity.class),REQ_REGISTER);
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

	private void onLoginSuccess(final Result result) {

		User user = result.toUser();
		User.fillInfo(user);
		User.saveUser(user);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(LoginActivity.this,
						RecommandListActivity.class));
				MainActivity.getInstance().login(User.readUser());
				finish();

			}
		});

	}

	private boolean validate(String username,String password){
		if(TextUtils.isEmpty(username)){
			Utils.tip(context, "请输入用户名");
			return false;
		}
		if(password.length() <6 || password.length() > 16){
			Utils.tip(context, "密码应该为6-16位字符");
			return false;
		}
		return true;
	}
	
	private void onLogin() {
		final String username = usernameView.getText().toString();
		final String password = passwordView.getText().toString();
		if(!validate(username, password)){
			return;
		}
		LoadingPopup.show(context);
		new Thread(new Runnable() {

			@Override
			public void run() {
				new Login(username, password) {

					@Override
					protected void onSuccess(final Result result) {
						onLoginSuccess(result);

					}

					public void onError(int code, String msg) {

						Utils.tip(context, msg);

					};
				}.request();
				LoadingPopup.hide(context);
			}
		}).start();

	}

	private void onThirdLogin(final ThirdLogin login) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				login.request();
				if (login.getStatus() == Request.Status.SUCCESS) {
					onLoginSuccess(login.getResult());
				}
				LoadingPopup.hide(context);
			}
		}).start();
	}

	private void onWeiboLogin2(final SINAToken token) {
		Weibo weibo = new Weibo(context);
		weibo.getUser(token, new OperateListener<JSONObject>() {

			@Override
			public void onSuccess(final JSONObject user) {
				LoadingPopup.show(context);
				try {
					ThirdLogin request = new ThirdLogin(ThirdLogin.TYPE_SINA,
							new Gson().toJson(WeiboTokenInfo
									.toInfo(token, user)));
					onThirdLogin(request);
				} catch (JSONException e1) {
					LoadingPopup.hide(context);
					e1.printStackTrace();
				}

			}

			@Override
			public void onError(String msg) {
				LoadingPopup.hide(context);

			}
		});
	}

	private void onWeiboLogin() {
		
		Weibo weibo = new Weibo(context);
		ShareAccount account = ShareAccount.read();
		if (account == null || account.weibo == null
				|| !account.weibo.toOauth2AccessToken().isSessionValid()) {
			weibo.login(new OperateListener<SINAToken>() {

				@Override
				public void onSuccess(SINAToken t) {
					onWeiboLogin2(t);

				}

				@Override
				public void onError(String msg) {
					LoadingPopup.hide(context);

				}
			});
		} else {
			onWeiboLogin2(account.weibo);
		}

	}

	private void onQWeiboLogin2(final WeiboToken t) {

		TWeibo weibo = new TWeibo(context);
		weibo.getUserInfo(t.accessToken, new OperateListener<ModelResult>() {

			@Override
			public void onSuccess(final ModelResult result) {
				LoadingPopup.show(context);
				try {
					ThirdLogin request = new ThirdLogin(
							ThirdLogin.TYPE_TENCENT, new Gson()
									.toJson(TokenInfo.toInfo(result, t)));
					onThirdLogin(request);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onError(String msg) {
				LoadingPopup.hide(context);

			}
		});

	}

	private void onQWeiboLogin() {
		
		ShareAccount account = ShareAccount.read();
		final TWeibo weibo = new TWeibo(context);
		if (account == null || account.qweibo == null) {
			weibo.login(new OperateListener<WeiboToken>() {

				@Override
				public void onSuccess(final WeiboToken t) {
					onQWeiboLogin2(t);
				}

				@Override
				public void onError(String msg) {
					LoadingPopup.hide(context);
				}
			});
		} else {
			onQWeiboLogin2(account.qweibo);
		}

	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "登录";
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		if(reqCode == REQ_REGISTER && resultCode == RESULT_OK){
			finish();
		}
	}
	

}
