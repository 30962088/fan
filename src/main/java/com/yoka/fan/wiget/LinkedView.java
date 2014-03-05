package com.yoka.fan.wiget;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.R;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.BuyPopupWindow.GoodsItem;
import com.yoka.fan.wiget.LinkModel.Link;
import com.yoka.fan.wiget.LinkedView.BackgroundImageView.OnViewdrawListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
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
	
	private RelativeLayout tagContainer;

	public static interface onImageClickListener {
		public void onClick(float left, float top);
	}

	private onImageClickListener onImageClickListener;

	public void setOnImageClickListener(
			onImageClickListener onImageClickListener) {
		this.onImageClickListener = onImageClickListener;
	}

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
		imageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (onImageClickListener != null) {
						float[] b = imageView.getBitmapBound();
						float left = (event.getX() - b[0]) / b[2];
						float top = (event.getY() - b[1]) / b[3];
						onImageClickListener.onClick(left, top);
					}
				}
				return true;
			}
		});
		addView(imageView);
		tagContainer = new RelativeLayout(context);
		tagContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		addView(tagContainer);
		imageView.setOndrawListener(new OnViewdrawListener() {

			@Override
			public void ondraw(float[] bounds) {
				if (linkModel.isShowLink() && linkModel.getLinkList() != null) {
					for (Link link : linkModel.getLinkList()) {
						tagContainer.addView(new TagView(context, link, bounds));
					}
//					tagContainer.setVisibility(View.VISIBLE);
				}

			}
		});
		imageLoader = Utils.getImageLoader(context);
	}

	public void load(LinkModel model) {
		this.linkModel = model;
//		tagContainer.setVisibility(View.INVISIBLE);
		tagContainer.removeAllViews();
		changeImageSize();
		imageView.setImageBitmap(null);
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


	private static class TagView extends TextView implements OnClickListener {

		private Link link;

		private float[] bounds;

		public TagView(Context context, final Link link, final float[] bounds) {
			super(context);
			setOnClickListener(this);
			setTextColor(Color.WHITE);
			setGravity(Gravity.CENTER);
			setVisibility(View.INVISIBLE);
			setSingleLine(true);
			setEllipsize(TruncateAt.END);
			// setTextSize(DisplayUtils.Dp2Px(context, 9));
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

								postDelayed(new Runnable() {

									@Override
									public void run() {
										setVisibility(View.VISIBLE);

									}
								}, 50);

							}
						});
			}

		}

		@Override
		public void onClick(View v) {
			new BuyPopupWindow(getContext(),
					new ArrayList<BuyPopupWindow.GoodsItem>() {
						{
							for (int i = 0; i < 5; i++) {
								GoodsItem item = new GoodsItem();
								item.img = "http://image.iask.sina.com.cn/cidian/21/90/13715390212009-07-151006466.jpg";
								item.name = "goods";
								item.price = 323.11f;
								item.typeResId = R.drawable.blazers;
								add(item);
							}
							GoodsItem item1 = new GoodsItem();
							item1.title = "编辑推荐";
							add(item1);
							for (int i = 0; i < 10; i++) {
								GoodsItem item = new GoodsItem();
								item.img = "http://image.iask.sina.com.cn/cidian/21/90/13715390212009-07-151006466.jpg";
								item.name = "goods";
								item.price = 323.11f;
								item.typeResId = R.drawable.blazers;
								add(item);
							}

						}
					});

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

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			// final Drawable d = this.getDrawable();
			//
			// int minHeight = DisplayUtils.Dp2Px(getContext(), 220);
			//
			// if (d != null) {
			// // ceil not round - avoid thin vertical gaps along the
			// // left/right edges
			// final int width = MeasureSpec.getSize(widthMeasureSpec);
			// int height = (int) Math.ceil(width
			// * (float) d.getIntrinsicHeight()
			// / d.getIntrinsicWidth());
			// if(height < minHeight){
			// height = minHeight;
			// }
			// this.setMeasuredDimension(width, height);
			// } else {
			// if(heightMeasureSpec < minHeight){
			// heightMeasureSpec = minHeight;
			// }
			// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			// }
		}

		private void init() {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			setLayoutParams(params);

			setScaleType(ScaleType.FIT_XY);
			setAdjustViewBounds(true);

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
			if (ondrawListener != null && ((BitmapDrawable)getDrawable()).getBitmap() != null) {
				ondrawListener.ondraw(getBitmapBound());
			}

		}
	}

}
