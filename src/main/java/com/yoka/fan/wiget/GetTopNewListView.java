package com.yoka.fan.wiget;

import java.util.List;

import com.yoka.fan.network.GetTopNew;
import com.yoka.fan.network.ListItemData;

import android.content.Context;
import android.util.AttributeSet;

public class GetTopNewListView extends CommonListView{

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

	
	
}
