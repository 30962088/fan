package com.yoka.fan;



import org.json.JSONObject;

import com.tencent.connect.auth.QQAuth;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity{
	
	private QQAuth mQQAuth;
	private Tencent mTencent;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.login_layout);
		((TextView)findViewById(R.id.actionbar_title)).setText("登录");
		findViewById(R.id.actionbar_close).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		mQQAuth = QQAuth.createInstance("222222", this);
		mTencent = Tencent.createInstance("222222", this);
		findViewById(R.id.login_qq_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onQQClickLogin();
				
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
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onError(UiError error) {
					// TODO Auto-generated method stub
					
				}
			};
			//mQQAuth.login(this, "all", listener);
			mTencent.loginWithOEM(this, "all", listener,"10000144","10000144","xxxx");
		} else {
			
		}
	}
	
	
}
