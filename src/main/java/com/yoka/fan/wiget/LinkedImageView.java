package com.yoka.fan.wiget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LinkedImageView extends ImageView{

	private int top = 33;
	
	private int left = 33;
	
	public LinkedImageView(Context context) {
		super(context);
	}
	
	public static int[] getBitmapOffset(ImageView img,  Boolean includeLayout) {
        int[] offset = new int[2];
        float[] values = new float[9];

        Matrix m = img.getImageMatrix();
        m.getValues(values);

        offset[0] = (int) values[5];
        offset[1] = (int) values[2];

        if (includeLayout) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) img.getLayoutParams();
            int paddingTop = (int) (img.getPaddingTop() );
            int paddingLeft = (int) (img.getPaddingLeft() );

            offset[0] += paddingTop + lp.topMargin;
            offset[1] += paddingLeft + lp.leftMargin;
        }
        return offset;
    }
	
	public LinkedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}



	public LinkedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	private float[] getBitmapBound(){
		float[] bounds = new float[6];
		float[] values = new float[9];

        Matrix m = getImageMatrix();
        m.getValues(values);
        bounds[0] = (int) values[2];
        bounds[1] = (int) values[5];
        bounds[2] = values[0]*getDrawable().getIntrinsicWidth();
        bounds[3] = values[4]*getDrawable().getIntrinsicHeight();
        Bitmap bitmap = ((BitmapDrawable)getDrawable()).getBitmap();
        bounds[4] = bitmap.getWidth();
        bounds[5] = bitmap.getHeight();
        return bounds;
        
	}



	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float[] bounds = getBitmapBound();
		
		
	}

}
