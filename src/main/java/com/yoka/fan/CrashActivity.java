package com.yoka.fan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class CrashActivity extends BaseActivity{

	public static final String PARAM_LOG = "PARAM_LOG";
	
	public static void open(Context context,String log){
		Intent intent = new Intent(context, CrashActivity.class);
		intent.putExtra(PARAM_LOG, log);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	private String log;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		log = getIntent().getStringExtra(PARAM_LOG);
		WebView webView = new WebView(this);
		
		webView.loadData(log, "text/html", "utf-8");
		
		setContentView(webView);
		
	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "程序崩溃了，请全选复制，利用QQ同步到PC，发送给我";
	}
	
}
