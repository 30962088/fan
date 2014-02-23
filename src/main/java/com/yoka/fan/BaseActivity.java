package com.yoka.fan;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseActivity extends FragmentActivity{

	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		
		setContentView(LayoutInflater.from(this).inflate(layoutResID,null));
		
	}
	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(R.layout.base_activity_layout);
		
		((ViewGroup)findViewById(R.id.content_view)).addView(view);
		
		((TextView)findViewById(R.id.actionbar_title)).setText(getActionBarTitle());
		
		findViewById(R.id.actionbar_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		
	}
	@Override
	public void setContentView(View view) {
		setContentView(view,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	protected abstract String getActionBarTitle();
	
	
	protected ImageView getActionbarRight(){
		return (ImageView) findViewById(R.id.actionbar_right);
	}
	
	protected ImageView getActionbarLeft(){
		return (ImageView) findViewById(R.id.actionbar_left);
	}
	
}
