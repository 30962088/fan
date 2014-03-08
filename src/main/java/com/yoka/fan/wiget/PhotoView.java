package com.yoka.fan.wiget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.makeramen.RoundedImageView;
import com.yoka.fan.R;

public class PhotoView extends RoundedImageView {

	
	
	public PhotoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PhotoView(Context context) {
		super(context);
		init();
	}
	
	private int vipSize;
	
	private Bitmap vipBitmap;

	private void init() {
		setImageResource(R.drawable.photo_default);
		setScaleType(ScaleType.FIT_XY);
		setOval(true);
		setBackgroundColor(Color.WHITE);
		setRoundBackground(true);
		setBorderColor(getResources().getColor(R.color.photo_border));
		setBorderWidth((int)getResources().getDimension(R.dimen.border_size));
	}
	
	public void setVipSize(int vipSize) {
		this.vipSize = vipSize;
		invalidate();
	}
	
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(vipSize>0){
			if(vipBitmap == null){
				vipBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.vip)).getBitmap();
				vipBitmap = Bitmap.createScaledBitmap(vipBitmap, vipSize, vipSize, true);
			}
			canvas.drawBitmap(vipBitmap, getWidth()-vipSize, getHeight()-vipSize, null);
		}
	}

}
