package com.yoka.fan.wiget;

import com.yoka.fan.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class MaskImageView extends ImageView{

	public MaskImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MaskImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MaskImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (maskBitmap == null) {
			maskBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
					Config.ARGB_8888);
			maskCanvas = new Canvas(maskBitmap);
		}
		maskCanvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		canvas.drawBitmap(maskBitmap, 0f, 0f, paint);
		
	}
	
	private Paint paint = new Paint();

	private Paint clearPaint = new Paint();

	private Bitmap maskBitmap;

	private Canvas maskCanvas;
	
	private void init() {
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		paint.setColor(getContext().getResources().getColor(
				R.color.holo_green_light));
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(10);

		clearPaint
				.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		clearPaint.setColor(getContext().getResources().getColor(
				R.color.transparent));
		clearPaint.setFilterBitmap(true);
	}
	
}
