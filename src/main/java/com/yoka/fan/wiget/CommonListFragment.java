package com.yoka.fan.wiget;

import java.util.List;

import com.yoka.fan.network.ListItemData;
import com.yoka.fan.wiget.CommonListView.LoadResult;
import com.yoka.fan.wiget.CommonListView.OnVerticalScrollListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public abstract class CommonListFragment extends Fragment{


	private CListView view;
	
	private OnVerticalScrollListener onVerticalScrollListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = new CListView(getActivity());
		
		
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		view.setOnVerticalScrollListener(onVerticalScrollListener);
		
		return view;
	}
	
	
	public void setOnVerticalScrollListener(
			OnVerticalScrollListener onVerticalScrollListener) {
		this.onVerticalScrollListener = onVerticalScrollListener;
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
		protected LoadResult load(int offset, int limit) {
			
			return CommonListFragment.this.load(offset, limit);
		}

		@Override
		public String getEmptyTip() {
			// TODO Auto-generated method stub
			return CommonListFragment.this.getEmptyTip();
		}

		
		
	}
	
	public void refresh(){
		view.setRefreshing(true);
	}
	
	protected abstract LoadResult load(int offset, int limit);


	protected abstract String getEmptyTip();
	
}
