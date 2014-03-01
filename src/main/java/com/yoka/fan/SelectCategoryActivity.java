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

public class SelectCategoryActivity extends Activity implements OnClickListener,TextWatcher{
	
	public static final String PARAM_MODEL = "PARAM_MODEL";
	
	private ListView listView;
	
	private SearchInput inputView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_catetory_layout);
		findViewById(R.id.prev).setOnClickListener(this);
		findViewById(R.id.next).setEnabled(false);
		findViewById(R.id.next).setOnClickListener(this);
		inputView = (SearchInput) findViewById(R.id.search_input);
		inputView.addTextChangedListener(this);
		/*List<ListModel> list = new ArrayList<SelectCategoryActivity.ListModel>();
		for(int i = 0;i<10;i++){
			
			List<Model> models = new ArrayList<SelectCategoryActivity.Model>();
			
			for(int j = 0;j<10;j++){
				models.add(new Model(""+i, "item"));
			}
			list.add(new ListModel("标题", models));
		}*/
		
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
				
				listView.setAdapter(new ListAdapter(SelectCategoryActivity.this, list));
				
			}
		});
	}
	
	private static List<ListModel> catToList(Map<String, List<Tag>> map){
		List<ListModel> list = new ArrayList<SelectCategoryActivity.ListModel>();
		for(String key : map.keySet()){
			List<Model> models = new ArrayList<SelectCategoryActivity.Model>();
			for(Tag tag : map.get(key)){
				models.add(new Model(tag.getTag_en(),tag.getTag_zh()));
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
		private String id;
		private String name;
		public Model(String id,String name) {
			this.id = name;
			this.name = name;
		}
		public String getId() {
			return id;
		}
		public String getName() {
			return name;
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
		
		public ListAdapter(Context context, List<ListModel> list) {
			this.list = list;
			this.context = context;
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
			ListModel model = list.get(position);
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
		Category.findCatsByPinyin(s.toString(),new findCatsListener() {
			
			@Override
			public void success(Map<String, List<Tag>> result) {
				update(catToList(result));
				
			}
		});
		
	}
	
}
