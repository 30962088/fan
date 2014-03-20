package com.yoka.fan.wiget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;

import com.yoka.fan.network.ListItemData;
import com.yoka.fan.network.Request.Status;
import com.yoka.fan.wiget.BaseListView.OnLoadListener;

public abstract class CommonListView extends BaseListView implements OnLoadListener{
	
	public static interface OnVerticalScrollListener{
		public void onScrollUp();
		public void onScrollDown();
	}
	
	private static int limit = 20;
	
	protected List<CommonListModel> list = new ArrayList<CommonListModel>();
	
	protected CommonListAdapter adapter;
	
	public CommonListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public List<CommonListModel> getList() {
		return list;
	}
	
	public CommonListAdapter getAdapter() {
		return adapter;
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
	
	private OnVerticalScrollListener onVerticalScrollListener;
	
	public void setOnVerticalScrollListener(
			OnVerticalScrollListener onVerticalScrollListener) {
		this.onVerticalScrollListener = onVerticalScrollListener;
	}
	
	private float startY=0;

	private void init(){
		
		adapter = new CommonListAdapter(getContext(), list);
		setAdapter(adapter);
		setOnLoadListener(this);
		setLimit(limit);
				
		
	}
	
	
	
	public abstract String getEmptyTip();
	
	public static class LoadResult{
		private Status status;
		private List<ListItemData> list;
		public LoadResult(Status status, List<ListItemData> list) {
			super();
			this.status = status;
			this.list = list;
		}
		public List<ListItemData> getList() {
			return list;
		}
		public Status getStatus() {
			return status;
		}
	}
	
	protected abstract LoadResult load(int offset,int limit);
	
	

	
	private int offset;
	
	@Override
	public boolean onLoad(int offset,int limit) {
		this.offset = offset;
		LoadResult loadResult = load(offset,limit);
		if(loadResult.getStatus() != Status.SUCCESS){
			return true;
		}
		List<ListItemData> result = loadResult.list;
		if(result == null ){
			result = new ArrayList<ListItemData>();
		}
		List<CommonListModel> model = new ArrayList<CommonListModel>();
		
		for(ListItemData data:result){
			model.add(data.toCommonListModel());
		}
		if(offset == 0){
			list.clear();
		}
		list.addAll(model);
		
		
		return result.size()>=limit?true:false;
	}
	
	@Override
	public void onLoadSuccess() {
		if(offset == 0 && list.size() == 0){
			setBackgroundDrawable(writeOnDrawable(getEmptyTip()));
		}else{
			setBackgroundDrawable(null);
		}
		adapter.notifyDataSetChanged();
		
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		Adapter adapter = getRefreshableView().getAdapter();
		if(onVerticalScrollListener != null&&adapter != null && adapter.getCount()>3){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				float delta = event.getY()-startY;
				if(delta<-5){
					onVerticalScrollListener.onScrollDown();
				}else if(delta > 5) {
					onVerticalScrollListener.onScrollUp();
				}
				Log.d("zzm", ""+delta);
				break;
			}
		}
		
		return super.dispatchTouchEvent(event);
	}
	
	
}
