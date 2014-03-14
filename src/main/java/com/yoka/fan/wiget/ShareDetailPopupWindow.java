package com.yoka.fan.wiget;

import com.yoka.fan.R;
import com.yoka.fan.utils.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ShareDetailPopupWindow implements OnClickListener, TextWatcher {

	public static interface OnOperateLisener {
		public void onsubmit(String photo, String content);
	}

	private PopupWindow mPopupWindow;

	private EditText contentText;

	private ImageView thumnail;

	private TextView countText;

	private int limit = 110;

	private OnOperateLisener onOperateLisener;

	private String photo;

	private View loading;

	private Context context;

	public ShareDetailPopupWindow(Context context, String title, String photo,
			String content) {
		this.photo = photo;
		this.context = context;
		View view = LayoutInflater.from(context).inflate(
				R.layout.share_detail_layout, null);
		view.setOnClickListener(this);
		loading = view.findViewById(R.id.loading);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#4D000000")));
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);

		Animation rotation = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_from_bottom);

		((TextView) view.findViewById(R.id.title)).setText(title);
		View popup = view.findViewById(R.id.popup);
		contentText = (EditText) view.findViewById(R.id.content);
		contentText
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						limit) });
		contentText.addTextChangedListener(this);

		countText = (TextView) view.findViewById(R.id.count);
		countText.setText("" + limit);
		thumnail = (ImageView) view.findViewById(R.id.thumbnail);
		Utils.getImageLoader(context).displayImage(photo, thumnail);
		popup.setOnClickListener(this);
		popup.startAnimation(rotation);
		view.findViewById(R.id.layout).setOnClickListener(this);
		view.findViewById(R.id.cancel).setOnClickListener(this);
		view.findViewById(R.id.send).setOnClickListener(this);
		mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		contentText.setText(content);
	}

	public void showLoading() {
		new Handler(context.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				loading.setVisibility(View.VISIBLE);

			}
		});

	}

	public void hideLoading() {
		new Handler(context.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				loading.setVisibility(View.GONE);

			}
		});
	}

	public void setOnOperateLisener(OnOperateLisener onOperateLisener) {
		this.onOperateLisener = onOperateLisener;
	}

	public void dismiss() {
		mPopupWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout:
		case R.id.cancel:
			mPopupWindow.dismiss();
		case R.id.send:
			if (onOperateLisener != null) {
				onOperateLisener.onsubmit(photo, contentText.getText()
						.toString());
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		countText.setText(String.valueOf(limit - s.toString().length()));

	}

}
