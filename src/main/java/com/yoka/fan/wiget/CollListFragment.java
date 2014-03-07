package com.yoka.fan.wiget;


import java.util.List;


import com.yoka.fan.network.Coll;
import com.yoka.fan.network.ListItemData;
import android.os.Bundle;

public class CollListFragment extends CommonListFragment{

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
		Coll request = new Coll(offset, limit, user_id, target_id, access_token);
		request.request();
		return request.getListData();
	}



	@Override
	public String getEmptyTip() {
		// TODO Auto-generated method stub
		return "没有任何搭配信息";
	}
	
}
