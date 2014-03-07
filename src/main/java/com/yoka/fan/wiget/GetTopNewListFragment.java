package com.yoka.fan.wiget;

import java.util.List;

import com.yoka.fan.R;
import com.yoka.fan.network.GetTopNew;
import com.yoka.fan.network.ListItemData;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class GetTopNewListFragment extends Fragment{

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		GetTopNewListView view = new GetTopNewListView(getActivity());
		
		
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return view;
	}
	
	
	public static class GetTopNewListView extends CommonListView{

		public GetTopNewListView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

		public GetTopNewListView(
				Context context,
				com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
				com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle style) {
			super(context, mode, style);
			// TODO Auto-generated constructor stub
		}

		public GetTopNewListView(Context context,
				com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
			super(context, mode);
			// TODO Auto-generated constructor stub
		}

		public GetTopNewListView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected List<ListItemData> load(int offset, int limit) {
			GetTopNew request = new GetTopNew( offset, limit);
			request.request();
			return request.getListData();
		}

		@Override
		public String getEmptyTip() {
			// TODO Auto-generated method stub
			return "没有最新的搭配";
		}

		
		
	}
	
}
