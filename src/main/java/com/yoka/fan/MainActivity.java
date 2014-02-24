package com.yoka.fan;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.Constant.User;
import com.yoka.fan.wiget.HomeFragment;
import com.yoka.fan.wiget.SettingFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



public class MainActivity extends SlidingFragmentActivity {

	private Fragment mContent;
	
	private TextView mTitleView;
	
	private SidingMenuFragment menuFragment;
	
	private static MainActivity instance;
	
	public static MainActivity getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new HomeFragment();	
		setContentView(R.layout.main_layout);
		mTitleView = (TextView) findViewById(R.id.actionbar_title);
		initSlidingMenu();
		
		
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		
		
		
	
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		login(Constant.user);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	private void initSlidingMenu(){
		menuFragment = new SidingMenuFragment();
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame,menuFragment)
		.commit();
		
		findViewById(R.id.actionbar_nav).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggle();
				
			}
		});
		
	}
	
	
	
	public void switchContent(Fragment fragment) {
		if(fragment instanceof HomeFragment){
			mTitleView.setText("潮流搭配");
		}else if(fragment instanceof SettingFragment){
			mTitleView.setText("设置");
		}
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
	
	public void login(User user){
		menuFragment.login(user);
	}


}
