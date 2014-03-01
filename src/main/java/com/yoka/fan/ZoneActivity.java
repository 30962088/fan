package com.yoka.fan;

import com.yoka.fan.wiget.ZoneFragment;

import android.os.Bundle;

public class ZoneActivity extends BaseActivity{

	
	public static final String PARAM_NAME = "PARAM_NAME";
	
	public static final String PARAM_TARGET_ID = "PARAM_TARGET_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zone);
		if (savedInstanceState != null) {
            return;
        }
		ZoneFragment fragment = new ZoneFragment();
		fragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, fragment).commit();
	}
	
	@Override
	protected String getActionBarTitle() {
		String title = getIntent().getStringExtra(PARAM_NAME);
		if(title == null){
			title = "我的空间";
		}
		
		return title;
	}

}
