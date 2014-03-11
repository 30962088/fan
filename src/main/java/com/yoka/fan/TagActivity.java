package com.yoka.fan;

import java.util.List;

import com.yoka.fan.network.ListItemData;
import com.yoka.fan.network.Tag;
import com.yoka.fan.wiget.CommonListView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

public class TagActivity extends BaseActivity{

	public static final String PARAM_TAG = "tag";
	
	public static final String PARAM_STYLE = "style";
	
	private String tag;
	
	private String style;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		tag = getIntent().getStringExtra(PARAM_TAG);
		style = getIntent().getStringExtra(PARAM_STYLE);
		TagListView listView = new TagListView(this);
		setContentView(listView);
	}
	
	
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return tag;
	}
	
	private class TagListView extends CommonListView{

		public TagListView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

		public TagListView(
				Context context,
				com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
				com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle style) {
			super(context, mode, style);
			// TODO Auto-generated constructor stub
		}

		public TagListView(Context context,
				com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
			super(context, mode);
			// TODO Auto-generated constructor stub
		}

		public TagListView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected List<ListItemData> load(int skip,int limit) {
			Tag request = new Tag(style,skip,limit);
			request.request();
			return request.getListData();
		}

		@Override
		public String getEmptyTip() {
			// TODO Auto-generated method stub
			return "该标签没有任何搭配";
		}

		
		
	}

}
