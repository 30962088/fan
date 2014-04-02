package com.yoka.fan;


import java.util.ArrayList;
import java.util.List;

import com.yoka.fan.SelectCategoryActivity.Model;
import com.yoka.fan.SelectMainActivity.Result;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.AlertDialog;




import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.GridView;

public class SelectLinkActivity extends BaseSelectActivity implements TextWatcher {


	private EditText linkView;
	
	private EditText priceView;

	private List<SelectCategoryActivity.Model> models;
	
	private Result selected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		selected = (Result) getIntent().getSerializableExtra(PARAM_SELECTED_RESULT);
		setContentView(R.layout.select_link_layout);
		linkView = (EditText) findViewById(R.id.link_input);
		linkView.addTextChangedListener(this);
		priceView = (EditText) findViewById(R.id.price_input);
		priceView.addTextChangedListener(this);
		if(selected != null){
			linkView.setText(selected.getLink().getUrl());
			if(selected.getLink() != null && selected.getLink().getPrice() != null && selected.getLink().getPrice()>0){
				priceView.setText(""+selected.getLink().getPrice());
			}
			
		}
		models = (List<com.yoka.fan.SelectCategoryActivity.Model>) getIntent()
				.getSerializableExtra(PARAM_SELECTED_LIST);
		((GridView) findViewById(R.id.select_list))
				.setAdapter(new SelectCategoryActivity.GridAdapter(this, models,false));
		
//		setNextEnable(false);
	
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
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
//		if(URLUtil.isValidUrl(linkView.getText().toString())){
//			try{
//				Float.parseFloat(priceView.getText().toString());
//				setNextEnable(true);
//			}catch(NumberFormatException exception){
//				setNextEnable(false);
//			}
//				
//		
//			
//		}else{
//			setNextEnable(false);
//		}
	}

	@Override
	protected String getPrevText() {
		// TODO Auto-generated method stub
		return "颜色";
	}

	@Override
	protected String getNextText() {
		// TODO Auto-generated method stub
		return "下一步";
	}

	@Override
	protected void onPrevClick() {
		finish();

	}

	@Override
	protected void onNextClick() {
		String url = linkView.getText().toString();
		String price = priceView.getText().toString();
		if(!TextUtils.isEmpty(url) && !URLUtil.isValidUrl(url)){
			AlertDialog.show(this, "链接格式错误");
			return;
		}
		if(!TextUtils.isEmpty(price)){
			try{
				Float.parseFloat(price);
			}catch(NumberFormatException e){
				AlertDialog.show(this, "请输入正确的价格");
				return;
			}
			
		}
		ArrayList<SelectCategoryActivity.Model> list = new ArrayList<SelectCategoryActivity.Model>(models);
		if(!TextUtils.isEmpty(url)){
			list.add(new Model(url, url, Model.TYPE_LINK));
		}
		
		if(!TextUtils.isEmpty(price)){
			list.add(new Model(price,price,Model.TYPE_PRICE));
		}
		
		
		
		Intent intent = new Intent(this,SelectMainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setAction(SelectMainActivity.ACTION_COMPLETE);
		Bundle bundle = getIntent().getExtras();
		if(bundle == null){
			bundle = new Bundle();
		}
		bundle.putSerializable(PARAM_SELECTED_LIST, list);
		intent.putExtras(bundle);
		startActivity(intent);

	}

}
