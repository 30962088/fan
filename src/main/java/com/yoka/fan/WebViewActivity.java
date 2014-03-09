package com.yoka.fan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends BaseActivity{

	public static final String PARAM_URL = "url";
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		WebView webView = new WebView(this);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.loadUrl(getIntent().getStringExtra(PARAM_URL));
		webView.setWebViewClient(new WebViewClient() {
	        @Override
	        public void onPageFinished(WebView view, String url) {
	            setWebTitle(view.getTitle());
	        }
	    });
		setContentView(webView);
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

}
