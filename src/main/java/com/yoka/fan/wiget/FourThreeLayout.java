package com.yoka.fan.wiget;

import com.yoka.fan.utils.DisplayUtils;

import android.content.Context;
import android.util.AttributeSet;

import android.view.Display;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class FourThreeLayout extends FrameLayout{

	public FourThreeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public FourThreeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FourThreeLayout(Context context) {
		super(context);
		init();
	}

	private void init(){
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		int width = display.getWidth()-DisplayUtils.Dp2Px(getContext(), 20)*2;
		int height = display.getHeight()/3*4;
		setLayoutParams(new LayoutParams(width, height));
	}
	
	
	
}
