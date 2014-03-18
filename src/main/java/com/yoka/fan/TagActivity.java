package com.yoka.fan;

import java.util.List;

import com.yoka.fan.network.ListItemData;
import com.yoka.fan.network.Tag;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.CommonListView;
import com.yoka.fan.wiget.CommonListView.OnVerticalScrollListener;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

public class TagActivity extends BaseActivity implements OnVerticalScrollListener{

	public static final String PARAM_TAG = "tag";
	
	public static final String PARAM_STYLE = "style";
	
	private String tag;
	
	private String style;
	
	private View headerContainer;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		tag = getIntent().getStringExtra(PARAM_TAG);
		style = getIntent().getStringExtra(PARAM_STYLE);
		TagListView listView = new TagListView(this);
		listView.setOnVerticalScrollListener(this);
		setContentView(listView);
		headerContainer = findViewById(R.id.base_actionbar_content);
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

	@Override
	public void onScrollUp() {
		if(headerContainer != null){
			Utils.expand(headerContainer);
//			headerContainer.setVisibility(View.VISIBLE);
		}
		
	}


	@Override
	public void onScrollDown() {
		if(headerContainer != null){
			Utils.collapse(headerContainer);
//			headerContainer.setVisibility(View.GONE);
		}
		
	}

}
