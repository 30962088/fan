package com.yoka.fan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends BaseActivity{

	public static final String PARAM_URL = "url";
	
	private WebView webView;
	
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.webview_layout);
		webView = (WebView) findViewById(R.id.webview);
		final View loading = findViewById(R.id.loading);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.loadUrl(getIntent().getStringExtra(PARAM_URL));
		webView.setWebViewClient(new WebViewClient() {
	        @Override
	        public void onPageFinished(WebView view, String url) {
	        	loading.setVisibility(View.GONE);
	            setWebTitle(view.getTitle());
	        }
	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        	loading.setVisibility(View.VISIBLE);
	        	super.onPageStarted(view, url, favicon);
	        }
	    });
	}
	
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "";
	}
	
	public static void open(Context context,String url){
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra(WebViewActivity.PARAM_URL,url);
		context.startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(webView.canGoBack()){
				webView.goBack();
			}else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
