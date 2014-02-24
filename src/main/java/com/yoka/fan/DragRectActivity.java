package com.yoka.fan;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class DragRectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_rect_layout);

		ImageLoader imageLoader = Utils.getImageLoader(this);

		imageLoader.displayImage(getIntent().getData().toString(),
				((ImageView) findViewById(R.id.dragRect)));
	}
}
