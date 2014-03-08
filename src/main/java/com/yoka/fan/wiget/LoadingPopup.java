package com.yoka.fan.wiget;

import com.yoka.fan.R;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class LoadingPopup extends PopupWindow {

	private View view;

	private LoadingPopup(Context context) {
		view = LayoutInflater.from(context).inflate(R.layout.loading_popup,
				null);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setContentView(view);
	}

	private void show() {
		showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	private static LoadingPopup popup;

	public static void show(final Context context) {
		new Handler(context.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				if (popup == null) {
					popup = new LoadingPopup(context);
				}
				popup.show();
			}

		});
	}

	public static void hide(Context context) {
		new Handler(context.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				if (popup != null) {
					popup.dismiss();
				}
			}

		});
		
	}

}
