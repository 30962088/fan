package com.yoka.fan.wiget;


import java.util.List;


import com.yoka.fan.network.CollLike;
import com.yoka.fan.network.ListItemData;

import android.os.Bundle;

public class CollLikeListFragment extends CommonListFragment{

	private String user_id;
	
	private String target_id;
	
	private String access_token;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments =  getArguments();
		user_id = arguments.getString("user_id");
		target_id = arguments.getString("target_id");
		access_token =arguments.getString("access_token");
		
	}
	
	

	@Override
	protected List<ListItemData> load(int offset, int limit) {
		CollLike request = new CollLike(offset, limit, user_id, target_id, access_token);
		request.request();
		return request.getListData();
	}



	@Override
	public String getEmptyTip() {
		// TODO Auto-generated method stub
		return "您喜欢的人还没有任何搭配信息";
	}
	
}
