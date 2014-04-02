package com.yoka.fan.wiget;


import java.util.List;


import com.yoka.fan.network.Coll;
import com.yoka.fan.network.ListItemData;
import com.yoka.fan.utils.User;
import com.yoka.fan.wiget.CommonListView.LoadResult;

import android.os.Bundle;
import android.text.TextUtils;

public class CollListFragment extends CommonListFragment{

	private String user_id;
	
	private String target_id;
	
	private String access_token;
	
	public CollListFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments =  getArguments();
		user_id = arguments.getString("user_id");
		target_id = arguments.getString("target_id");
		access_token =arguments.getString("access_token");
		
	}
	
	
	@Override
	protected LoadResult load(int offset, int limit) {
		Coll request = new Coll(offset, limit, user_id, target_id, access_token);
		request.request();
		return new LoadResult(request.getStatus(),request.getListData());
	}



	@Override
	public String getEmptyTip() {
		if(!TextUtils.equals(target_id, user_id)){
			return "TA还没有任何搭配";
		}
		return "没有任何搭配信息";
	}
	
}
