package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.yoka.fan.SelectCategoryActivity.Model;
import com.yoka.fan.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseSelectActivity extends FragmentActivity{

	public static final String PARAM_IMG_PATH = "PARAM_IMG_PATH";
	
	public static final String PARAM_SELECTED_LIST = "PARAM_SELECTED_LIST";
	
	
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
		super.setContentView(R.layout.base_select_layout);
		
		((ViewGroup)findViewById(R.id.content_view)).addView(view);
		
		((TextView)findViewById(R.id.prev_text)).setText(getPrevText());
		
		((TextView)findViewById(R.id.next_text)).setText(getNextText());
		
		getPrevView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onPrevClick();
			}
		});
		
		getNextView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onNextClick();
				
			}
		});
		
		Utils.getImageLoader(this).displayImage(getIntent().getStringExtra(PARAM_IMG_PATH), (ImageView)findViewById(R.id.base_img));
		
		
	}
	@Override
	public void setContentView(View view) {
		setContentView(view,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	protected abstract String getPrevText();
	
	protected abstract String getNextText();
	
	protected abstract void onPrevClick();
	
	protected abstract void onNextClick();
	
	protected void setNextEnable(boolean enable){
		ViewGroup group = ((ViewGroup)getNextView());
		group.setEnabled(enable);
		for(int i = 0;i<group.getChildCount();i++){
			group.getChildAt(i).setEnabled(enable);
		}
	}
	
	protected View getPrevView(){
		return findViewById(R.id.base_prev);
	}
	
	protected View getNextView(){
		return findViewById(R.id.base_next);
	}
}
