package com.yoka.fan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yoka.fan.R;

public class LauncherActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher_layout);
		new Handler(getMainLooper()).postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finish();
				
			}
		}, 2000);
	}
	
}
