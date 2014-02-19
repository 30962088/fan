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

public class RegisterActivity extends BaseActivity{
	
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.register_layout);
		((TextView)findViewById(R.id.actionbar_title)).setText("注册");
		findViewById(R.id.actionbar_close).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		
		
	}
	
	
	
}
