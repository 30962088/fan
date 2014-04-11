package com.yoka.fan.wiget;


import java.util.List;


import com.yoka.fan.network.CollLike;
import com.yoka.fan.network.ListItemData;
import com.yoka.fan.utils.User;
import com.yoka.fan.wiget.CommonListView.LoadResult;

import android.os.Bundle;
import android.text.TextUtils;

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
	protected LoadResult load(int offset, int limit) {
		CollLike request = new CollLike(offset, limit, user_id, target_id, access_token);
		request.request();
		List<ListItemData> list = request.getListData();
		if(list != null && TextUtils.equals(target_id, User.readUser().id)){
			for(ListItemData data:list){
				data.stared = true;
			}
		}
		
		return new LoadResult(request.getStatus(),list);
	}



	@Override
	public String getEmptyTip() {
		if(!TextUtils.equals(target_id,user_id )){
			return "TA目前还没有喜欢的搭配";
		}
		return "您目前还没有喜欢的搭配";
	}
	
}
