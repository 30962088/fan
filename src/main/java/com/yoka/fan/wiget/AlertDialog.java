package com.yoka.fan.wiget;

import com.yoka.fan.R;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class AlertDialog {

	private static String _text = "";
	
	public static void show(final Context context, final String text) {
		if(text.equals(_text)){
			return;
		}
		_text = text;
		new Handler(context.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				View popupView = LayoutInflater.from(context).inflate(
						R.layout.alert_layout, null);

				// Animation zoom = AnimationUtils.loadAnimation(context,
				// R.anim.zoom_center);

				((TextView) popupView.findViewById(R.id.alert_text))
						.setText(text);

//				final PopupWindow mPopupWindow = new PopupWindow(popupView,
//						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
//						true);
//
//				mPopupWindow.setTouchable(false);
//				mPopupWindow.setOutsideTouchable(false);
//				mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
				Toast toast = new Toast(context);

				toast.setDuration(Toast.LENGTH_SHORT);

				toast.setGravity(Gravity.CENTER, 0, 0);

				toast.setView(popupView);

				toast.show();

				// popupView.findViewById(R.id.popup).startAnimation(zoom);

				new Handler(context.getMainLooper()).postDelayed(
						new Runnable() {

							@Override
							public void run() {
								_text = null;

							}
						}, 2000);

			}
		});

	}

}
