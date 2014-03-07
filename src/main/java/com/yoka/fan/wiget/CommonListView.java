package com.yoka.fan.wiget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;

import android.util.AttributeSet;
import android.view.View;
import com.yoka.fan.network.ListItemData;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.wiget.BaseListView.OnLoadListener;

public abstract class CommonListView extends BaseListView implements OnLoadListener{
	
	private static int limit = 20;
	
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
		
		adapter = new CommonListAdapter(getContext(), list);
		setAdapter(adapter);
		setOnLoadListener(this);
		setLimit(limit);
	
	
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		// TODO Auto-generated method stub
		super.onVisibilityChanged(changedView, visibility);
		
	}
	
	
	public abstract String getEmptyTip();
	
	protected abstract List<ListItemData> load(int offset,int limit);
	
	

	
	private int offset;
	
	@Override
	public boolean onLoad(int offset,int limit) {
		this.offset = offset;
		List<ListItemData> result = load(offset,limit);
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
			setBackground(writeOnDrawable(getEmptyTip()));
		}else{
			setBackground(null);
		}
		adapter.notifyDataSetChanged();
		
	}
	
}
