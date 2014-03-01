package com.yoka.fan.wiget;

import java.util.List;

import com.yoka.fan.R;
import com.yoka.fan.network.CollRecommand;
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

public class CollRecommandListFragment extends Fragment{

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RecommandListView view = new RecommandListView(getActivity());
		
		
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return view;
	}
	
	
	public static class RecommandListView extends CommonListView{

		public RecommandListView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

		public RecommandListView(
				Context context,
				com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
				com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle style) {
			super(context, mode, style);
			// TODO Auto-generated constructor stub
		}

		public RecommandListView(Context context,
				com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
			super(context, mode);
			// TODO Auto-generated constructor stub
		}

		public RecommandListView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected List<ListItemData> load(int offset, int limit) {
			CollRecommand request = new CollRecommand( offset, limit);
			request.request();
			return request.getListData();
		}

		
		
	}
	
}
