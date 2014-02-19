package com.yoka.fan.wiget;

import com.yoka.fan.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonListItemView extends FrameLayout{

	private ImageView mPhotoView;
	
	private TextView mNameView;
	
	private TextView mDatetimeView;
	
	private TextView mStarCount;
	
	private TextView mCommentCount;
	
	public CommonListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CommonListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CommonListItemView(Context context) {
		super(context);
		init();
	}

	private void init(){
		LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, this);
		
	}
	
}
