package com.yoka.fan.wiget;

import java.util.List;
import java.util.zip.Inflater;

import com.yoka.fan.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class ShareDetailPopupWindow implements OnClickListener,TextWatcher{

	
	
	private PopupWindow mPopupWindow;
	
	private EditText contentText;
	
	private ImageView thumnail;
	
	private TextView countText;
	
	private int limit = 110;
	
	public ShareDetailPopupWindow(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.share_detail_layout,null);
		view.setOnClickListener(this);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4D000000")));
		mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
       
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_bottom);
        
        View popup = view.findViewById(R.id.popup);
        contentText = (EditText) view.findViewById(R.id.content);
        contentText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(limit) });
        contentText.addTextChangedListener(this);
        countText = (TextView) view.findViewById(R.id.count);
        countText.setText(""+limit);
        thumnail = (ImageView) view.findViewById(R.id.thumbnail);
        
        popup.setOnClickListener(this);
        popup.startAnimation(rotation);
        view.findViewById(R.id.layout).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout:
		case R.id.cancel:
			mPopupWindow.dismiss();
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
		countText.setText(String.valueOf(limit-s.toString().length()));
		
	}
	
	
	
	
	

	
}
