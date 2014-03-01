package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.utils.Category;
import com.yoka.fan.utils.Category.Color;
import com.yoka.fan.utils.Category.findColorListener;
import com.yoka.fan.utils.Utils;
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
import android.widget.ImageView;
import android.widget.TextView;

public class SelectColorActivity extends Activity implements OnClickListener,TextWatcher{
	
	private GridView gridView;
	
	private SearchInput inputView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_color_layout);
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
		
		gridView = (GridView) findViewById(R.id.gridview);
		
		Category.findColorByPinyin(new findColorListener() {
			
			@Override
			public void success(List<Color> result) {
				
				update(toList(result));
				
			}
		});
		
	}
	
	public static List<Model> toList(List<Color> colors){
		List<Model> models = new ArrayList<SelectColorActivity.Model>();
		for(Color color : colors){
			models.add(new Model(color.getEn(),color.getZh(), color.getUrl()));
		}
		return models;
	}
	
	public void update(final List<Model> list){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				gridView.setAdapter(new GridAdapter(SelectColorActivity.this, list));
				
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
	
	private static class Model{
		private String id;
		private String name;
		private String url;
		public Model(String id, String name,String url) {
			this.id = id;
			this.name = name;
			this.url = url;
		}
	}
	
	private static class GridAdapter extends BaseAdapter{
		
		private List<Model> models;
		
		private Context context ;
		
		private ImageLoader imageLoader;
		
		public GridAdapter(Context context, List<Model> models) {
			this.context = context;
			this.models = models;
			imageLoader = Utils.getImageLoader(context);
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
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.color_item,null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			if(model.url != null){
				holder.imageView.setVisibility(View.VISIBLE);
				imageLoader.displayImage(model.url, holder.imageView);
			}else{
				holder.imageView.setVisibility(View.GONE);
			}
			
			holder.textView.setText(model.name);
			return convertView;
		}

		public static class ViewHolder{
			private ImageView imageView;
			private TextView textView;
			public ViewHolder(View view) {
				imageView = (ImageView) view.findViewById(R.id.img);
				textView = (TextView) view.findViewById(R.id.name);
			}
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
		Category.findColorByPinyin(s.toString(),new findColorListener() {
			
			@Override
			public void success(List<Color> result) {
				
				update(toList(result));
				
			}
		});
		
	}
	
}
