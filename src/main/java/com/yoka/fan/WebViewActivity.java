package com.yoka.fan;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebViewActivity extends BaseActivity{

	public static final String PARAM_TITLE = "title";
	
	public static final String PARAM_URL = "url";
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		WebView webView = new WebView(this);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.loadUrl(getIntent().getStringExtra(PARAM_URL));
		setContentView(webView);
	}
	
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return getIntent().getStringExtra(PARAM_TITLE);
	}

}
