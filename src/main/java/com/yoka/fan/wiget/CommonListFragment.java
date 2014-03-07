package com.yoka.fan.wiget;

import java.util.List;

import com.yoka.fan.network.ListItemData;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public abstract class CommonListFragment extends Fragment{


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		CListView view = new CListView(getActivity());
		
		
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return view;
	}
	
	
	private class CListView extends CommonListView{

		public CListView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

		public CListView(
				Context context,
				com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
				com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle style) {
			super(context, mode, style);
			// TODO Auto-generated constructor stub
		}

		public CListView(Context context,
				com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
			super(context, mode);
			// TODO Auto-generated constructor stub
		}

		public CListView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected List<ListItemData> load(int offset, int limit) {
			
			return CommonListFragment.this.load(offset, limit);
		}

		@Override
		public String getEmptyTip() {
			// TODO Auto-generated method stub
			return CommonListFragment.this.getEmptyTip();
		}

		
		
	}
	
	protected abstract List<ListItemData> load(int offset, int limit);


	protected abstract String getEmptyTip();
	
}
