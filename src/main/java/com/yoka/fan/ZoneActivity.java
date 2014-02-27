package com.yoka.fan;

import com.yoka.fan.wiget.ZoneFragment;

import android.os.Bundle;

public class ZoneActivity extends BaseActivity{

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
		
		return "我的空间";
	}

}
