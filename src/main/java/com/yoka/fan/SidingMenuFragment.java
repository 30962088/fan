package com.yoka.fan;


import com.yoka.fan.wiget.HomeFragment;
import com.yoka.fan.wiget.SettingFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class SidingMenuFragment extends Fragment implements OnClickListener{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_nav, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		view.findViewById(R.id.login_btn).setOnClickListener(this);
		view.findViewById(R.id.setting).setOnClickListener(this);
		view.findViewById(R.id.home).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			Intent intent = new Intent(getActivity(),LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.setting:
			switchFragment(new SettingFragment());
			break;
		case R.id.home:
			switchFragment(new HomeFragment());
			break;
		default:
			break;
		}
		
	}
	
	private void switchFragment(Fragment fragment){
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
