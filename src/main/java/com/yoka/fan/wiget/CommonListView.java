package com.yoka.fan.wiget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yoka.fan.R;

public abstract class CommonListView extends PullToRefreshListView{

	public static class Result{
		
		private boolean hasMore;
		
		private List<CommonListModel> list;

		public Result(boolean hasMore, List<CommonListModel> list) {
			super();
			this.hasMore = hasMore;
			this.list = list;
		}
	}
	
	private View mFooterLoading;
	
	private List<CommonListModel> list = new ArrayList<CommonListModel>();
	
	private CommonListAdapter adapter;
	
	public CommonListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CommonListView(
			Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
			com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle style) {
		super(context, mode, style);
		init();
	}

	public CommonListView(Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
		super(context, mode);
		init();
	}

	public CommonListView(Context context) {
		super(context);
		init();
	}

	private void init(){
		mFooterLoading = inflate(getContext(), R.layout.footer_loading, null);
		getRefreshableView().addFooterView(mFooterLoading);
		adapter = new CommonListAdapter(getContext(), list);
		setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				_load(true);
				
			}
		});
		
		setRefreshing(true);
	}
	
	private void _complete(boolean more){
		if(more){
			mFooterLoading.setVisibility(View.VISIBLE);
		}else{
			mFooterLoading.setVisibility(View.GONE);
		}
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				CommonListView.this.onRefreshComplete();
				CommonListView.this.adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void _load(final boolean refresh){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(refresh){
					list.clear();
				}
				
				Result result = refresh();
				list.addAll(result.list);
				_complete(result.hasMore);
				
			}
		}).start();	
	}
	
	
	protected abstract Result refresh();
	
	protected abstract Result loadMore();
	

	
	
}
