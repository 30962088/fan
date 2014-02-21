package com.yoka.fan.wiget;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.R;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.LinkModel.Link;
import com.yoka.fan.wiget.LinkedView.BackgroundImageView.OnViewdrawListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LinkedView extends RelativeLayout {

	private BackgroundImageView imageView;

	private Context context;

	private ImageLoader imageLoader;

	private LinkModel linkModel;

	public LinkedView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LinkedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LinkedView(Context context) {
		super(context);
		init(context);
	}

	private void init(final Context context) {
		this.context = context;
		imageView = new BackgroundImageView(context);
		addView(imageView);
		imageView.setOndrawListener(new OnViewdrawListener() {

			@Override
			public void ondraw(float[] bounds) {
				if(linkModel.isShowLink()){
					for (Link link : linkModel.getLinkList()) {
						addView(new TagView(context, link, bounds));
					}
				}
				

			}
		});
		imageLoader = Utils.getImageLoader(context);
	}

	public void load(LinkModel model) {
		this.linkModel = model;
		changeImageSize();
		removeAllTag();
		imageLoader.displayImage(model.getUrl(), imageView);

	}

	private void changeImageSize() {
		int width = linkModel.getWidth(), height = linkModel.getHeight();
		if (width > 0 && height > 0 && imageView instanceof BackgroundImageView) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView
					.getLayoutParams();
			params.width = width;
			params.height = height;
			imageView.setLayoutParams(params);
		}

	}

	private void removeAllTag() {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TagView) {
				removeView(view);
			}
		}
	}

	private static class TagView extends TextView {

		private Link link;

		private float[] bounds;

		public TagView(Context context, final Link link, final float[] bounds) {
			super(context);
			setTextColor(Color.WHITE);
			setGravity(Gravity.CENTER);
			setTextSize(DisplayUtils.Dp2Px(context, 9));
			if (link.getLeft() < 0.5) {
				setBackgroundResource(R.drawable.tag);
			} else {
				setBackgroundResource(R.drawable.tag_right);
			}

			setText(link.getName());

			this.link = link;
			this.bounds = bounds;

			ViewTreeObserver viewTreeObserver = getViewTreeObserver();
			if (viewTreeObserver.isAlive()) {
				viewTreeObserver
						.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
							@Override
							public void onGlobalLayout() {
								getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
								int w = getWidth();
								int h = getHeight();
								int width = w, height = h;
								RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();

								int t = (int) (bounds[1]
										+ (bounds[3] * link.getTop()) - height / 2);

								int l = (int) (bounds[0] + (bounds[2] * link
										.getLeft()));

								if (link.getLeft() < 0.5) {
									l -= width;
								}

								params.setMargins(l, t, 0, 0);

								setLayoutParams(params);

							}
						});
			}

		}

	}

	public static class BackgroundImageView extends ImageView {

		public static interface OnViewdrawListener {
			public void ondraw(float[] bounds);
		}

		private OnViewdrawListener ondrawListener;

		public void setOndrawListener(OnViewdrawListener ondrawListener) {
			this.ondrawListener = ondrawListener;
		}

		public BackgroundImageView(Context context) {
			super(context);
			init();
		}

		@Override
		protected void onMeasure(final int widthMeasureSpec,
				int heightMeasureSpec) {
			final Drawable d = this.getDrawable();

			int minHeight = DisplayUtils.Dp2Px(getContext(), 220);
			
			if (d != null) {
				// ceil not round - avoid thin vertical gaps along the
				// left/right edges
				final int width = MeasureSpec.getSize(widthMeasureSpec);
				int height = (int) Math.ceil(width
						* (float) d.getIntrinsicHeight()
						/ d.getIntrinsicWidth());
				if(height < minHeight){
					height = minHeight;
				}
				this.setMeasuredDimension(width, height);
			} else {
				if(heightMeasureSpec < minHeight){
					heightMeasureSpec = minHeight;
				}
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
		}

		private void init() {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

			setLayoutParams(params);

			setScaleType(ScaleType.FIT_CENTER);

		}

		private float[] getBitmapBound() {
			float[] bounds = new float[6];
			float[] values = new float[9];

			Matrix m = getImageMatrix();
			m.getValues(values);
			bounds[0] = (int) values[2];
			bounds[1] = (int) values[5];
			bounds[2] = values[0] * getDrawable().getIntrinsicWidth();
			bounds[3] = values[4] * getDrawable().getIntrinsicHeight();
			Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
			bounds[4] = bitmap.getWidth();
			bounds[5] = bitmap.getHeight();
			return bounds;

		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (ondrawListener != null && getDrawable() != null) {
				ondrawListener.ondraw(getBitmapBound());
			}

		}
	}

}
