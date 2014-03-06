package com.yoka.fan.wiget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import com.yoka.fan.R;
import com.yoka.fan.network.ListItemData;

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
	
	
	
	private static int limit = 20;
	
	private int offset = 0;

	
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
		setAdapter(adapter);
		setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				load(true);
				
			}
		});
		
		setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if(hasMode){
					load(false);
				}
				
			}
		});
		
	
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		load(true);
	}
	
	private boolean hasMode = false;


	
	private void _complete(final boolean more){
		hasMode = more;
		
		if(!more){
			getRefreshableView().removeFooterView(mFooterLoading);
		}
		CommonListView.this.onRefreshComplete();
		CommonListView.this.adapter.notifyDataSetChanged();
		offset += limit;
		
	}
	
	
	
	public void load(final boolean refresh){
		
		if(refresh){
			offset = 0;
			list.clear();
		}
		
		new AsyncTask<Void, Void, Result>() {

			@Override
			protected Result doInBackground(Void... s) {
				// TODO Auto-generated method stub
				return _load(offset,limit);
			}
			
			@Override
			protected void onPostExecute(Result result) {
				
				list.addAll(result.list);
				
				_complete(result.hasMore);
			}
			
		}.execute();
		
	}
	
	
	private Result _load(int offset,int limit){
		List<ListItemData> result = load(offset,limit);
		if(result == null ){
			result = new ArrayList<ListItemData>();
		}
		List<CommonListModel> model = new ArrayList<CommonListModel>();
		
		for(ListItemData data:result){
			model.add(data.toCommonListModel());
		}
		return new Result(result.size()>=limit?true:false, model);
	}
	
	
	protected abstract List<ListItemData> load(int offset,int limit);
	
	

	
	
}
