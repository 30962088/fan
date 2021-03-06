package com.yoka.fan;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.utils.Dirctionary;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.TouchView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class DragRectActivity extends Activity implements OnClickListener{

	private TouchView dragRectView;
	
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.drag_rect_layout);
		findViewById(R.id.cancel_btn).setOnClickListener(this);
		findViewById(R.id.select_btn).setOnClickListener(this);
		dragRectView = (TouchView) findViewById(R.id.dragRect);
		initRectView();
		
	}
	
	private void initRectView(){
		dragRectView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				int size = dragRectView.getWidth()-DisplayUtils.Dp2Px(context, 30);
				dragRectView.setRectWidth(size);
				dragRectView.setRectHeight(size);
				dragRectView.setRectType(TouchView.TYPE_RECYCLE);
				dragRectView.drawRect();
				
				
			}
		});
	
		ImageLoader imageLoader = Utils.getImageLoader(this);
		imageLoader.displayImage(getIntent().getDataString(),
				dragRectView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_btn:
			setResult(RESULT_CANCELED, null);
			finish();
			break;
		case R.id.select_btn:
			byte[] stream = dragRectView.getSelection();
			if(stream != null){
				File photoFile = Dirctionary.creatPicture();
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(photoFile);
					fos.write(dragRectView.getSelection());
					Intent intent = new Intent();
					intent.setData(Uri.fromFile(photoFile));
					setResult(RESULT_OK, intent);
					finish();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
		
	}
}
