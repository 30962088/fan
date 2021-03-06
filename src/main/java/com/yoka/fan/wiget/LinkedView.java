package com.yoka.fan.wiget;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.MainActivity;
import com.yoka.fan.R;
import com.yoka.fan.SelectCategoryActivity.Model;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.BuyPopupWindow.GoodsItem;
import com.yoka.fan.wiget.LinkModel.Link;

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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

	private boolean closed = false;
	
	private boolean move = false;

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public static interface onImageClickListener {
		public void onClick(float left, float top);
	}

	public static interface onTagClickListener {
		public void onClose(LinkModel.Link link);

		public void onClick(LinkModel.Link link);
		
		public void onMove(LinkModel.Link link);
		
	}

	private onImageClickListener onImageClickListener;

	private onTagClickListener onTagClickListener;

	public void setOnTagClickListener(onTagClickListener onTagClickListener) {
		this.onTagClickListener = onTagClickListener;
	}

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
	
	public void setMove(boolean move) {
		this.move = move;
	}

	private void init(final Context context) {
		this.context = context;
		setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		imageView = new BackgroundImageView(context);
		imageView.setLayoutParams(new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		imageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (onImageClickListener != null
							&& imageView.getDrawable() != null
							&& ((BitmapDrawable) imageView.getDrawable())
									.getBitmap() != null) {
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
		tagContainer.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(tagContainer);
		imageView.setOndrawListener(new OnViewdrawListener() {

			@Override
			public void ondraw(float[] bounds) {
				if (linkModel.getLinkList() != null) {
					for (Link link : linkModel.getLinkList()) {
						TagView tagView = new TagView(context, link, bounds);
						tagView.setMove(move);
						tagContainer
								.addView(tagView);
					}
					// tagContainer.setVisibility(View.VISIBLE);
				}

			}
		});
		imageLoader = Utils.getImageLoader(context);

	}
	
	private boolean online = true;
	
	public void setOnline(boolean online) {
		this.online = online;
	}

	public void load(LinkModel model) {
		if(this.linkModel != model){
			this.linkModel = model;
			init = true;
			tagContainer.removeAllViews();
			// changeImageSize();
			imageView.setImageBitmap(null);
			if(online){
				measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				imageLoader.displayImage(model.getUrl(getMeasuredWidth(),getMeasuredHeight()), imageView);
			}else{
				imageLoader.displayImage(model.getUrl(), imageView);
			}
			
			
		}
		if(model.isShowLink()){
			tagContainer.setVisibility(View.VISIBLE);
		}else{
			tagContainer.setVisibility(View.INVISIBLE);
		}
		
	}

	private void changeImageSize() {
		int width = linkModel.getWidth(context), height = linkModel
				.getHeight(context);
		if (width > 0 && height > 0 && imageView instanceof BackgroundImageView) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView
					.getLayoutParams();
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.WRAP_CONTENT;
			imageView.setLayoutParams(params);
		}

	}

	private class TagView extends TextView  {

		private Link link;

		private float[] bounds;
		
		private int offset;
		
		private boolean move = false;

		public TagView(Context context, final Link link, final float[] bounds) {
			super(context);
			if(closed){
				offset = (int) getResources().getDimension(R.dimen.tag_close);
			}
			
			setTextColor(Color.WHITE);
			setGravity(Gravity.CENTER);
			setVisibility(View.INVISIBLE);
			setSingleLine(true);
			setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
			setEllipsize(TruncateAt.END);
			

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
								draw();

							}
						});
			}

		}
		
		public void setMove(boolean move) {
			this.move = move;
		}
		
		private void draw(){
			if(move){
				if (closed) {
					setBackgroundResource(R.drawable.tag_right_close);
				} else {
					setBackgroundResource(R.drawable.tag_right);
				}
			}else{
				if (link.getLeft() < 0.5) {
					if (closed) {
						setBackgroundResource(R.drawable.tag_close);
					} else {
						setBackgroundResource(R.drawable.tag);
					}

				} else {
					if (closed) {
						setBackgroundResource(R.drawable.tag_right_close);
					} else {
						setBackgroundResource(R.drawable.tag_right);
					}

				}
			}
			
			setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); 
			
			
			int w = getMeasuredWidth();
			int h = getMeasuredHeight();
			int width = w, height = h;
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();

			int t = (int) (bounds[1]
					+ (bounds[3] * link.getTop()) - height / 2);

			int l = (int) (bounds[0] + (bounds[2] * link
					.getLeft()));
			
			if(!move){
				if (link.getLeft() < 0.5) {
					l -= width;
					if (l < 0) {
						params.width = Math.max(getWidth() + l, 0);
						l = 0;
					}
				}
			}
			

			params.setMargins(l, t, 0, 0);

			setLayoutParams(params);

			
			setVisibility(View.VISIBLE);
		}
		
		private float[] lastCoor;

		private int[] lastMargin;
		
		private long touchTime;
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			
			LayoutParams params = ((RelativeLayout.LayoutParams)getLayoutParams());
			lastMargin = new int[]{params.leftMargin,params.topMargin};
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				touchTime = System.currentTimeMillis();
				lastCoor = new float[]{event.getX(),event.getY()};
			}else if(event.getAction() == MotionEvent.ACTION_MOVE){
				if(move){
					
					float deltaX = event.getX()-lastCoor[0],
							  deltaY = event.getY()-lastCoor[1];
					link.setLeft((lastMargin[0]+deltaX- bounds[0])/bounds[2]);
					link.setTop((lastMargin[1]+deltaY-bounds[1]+getHeight()/2)/bounds[3]);
					draw();
				}
			}if(event.getAction() == MotionEvent.ACTION_UP){
				if(System.currentTimeMillis()-touchTime < 200){
					if( onTagClickListener != null ){
						if(closed){
							if( event.getX()>getWidth()-offset){
								onTagClickListener.onClose(link);
								return true;
							}
						}
						onTagClickListener.onClick(link);
					}
					
					
				}
				if(onTagClickListener != null){
					
					onTagClickListener.onMove(link);
				}
				
				
			}
			return true;
		}

	}

	private boolean init = true;

	public static interface OnViewdrawListener {
		public void ondraw(float[] bounds);
	}

	public class BackgroundImageView extends ImageView {

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

			int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
			int parentHeight = parentWidth / 3 * 4;
			this.setMeasuredDimension(parentWidth, parentHeight);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

		private void init() {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			setLayoutParams(params);

			setScaleType(ScaleType.FIT_CENTER);
			// setAdjustViewBounds(true);

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

			if (init && ondrawListener != null && getDrawable() != null
					&& ((BitmapDrawable) getDrawable()).getBitmap() != null) {
				ondrawListener.ondraw(getBitmapBound());
				init = false;
			}

		}
		
		
		
	}

}
