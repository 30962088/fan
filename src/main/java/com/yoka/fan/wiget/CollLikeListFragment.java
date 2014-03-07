package com.yoka.fan.wiget;


import java.util.List;


import com.yoka.fan.network.Coll;
import com.yoka.fan.network.CollLike;
import com.yoka.fan.network.ListItemData;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class CollLikeListFragment extends Fragment{

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Bundle arguments =  getArguments();
		
		CollListView view = new CollListView(getActivity(),arguments.getString("user_id"),arguments.getString("target_id"),arguments.getString("access_token"));
		
		
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return view;
	}
	
	
	public class CollListView extends CommonListView{

		private String user_id;
		
		private String target_id;
		
		private String access_token;
		

		

		public CollListView(Context context,
				String user_id, String target_id, String access_token) {
			super(context);
			this.user_id = user_id;
			this.target_id = target_id;
			this.access_token = access_token;
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
	
}
