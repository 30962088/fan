package com.yoka.fan;


import java.util.ArrayList;
import java.util.List;

import com.yoka.fan.SelectCategoryActivity.Model;




import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;

public class SelectLinkActivity extends BaseSelectActivity implements TextWatcher {


	private EditText linkView;
	
	private EditText priceView;

	private List<SelectCategoryActivity.Model> models;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_link_layout);
		linkView = (EditText) findViewById(R.id.link_input);
		linkView.addTextChangedListener(this);
		priceView = (EditText) findViewById(R.id.price_input);
		priceView.addTextChangedListener(this);
		models = (List<com.yoka.fan.SelectCategoryActivity.Model>) getIntent()
				.getSerializableExtra(PARAM_SELECTED_LIST);
		((GridView) findViewById(R.id.select_list))
				.setAdapter(new SelectCategoryActivity.GridAdapter(this, models));
		
		setNextEnable(false);
	
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
		if(linkView.getText().toString().length() == 0 || priceView.getText().toString().length() == 0){
			setNextEnable(false);
		}else{
			setNextEnable(true);
		}
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
		ArrayList<SelectCategoryActivity.Model> list = new ArrayList<SelectCategoryActivity.Model>(models);
		list.add(new Model(linkView.getText().toString(), linkView.getText().toString(), Model.TYPE_LINK));
		list.add(new Model(priceView.getText().toString(),priceView.getText().toString(),Model.TYPE_PRICE));
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
