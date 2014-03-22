package com.yoka.fan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.utils.Dirctionary;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.DragRectView;
import com.yoka.fan.wiget.TouchView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

public class SelectPicActivity extends Activity implements OnClickListener {

	private TouchView dragRectView;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.select_pic_layout);
		findViewById(R.id.cancel_btn).setOnClickListener(this);
		findViewById(R.id.select_btn).setOnClickListener(this);
		dragRectView = (TouchView) findViewById(R.id.dragRect);
		initRectView();

	}

	private void initRectView() {
		dragRectView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						int height = dragRectView.getHeight();
						int width = height / 4 * 3;

						dragRectView.setRectWidth(width);
						dragRectView.setRectHeight(height);
						dragRectView.setRectType(TouchView.TYPE_RECT);
						dragRectView.drawRect();

					}
				});

		ImageLoader imageLoader = Utils.getImageLoader(this);
		imageLoader.displayImage(getIntent().getDataString(), dragRectView);
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
			if (stream != null) {
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

	public static Uri crop(Bitmap bitmap) {
		int width = bitmap.getWidth(), height = bitmap.getHeight();
		int cropWidth = 0, cropHeight = 0;
		if(width == height / 4 * 3){
			cropWidth = width;
			cropHeight = height;
		}else if (width > height / 4 * 3) {
			cropHeight = height;
			cropWidth = height / 4 * 3;
		} else {
			cropWidth = width;
			cropHeight = width / 3 * 4;
		}

		int x = (width - cropWidth) / 2;

		int y = (height - cropHeight) / 2;

		bitmap = Bitmap.createBitmap(bitmap, x, y, cropWidth, cropHeight);
		FileOutputStream out = null;
		File filename = Dirctionary.creatPicture();
		try {
			out = new FileOutputStream(filename);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bitmap.recycle();
				out.close();
			} catch (Throwable ignore) {
			}
		}
		
		return Uri.fromFile(filename);

	}

}
