package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.yoka.fan.SelectCategoryActivity.Model;
import com.yoka.fan.utils.Category;
import com.yoka.fan.utils.Category.Brand;
import com.yoka.fan.wiget.SearchInput;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectBrandActivity extends Activity implements OnClickListener,TextWatcher{
	
	private ListView listview;
	
	private SearchInput inputView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_brand_layout);
		findViewById(R.id.prev).setOnClickListener(this);
		findViewById(R.id.next).setEnabled(false);
		findViewById(R.id.next).setOnClickListener(this);
		inputView = (SearchInput) findViewById(R.id.search_input);
		inputView.addTextChangedListener(this);
		
		List<SelectCategoryActivity.Model> models = new ArrayList<SelectCategoryActivity.Model>(){{
			add(new SelectCategoryActivity.Model("1","a"));
		}};
		((GridView)findViewById(R.id.select_list)).setAdapter(new SelectCategoryActivity.GridAdapter(this, models));
		/*List<ListModel> list = new ArrayList<SelectCategoryActivity.ListModel>();
		for(int i = 0;i<10;i++){
			
			List<Model> models = new ArrayList<SelectCategoryActivity.Model>();
			
			for(int j = 0;j<10;j++){
				models.add(new Model(""+i, "item"));
			}
			list.add(new ListModel("标题", models));
		}*/
		
		listview = (ListView) findViewById(R.id.listview);
		
		Category.findBrandByPinyin(new Category.findBrandListener() {
			
			@Override
			public void success(List<Brand> result) {
				
				update(toList(result));
			}
		});
		
	}
	
	public static List<Model> toList(List<Brand> brands){
		List<Model> models = new ArrayList<Model>();
		for(Brand brand : brands){
			models.add(new Model(brand.get_id(),brand.getName()));
		}
		return models;
	}
	
	public void update(final List<Model> list){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				listview.setAdapter(new ListAdapter(SelectBrandActivity.this, list));
				
			}
		});
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prev:
			finish();
			break;
		case R.id.next:
			
			break;
		default:
			break;
		}
		
	}
	
	
	private static class ListAdapter extends BaseAdapter{
		
		private List<Model> models;
		
		private Context context ;
		
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
		Category.findBrandByPinyin(s.toString(),new Category.findBrandListener() {
			
			@Override
			public void success(List<Brand> result) {
				
				update(toList(result));
			}
		});
		
	}
	
}
