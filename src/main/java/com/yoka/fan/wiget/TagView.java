package com.yoka.fan.wiget;

import com.yoka.fan.R;

import android.content.Context;
import android.widget.TextView;

public class TagView extends TextView{

	public TagView(Context context,String tag) {
		super(context, null, R.style.listItemTag);
		setText(tag);
	}

}
