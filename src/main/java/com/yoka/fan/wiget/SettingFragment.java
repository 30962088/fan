package com.yoka.fan.wiget;

import com.yoka.fan.R;
import com.yoka.fan.WebViewActivity;
import com.yoka.fan.utils.CacheManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingFragment extends Fragment implements OnClickListener{

	private TextView mSizeView;
	
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
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		getCacheSize();
		
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

		default:
			break;
		}
		
	}
	
}
