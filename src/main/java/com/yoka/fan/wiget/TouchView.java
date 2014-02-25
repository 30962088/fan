package com.yoka.fan.wiget;

import java.io.ByteArrayOutputStream;

import com.yoka.fan.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
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

	public static Bitmap convertViewToBitmap(View view) {
//		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}

	public byte[] getSelection() {
		Bitmap bitmap = convertViewToBitmap(this);
		Log.d("zzm", "bounds:"+rectBounds[0]+","+rectBounds[1]+","+rectBounds[2]+","+rectBounds[3]);
		Bitmap photoBitmap = Bitmap.createBitmap(bitmap, rectBounds[0],
				rectBounds[1], rectBounds[2], rectBounds[3]);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] pixs = stream.toByteArray();
		photoBitmap.recycle();
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
		Log.d("zzm", "aa:" + (event.getAction() & MotionEvent.ACTION_MASK));
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

	/**
	 * 实现处理缩放
	 */
	private void setScale(float temp, int flag) {

		if (flag == BIGGER) {
			this.setFrame(this.getLeft() - (int) (temp * this.getWidth()),
					this.getTop() - (int) (temp * this.getHeight()),
					this.getRight() + (int) (temp * this.getWidth()),
					this.getBottom() + (int) (temp * this.getHeight()));
		} else if (flag == SMALLER) {
			this.setFrame(this.getLeft() + (int) (temp * this.getWidth()),
					this.getTop() + (int) (temp * this.getHeight()),
					this.getRight() - (int) (temp * this.getWidth()),
					this.getBottom() - (int) (temp * this.getHeight()));
		}

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
				int y1 = (getHeight() - rectHeight) / 2;
				int x2 = x1 + rectWidth;
				int y2 = y1 + rectHeight;
				rectBounds = new int[] { x1, y1, x2 - x1, y2 - y1 };
				if (rectType == TYPE_RECYCLE) {
					maskCanvas.drawOval(new RectF(x1, y1, x2, y2), clearPaint);
				} else {
					maskCanvas.drawOval(new RectF(x1, y1, x2, y2), clearPaint);
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