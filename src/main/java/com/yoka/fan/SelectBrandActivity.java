package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.yoka.fan.SelectCategoryActivity.Model;
import com.yoka.fan.utils.Category;
import com.yoka.fan.utils.Category.Brand;
import com.yoka.fan.wiget.SearchInput;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectBrandActivity extends BaseSelectActivity implements TextWatcher{
	
	private ListView listview;
	
	private SearchInput inputView;
	
	private Model model;
	
	private List<Model> list;
	
	private ArrayList<Model> selectedList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_brand_layout);
		
		inputView = (SearchInput) findViewById(R.id.search_input);
		inputView.addTextChangedListener(this);
		inputView.getSearchInput().setHint("选择品牌");
		setNextEnable(false);
		
		
		selectedList = (ArrayList<Model>) getIntent().getSerializableExtra(PARAM_SELECTED_LIST);
		if(selectedList != null){
			((GridView)findViewById(R.id.select_list)).setAdapter(new SelectCategoryActivity.GridAdapter(this, selectedList));
		}
		
		listview = (ListView) findViewById(R.id.listview);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				model = list.get(position);
				inputView.getSearchInput().setText("");
				inputView.getSearchInput().append(model.getName());
				setNextEnable(true);
				
			}
			
		});
		
		Category.findBrandByPinyin(new Category.findBrandListener() {
			
			@Override
			public void success(List<Brand> result) {
				
				update(toList(result));
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	public static List<Model> toList(List<Brand> brands){
		List<Model> models = new ArrayList<Model>();
		for(Brand brand : brands){
			models.add(new Model(brand.getName(),brand.getName(),Model.TYPE_BRAND));
		}
		return models;
	}
	
	public void update(final List<Model> list){
		this.list = list;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				listview.setAdapter(new ListAdapter(SelectBrandActivity.this, list));
				
			}
		});
	}
	

	
	
	private static class ListAdapter extends BaseAdapter{
		
		private List<Model> models;
		
		private Context context;
		
		public ListAdapter(Context context, List<Model> models) {
			this.context = context;
			this.models = models;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return models.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return models.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Model model = models.get(position);
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.brand_item,null);
			}
			((TextView)convertView).setText(model.getName());
			return convertView;
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
		setNextEnable(false);
		Category.findBrandByPinyin(s.toString(),new Category.findBrandListener() {
			
			@Override
			public void success(List<Brand> result) {
				
				update(toList(result));
			}
		});
		
	}

	@Override
	protected String getPrevText() {
		// TODO Auto-generated method stub
		return "分类";
	}

	@Override
	protected String getNextText() {
		// TODO Auto-generated method stub
		return "颜色";
	}

	@Override
	protected void onPrevClick() {
		finish();
		
	}

	@Override
	protected void onNextClick() {
		ArrayList<Model> list = new ArrayList<SelectCategoryActivity.Model>(selectedList);
		list.add(model);
		Intent intent = new Intent(this,SelectColorActivity.class);
		Bundle bundle = getIntent().getExtras();
		if(bundle == null){
			bundle = new Bundle();
		}
		bundle.putSerializable(PARAM_SELECTED_LIST, list);
		intent.putExtras(bundle);
		startActivity(intent);
		
	}
	
}