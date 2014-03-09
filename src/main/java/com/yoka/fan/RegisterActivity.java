package com.yoka.fan;




import com.yoka.fan.network.Register;
import com.yoka.fan.utils.User;

import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.LoadingPopup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements OnClickListener{
	
	private EditText mNameText;
	
	private EditText mEmailText;
	
	private EditText mPasswordText;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.register_layout);
	
		
		mNameText = (EditText) findViewById(R.id.username);
		
		mEmailText = (EditText) findViewById(R.id.email);
		
		mPasswordText = (EditText) findViewById(R.id.password);
		
		findViewById(R.id.create_btn).setOnClickListener(this);
		
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.create_btn:
			register();
			break;

		default:
			break;
		}
		
	}
	
	private void register(){
		final String username = mNameText.getText().toString();
		final String email = mEmailText.getText().toString();
		final String password = mPasswordText.getText().toString();
		if(validate(username,email,password)){
			LoadingPopup.show(this);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					new Register(username, email, password){
						public void onError(int code, final String msg) {
							error(msg);
						};
						@Override
						protected void onSuccess(Result result) {
							User user = result.toUser();
							User.fillInfo(user);
							User.saveUser(user);
							error("注册成功");
							startActivity(new Intent(RegisterActivity.this,RecommandListActivity.class));
							
						};
						
					}.request();
					LoadingPopup.hide(RegisterActivity.this);
				}
			}).start();
			
		}
	}
	
	private boolean validate(String username,String email,String password){
		if(username.length() < 2 || username.length() > 20){
			error("用户名应该为2-20位字符");
			return false;
		}
		if(!Utils.isValidEmailAddress(email)){
			error("email格式错误");
			return false;
		}
		
		if(password.length() < 2 || password.length() > 20){
			error("密码应该为6-16位字符");
			return false;
		}
		
		return true;
	}
	
	private void error(final String str){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
				
			}
			
		});
		
	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "注册";
	}
	
	
	
}
