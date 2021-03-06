package com.yoka.fan;


import com.nostra13.universalimageloader.core.ImageLoader;

import com.umeng.analytics.MobclickAgent;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.HomeFragment;
import com.yoka.fan.wiget.PhotoView;
import com.yoka.fan.wiget.SettingFragment;
import com.yoka.fan.wiget.ZoneFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SidingMenuFragment extends Fragment implements OnClickListener{
	
	private ImageLoader imageLoader;
	
	private View zoneBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		imageLoader = Utils.getImageLoader(getActivity());
	}
	
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
		zoneBtn = view.findViewById(R.id.zone);
		zoneBtn.setOnClickListener(this);
		view.findViewById(R.id.home).setOnClickListener(this);
		view.findViewById(R.id.camera_btn).setOnClickListener(this);
		view.findViewById(R.id.user_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			Intent intent = new Intent(getActivity(),LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.user_btn:
			startActivity(new Intent(getActivity(), ModifyActivity.class));
			break;
		case R.id.setting:
			switchFragment(new SettingFragment());
			break;
		case R.id.home:
			switchFragment(new HomeFragment());
			break;
		case R.id.camera_btn:
			Activity activity = getActivity();
			if(activity instanceof MainActivity){
				((MainActivity)activity).openShare();
			}
			break;
		case R.id.zone:
			MobclickAgent.onEvent(getActivity(),"myhome");
			if(User.readUser() == null){
				startActivity(new Intent(getActivity(),LoginActivity.class));
			}else{
				ZoneFragment fragment = new ZoneFragment();
				Bundle arguments =  new Bundle();
				arguments.putString(ZoneActivity.PARAM_TARGET_ID, User.readUser().id);
				fragment.setArguments(arguments);
				switchFragment(fragment);
			}
			
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
	
	public void login(User user){
		getView().findViewById(R.id.login_btn).setVisibility(View.GONE);
		getView().findViewById(R.id.user_btn).setVisibility(View.VISIBLE);
//		((PhotoView)getView().findViewById(R.id.user_photo)).setVipSize((int)getResources().getDimension(R.dimen.vip_20));
		if(!TextUtils.isEmpty(user.photo)){
			
			imageLoader.displayImage(user.photo, ((ImageView)getView().findViewById(R.id.user_photo)));
		}
		
		((TextView)getView().findViewById(R.id.user_name)).setText(user.nickname);
	}
	
	public void logout(){
		getView().findViewById(R.id.login_btn).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.user_btn).setVisibility(View.GONE);
	}

}
