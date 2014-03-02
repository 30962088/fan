package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.SelectCategoryActivity.GridAdapter;
import com.yoka.fan.SelectCategoryActivity.Model;
import com.yoka.fan.SelectMainActivity.Result;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SelectConfirmActivity extends Activity implements OnClickListener{

	public static final String PARAM_RESULT = "PARAM_RESULT";
	
	private ImageLoader imageLoader;
	
	public List<Result> list;
	
	private ViewGroup listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.select_confirm_layout);
		findViewById(R.id.base_prev).setOnClickListener(this);
		findViewById(R.id.base_next).setOnClickListener(this);
		imageLoader = Utils.getImageLoader(this);
		String url = getIntent().getStringExtra(BaseSelectActivity.PARAM_IMG_PATH);
		list = (List<Result>) getIntent().getSerializableExtra(PARAM_RESULT);
		imageLoader.displayImage(url, (ImageView)findViewById(R.id.thumbnail));
		imageLoader.displayImage(url, (ImageView)findViewById(R.id.img));
		listView = ((ViewGroup)findViewById(R.id.listview));
		initListView();
	}
	
	private void initListView(){
		

		for(Result result : list){
			TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.category_item,null);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = DisplayUtils.Dp2Px(this, 10);
			int padding = DisplayUtils.Dp2Px(this, 12);
			textView.setPadding(padding, padding/2, padding, padding/2);
			textView.setLayoutParams(layoutParams);
			textView.setText(result.getName());
			listView.addView(textView);
		}
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_prev:
			finish();
			break;
		case R.id.base_next:
			
			break;
		default:
			break;
		}
		
	}
	
}
