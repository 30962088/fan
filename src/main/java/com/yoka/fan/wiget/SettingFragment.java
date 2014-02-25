package com.yoka.fan.wiget;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.LoginActivity;
import com.yoka.fan.ModifyActivity;
import com.yoka.fan.R;
import com.yoka.fan.WebViewActivity;
import com.yoka.fan.network.Login;
import com.yoka.fan.utils.CacheManager;
import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingFragment extends Fragment implements OnClickListener{

	private TextView mSizeView;
	
	private ImageLoader imageLoader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		imageLoader = Utils.getImageLoader(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.setting_layout, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		mSizeView = (TextView) view.findViewById(R.id.cache_size);
		view.findViewById(R.id.guide).setOnClickListener(this);
		view.findViewById(R.id.login_btn).setOnClickListener(this);
		view.findViewById(R.id.user_btn).setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		getCacheSize();
		User user = User.readUser();
		if(user != null){
			login(user);
		}else{
			logout(user);
		}
		
	}
	
	private void getCacheSize(){
		new AsyncTask<Context, Void, Long>() {

			@Override
			protected Long doInBackground(Context... params) {
				// TODO Auto-generated method stub
				return CacheManager.folderSize(params[0].getCacheDir())+CacheManager.folderSize(params[0].getExternalCacheDir());
			}
			
			@Override
			protected void onPostExecute(Long result) {
				mSizeView.setText(Math.round( result/1024/1024 * 100 ) / 100.0+"M");
			}

			
		}.execute(getActivity());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guide:
			Intent intent = new Intent(getActivity(), WebViewActivity.class);
			intent.putExtra(WebViewActivity.PARAM_TITLE,"用户指南");
			intent.putExtra(WebViewActivity.PARAM_URL,"http://songaimin.fan.yoka.com/api/html/guide");
			startActivity(intent);
			break;
		case R.id.login_btn:
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		case R.id.user_btn:
			startActivity(new Intent(getActivity(),ModifyActivity.class));
			break;
			
		default:
			break;
		}
		
	}
	
	public void login(User user){
		getView().findViewById(R.id.login_frame).setVisibility(View.GONE);
		getView().findViewById(R.id.user_frame).setVisibility(View.VISIBLE);
		imageLoader.displayImage(user.photo, (ImageView)getView().findViewById(R.id.user_photo));
		((TextView)getView().findViewById(R.id.user_nickname)).setText(user.nickname);
	}
	
	public void logout(User user){
		getView().findViewById(R.id.login_frame).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.user_frame).setVisibility(View.GONE);
	}
	
}
