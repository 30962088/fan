package com.yoka.fan.wiget;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.yoka.fan.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * 继承ImageView 实现了多点触碰的拖动和缩放
 * 
 * @author Administrator
 * 
 */
public class TouchView extends ImageView {
	static final int NONE = 0;
	static final int DRAG = 1; // 拖动中
	static final int ZOOM = 2; // 缩放中
	static final int BIGGER = 3; // 放大ing
	static final int SMALLER = 4; // 缩小ing
	private int mode = NONE; // 当前的事件

	private float beforeLenght; // 两触点距离
	private float afterLenght; // 两触点距离
	private float scale = 0.024f; // 缩放的比例 X Y方向都是这个值 越大缩放的越快

	/* 处理拖动 变量 */
	private int start_x;
	private int start_y;
	private int stop_x;
	private int stop_y;

	public static final int TYPE_RECT = 1;

	public static final int TYPE_RECYCLE = 2;

	private int rectWidth;

	private int rectHeight;

	private int rectType;

	private int rectBounds[] = new int[4];

	public void setRectWidth(int rectWidth) {
		this.rectWidth = rectWidth;

	}

	public void setRectHeight(int rectHeight) {
		this.rectHeight = rectHeight;

	}

	public void setRectType(int rectType) {
		this.rectType = rectType;

	}

	public void drawRect() {
		if (maskView != null) {
			maskView.invalidate();
		}
	}

	public TouchView(Context context) {
		super(context);
		init();

	}

	private MaskView maskView;

	private void init() {
		maskView = new MaskView(getContext());
		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						ViewGroup group = ((ViewGroup) getParent());
						if (group.getChildCount() < 2) {
							group.addView(maskView);
						}

					}
				});
	}

	public TouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Bitmap loadBitmapFromView() {
		Bitmap bitmap = ((BitmapDrawable)getDrawable()).getBitmap();
		//02-26 08:11:45.282: D/zmm(2028): x1:-257,y1:-319,x2:737,y2:910,width:640,height:960
		Log.d("zmm", "x1:"+bounds[0]+",y1:"+bounds[1]+",x2:"+bounds[2]+",y2:"+bounds[3]+",width:"+bitmap.getWidth()+",height:"+bitmap.getHeight());
		Log.d("zmm", "left:"+getLeft()+",top:"+getTop());
		Log.d("zmm", "x1:"+rectBounds[0]+",y1:"+rectBounds[1]+",width:"+rectBounds[2]+",height:"+rectBounds[3]);
		bitmap = Bitmap.createScaledBitmap(bitmap, Math.abs(bounds[2]-bounds[0]), Math.abs( bounds[3]-bounds[1]), true);
		int x1 = Math.max(-getLeft()+rectBounds[0], 0);
		int y1 = Math.max(-getTop()+rectBounds[1], 0);
		int width = Math.min(rectBounds[2], bitmap.getWidth());
		int height = Math.min(rectBounds[3], bitmap.getHeight());
		bitmap = Bitmap.createBitmap(bitmap, x1,y1,width,height);
		
	    return bitmap;
	}
	
	public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) 
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else 
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

	public byte[] getSelection() {
		maskView.setVisibility(View.GONE);
		Bitmap bitmap = getBitmapFromView((View)getParent());
		bitmap = Bitmap.createBitmap(bitmap,rectBounds[0] ,rectBounds[1],rectBounds[2],rectBounds[3]);
//		try {
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream("/sdcard/fantest.jpg"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Log.d("zzm", "bounds:"+rectBounds[0]+","+rectBounds[1]+","+rectBounds[2]+","+rectBounds[3]);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] pixs = stream.toByteArray();
		bitmap.recycle();
		return pixs;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

	}

	/**
	 * 就算两点间的距离
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 处理触碰..
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			stop_x = (int) event.getRawX();
			stop_y = (int) event.getRawY();
			start_x = (int) event.getX();
			start_y = stop_y - this.getTop();
			if (event.getPointerCount() == 2)
				beforeLenght = spacing(event);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			if (spacing(event) > 10f) {
				mode = ZOOM;
				beforeLenght = spacing(event);
			}
			break;
		case MotionEvent.ACTION_UP:

			mode = NONE;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			/* 处理拖动 */
			if (mode == DRAG) {
				if (Math.abs(stop_x - start_x - getLeft()) < 88
						&& Math.abs(stop_y - start_y - getTop()) < 85) {
					this.setPosition(stop_x - start_x, stop_y - start_y, stop_x
							+ this.getWidth() - start_x, stop_y - start_y
							+ this.getHeight());
					stop_x = (int) event.getRawX();
					stop_y = (int) event.getRawY();
				}
			}
			/* 处理缩放 */
			else if (mode == ZOOM) {
				if (spacing(event) > 10f) {
					afterLenght = spacing(event);
					float gapLenght = afterLenght - beforeLenght;
					if (gapLenght == 0) {
						break;
					} else if (Math.abs(gapLenght) > 5f) {
						if (gapLenght > 0) {
							this.setScale(scale, BIGGER);
						} else {
							this.setScale(scale, SMALLER);
						}
						beforeLenght = afterLenght;
					}
				}
			}
			break;
		}
		return true;
	}

	private int bounds[] = new int[4];
	
	/**
	 * 实现处理缩放
	 */
	private void setScale(float temp, int flag) {
		
		
		
		if (flag == BIGGER) {
			bounds = new int[]{this.getLeft() - (int) (temp * this.getWidth()),this.getTop() - (int) (temp * this.getHeight())
					,this.getRight() + (int) (temp * this.getWidth()),
					this.getBottom() + (int) (temp * this.getHeight())};
			
		} else if (flag == SMALLER) {
			bounds = new int[]{this.getLeft() + (int) (temp * this.getWidth()),
					this.getTop() + (int) (temp * this.getHeight()),
					this.getRight() - (int) (temp * this.getWidth()),
					this.getBottom() - (int) (temp * this.getHeight())};
			
		}
		this.setFrame(bounds[0], bounds[1], bounds[2], bounds[3]);

	}

	/**
	 * 实现处理拖动
	 */
	private void setPosition(int left, int top, int right, int bottom) {
		this.layout(left, top, right, bottom);

	}

	private class MaskView extends View {

		public MaskView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public MaskView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public MaskView(Context context) {
			super(context);
			init();
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

		private void drawCircle() {
			if (rectWidth > 0 && rectHeight > 0 && rectType > 0) {
				int x1 = (getWidth() - rectWidth) / 2;
				int y1 = Math.max((getHeight() - rectHeight) / 2, 0);
				int x2 = x1 + rectWidth;
				int y2 = y1 + rectHeight;
				rectBounds = new int[] { x1, y1, x2 - x1, Math.min(y2-y1, getHeight())  };
				if (rectType == TYPE_RECYCLE) {
					maskCanvas.drawOval(new RectF(x1, y1, x2, y2), clearPaint);
				} else {
					maskCanvas.drawRect(new RectF(x1, y1, x2, y2), clearPaint);
				}

			}

		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			if (maskBitmap == null) {
				maskBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
						Config.ARGB_8888);
				maskCanvas = new Canvas(maskBitmap);
			}
			maskCanvas.drawRect(0, 0, getWidth(), getHeight(), paint);
			drawCircle();
			canvas.drawBitmap(maskBitmap, 0f, 0f, paint);

		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			TouchView.this.onTouchEvent(event);
			return false;
		}

	}

}// Source GIT HUB Libraries