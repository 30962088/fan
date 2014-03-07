package com.yoka.fan.utils;

import android.content.Context;

public class DisplayUtils {
	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static int pxToSp(Context context, float pixelValue) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		int sp = (int) (pixelValue / scaledDensity + 0.5f);
		return sp;
	}


	public static int spToPx(Context context, float spValue) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		int pixelValue = (int) (spValue * scaledDensity + 0.5f);
		return pixelValue;
	}

}
