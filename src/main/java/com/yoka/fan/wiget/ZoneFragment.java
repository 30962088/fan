package com.yoka.fan.wiget;


import com.yoka.fan.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ZoneFragment extends Fragment{

	private TextView matchView;
	
	private TextView focusView;
	
	private TextView fansView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.zone_header, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		matchView = (TextView) view.findViewById(R.id.match);
		focusView = (TextView) view.findViewById(R.id.focus);
		fansView = (TextView) view.findViewById(R.id.fans);
	}
	
	
	
}
