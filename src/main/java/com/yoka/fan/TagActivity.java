package com.yoka.fan;

import java.util.List;

import com.yoka.fan.network.ListItemData;
import com.yoka.fan.network.Tag;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.CommonListModel.NameValuePair;
import com.yoka.fan.wiget.CommonListView;
import com.yoka.fan.wiget.CommonListView.OnVerticalScrollListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class TagActivity extends BaseActivity implements OnVerticalScrollListener{

	public static final String PARAM_NAME = "name";
	
	public static final String PARAM_VALUE = "value";
	
	private String name;
	
	private String value;
	
	private View headerContainer;
	
	public static void open(Context context,String name,String value){
		Intent intent = new Intent(context, TagActivity.class);
		intent.putExtra(PARAM_NAME, name);
		intent.putExtra(PARAM_VALUE, value);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		name = getIntent().getStringExtra(PARAM_NAME);
		value = getIntent().getStringExtra(PARAM_VALUE);
		TagListView listView = new TagListView(this);
		listView.setOnVerticalScrollListener(this);
		setContentView(listView);
		headerContainer = findViewById(R.id.base_actionbar_content);
		ImageView camera = ((ImageView)findViewById(R.id.actionbar_right));
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				openShare();
			}
		});
		camera.setImageResource(R.drawable.camera);
	}
	
	
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return value;
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
		protected LoadResult load(int skip,int limit) {
			Tag request = new Tag(new NameValuePair(name, value),skip,limit);
			request.request();
			return new LoadResult(request.getStatus(),request.getListData());
		}

		@Override
		public String getEmptyTip() {
			// TODO Auto-generated method stub
			return "该标签没有任何搭配";
		}

		
		
	}

	@Override
	public void onScrollUp() {
		if(headerContainer != null && headerContainer.getVisibility() == View.GONE){
			Utils.expand(headerContainer);
//			headerContainer.setVisibility(View.VISIBLE);
		}
		
	}


	@Override
	public void onScrollDown() {
		if(headerContainer != null && headerContainer.getVisibility() == View.VISIBLE){
			Utils.collapse(headerContainer);
//			headerContainer.setVisibility(View.GONE);
		}
		
	}

}
