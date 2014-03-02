package com.yoka.fan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yoka.fan.utils.Category;
import com.yoka.fan.utils.Category.Tag;
import com.yoka.fan.utils.Category.findCatsListener;
import com.yoka.fan.wiget.SearchInput;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectCategoryActivity extends BaseSelectActivity implements OnClickListener,TextWatcher{
	
	public static final String PARAM_MODEL = "PARAM_MODEL";
	
	private ListView listView;
	
	private SearchInput inputView;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_catetory_layout);
		setNextEnable(false);
		inputView = (SearchInput) findViewById(R.id.search_input);
		inputView.addTextChangedListener(this);
		inputView.getSearchInput().setHint("选择分类");
		listView = (ListView) findViewById(R.id.list);
		
		Category.findCatsByPinyin(new findCatsListener() {
			
			@Override
			public void success(Map<String, List<Tag>> result) {
				update(catToList(result));
				
			}
		});
		
	}
	
	public static void setGridViewHeightBasedOnChildren(GridView gridView) {
		BaseAdapter listAdapter = (BaseAdapter) gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int count = listAdapter.getCount();
		View listItem = listAdapter.getView(0, null, gridView);
		listItem.measure(0, 0); // 计算子项View 的宽高
		totalHeight = listItem.getMeasuredHeight(); // 统计所有子项的总高度
		int yu = count % 4;
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		if (yu > 0) {
			params.height = (count - yu) / 4 * (totalHeight + 10)
					+ totalHeight;
		} else {
			params.height = count / 4 * totalHeight + (count / 4 - 1) * 10;
		}
		gridView.setLayoutParams(params);
	}
	
	public void update(final List<ListModel> list){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				listView.setAdapter(new ListAdapter(SelectCategoryActivity.this, list,inputView.getSearchInput()));
				
			}
		});
	}
	
	private static List<ListModel> catToList(Map<String, List<Tag>> map){
		List<ListModel> list = new ArrayList<SelectCategoryActivity.ListModel>();
		for(String key : map.keySet()){
			List<Model> models = new ArrayList<SelectCategoryActivity.Model>();
			for(Tag tag : map.get(key)){
				models.add(new Model(tag.getTag_en(),tag.getTag_zh(),Model.TYPE_TAG));
			}
			list.add(new ListModel(key, models));
		}
		return list;
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
	
	public static class Model implements Serializable{
		public static final int TYPE_COLOR = 0;
		public static final int TYPE_BRAND = 1;
		public static final int TYPE_TAG = 2;
		private String id;
		private String name;
		private int type;
	
		public Model(String id, String name, int type) {
			this.id = id;
			this.name = name;
			this.type = type;
		}

		public String getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public int getType() {
			return type;
		}
	}
	
	private static class ListModel{
		private String title;
		private List<Model> list;
		public ListModel(String title, List<Model> list) {
			this.title = title;
			this.list = list;
		}
		
		
	}
	
	private static class ListAdapter extends BaseAdapter{

		private List<ListModel> list;
		
		private Context context;
		
		private EditText editText;
		
		public ListAdapter(Context context, List<ListModel> list,EditText editText) {
			this.list = list;
			this.context = context;
			this.editText = editText;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ListModel model = list.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.category_list,null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(model.title);
			holder.gridView.setAdapter(new GridAdapter(context, model.list));
			holder.gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					editText.setText("");
					editText.append(model.list.get(position).name);
					
				}
			});
			setGridViewHeightBasedOnChildren(holder.gridView);
			return convertView;
		}
		
		public static class ViewHolder{
			private TextView textView;
			private GridView gridView;
			public ViewHolder(View view) {
				textView = (TextView) view.findViewById(R.id.textview);
				gridView = (GridView) view.findViewById(R.id.gridview);
			}
		}
		
		
		
	}
	
	public static class GridAdapter extends BaseAdapter{
		
		private List<Model> models;
		
		private Context context;
		
		public GridAdapter(Context context, List<Model> models) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.category_item,null);
			}
			((TextView)convertView).setText(model.name);
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
		if(s.toString().length() == 0){
			setNextEnable(false);
		}else{
			setNextEnable(true);
		}
		Category.findCatsByPinyin(s.toString(),new findCatsListener() {
			
			@Override
			public void success(Map<String, List<Tag>> result) {
				update(catToList(result));
				
			}
		});
		
	}

	@Override
	protected String getPrevText() {
		// TODO Auto-generated method stub
		return "返回";
	}

	@Override
	protected String getNextText() {
		// TODO Auto-generated method stub
		return "品牌";
	}

	@Override
	protected void onPrevClick() {
		finish();
	}

	@Override
	protected void onNextClick() {
		String result = inputView.getSearchInput().getText().toString();
		ArrayList<Model> models = new ArrayList<SelectCategoryActivity.Model>();
		models.add(new Model("", result, Model.TYPE_TAG));
		Intent intent = new Intent(this,SelectBrandActivity.class);
		Bundle bundle = getIntent().getExtras();
		if(bundle == null){
			bundle = new Bundle();
		}
		bundle.putSerializable(PARAM_SELECTED_LIST, models);
		intent.putExtras(bundle);
		startActivity(intent);
		
		
	}
	
}
