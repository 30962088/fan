package com.yoka.fan.wiget;

import java.util.List;

import com.yoka.fan.network.CollFollow;
import com.yoka.fan.network.ListItemData;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class CollFollowListFragment extends CommonListFragment{

	public static final String PARAM_USER_ID = "PARAM_USER_ID";
	
	public static final String PARAM_USER_ACCESS_TOKEN = "PARAM_USER_ACCESS_TOKEN";
	
	private String user_id;
	
	private String access_token;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user_id = getArguments().getString(PARAM_USER_ID);
		access_token = getArguments().getString(PARAM_USER_ACCESS_TOKEN);
		
	}

	
	@Override
	protected List<ListItemData> load(int offset, int limit) {
		CollFollow request = new CollFollow( offset, limit,access_token,user_id);
		request.request();
		return request.getListData();
	}

	@Override
	public String getEmptyTip() {
		// TODO Auto-generated method stub
		return "您目前还没有关注任何人";
	}
	
}
