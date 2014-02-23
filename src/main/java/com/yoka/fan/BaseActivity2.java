package com.yoka.fan;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;

public abstract class BaseActivity2 extends BaseActivity{

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		getActionbarRight().setImageResource(R.drawable.close);
		getActionbarRight().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getActionbarLeft().setVisibility(View.GONE);
	}

}
