package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.SelectCategoryActivity.Model;
import com.yoka.fan.utils.Category;
import com.yoka.fan.utils.Category.Color;
import com.yoka.fan.utils.Category.findColorListener;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.SearchInput;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectColorActivity extends BaseSelectActivity implements
		OnClickListener, TextWatcher {

	private GridView gridView;

	private SearchInput inputView;

	private List<SelectCategoryActivity.Model> models;

	private SelectCategoryActivity.Model model;
	
	private Model selectModel;
	
	private List<Model> list;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_color_layout);
		inputView = (SearchInput) findViewById(R.id.search_input);
		inputView.addTextChangedListener(this);
		inputView.getSearchInput().setHint("选择颜色");
		models = (List<com.yoka.fan.SelectCategoryActivity.Model>) getIntent()
				.getSerializableExtra(PARAM_SELECTED_LIST);
		((GridView) findViewById(R.id.select_list))
				.setAdapter(new SelectCategoryActivity.GridAdapter(this, models,false));
//		setNextEnable(false);
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(selectModel != null){
					selectModel.setSelected(false);
				}
				selectModel = list.get(position);
				String color = selectModel.name;
				model = new SelectCategoryActivity.Model(color, color,SelectCategoryActivity.Model.TYPE_COLOR);
				selectModel.setSelected(true);
				adapter.notifyDataSetChanged();
//				inputView.getSearchInput().setText("");
//				inputView.getSearchInput().append(list.get(position).name);
				onNextClick();
			}
			
		});
		Category.findColorByPinyin(new findColorListener() {

			@Override
			public void success(List<Color> result) {

				update(toList(result));

			}
		});
	}

	

	public static List<Model> toList(List<Color> colors) {
		List<Model> models = new ArrayList<SelectColorActivity.Model>();
		for (Color color : colors) {
			models.add(new Model(color.getEn(), color.getZh(), color.getUrl()));
		}
		return models;
	}
	
	private GridAdapter adapter;

	public void update(final List<Model> list) {
		this.list = list;
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				adapter = new GridAdapter(SelectColorActivity.this,list);
				gridView.setAdapter(adapter);

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

	private static class Model {
		private String id;
		private String name;
		private String url;
		private boolean selected;

		public Model(String id, String name, String url) {
			this.id = id;
			this.name = name;
			this.url = url;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}

	private static class GridAdapter extends BaseAdapter {

		private List<Model> models;

		private Context context;

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
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.color_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(model.selected){
				convertView.setBackgroundResource(R.drawable.category_item_bg_selected);
			}else{
				convertView.setBackgroundResource(R.drawable.category_item_bg);
			}
			
			if (model.url != null) {
				holder.imageView.setVisibility(View.VISIBLE);
				imageLoader.displayImage(model.url, holder.imageView);
			} else {
				holder.imageView.setVisibility(View.GONE);
			}

			holder.textView.setText(model.name);
			return convertView;
		}

		public static class ViewHolder {
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
		/*if(s.toString().length() == 0){
			setNextEnable(false);
		}else{
			setNextEnable(true);
		}*/
		Category.findColorByPinyin(s.toString(), new findColorListener() {

			@Override
			public void success(List<Color> result) {

				update(toList(result));

			}
		});

	}

	@Override
	protected String getPrevText() {
		// TODO Auto-generated method stub
		return "品牌";
	}

	@Override
	protected String getNextText() {
		// TODO Auto-generated method stub
		return "链接";
	}

	@Override
	protected void onPrevClick() {
		finish();

	}

	@Override
	protected void onNextClick() {
		if(model == null){
			Utils.tip(this, "请选择颜色");
			return;
		}
		String color = inputView.getSearchInput().getText().toString();
		ArrayList<SelectCategoryActivity.Model> list = new ArrayList<SelectCategoryActivity.Model>(models);
		list.add(model);
		Intent intent = new Intent(this,SelectLinkActivity.class);
		Bundle bundle = getIntent().getExtras();
		if(bundle == null){
			bundle = new Bundle();
		}
		bundle.putSerializable(PARAM_SELECTED_LIST, list);
		intent.putExtras(bundle);
		startActivity(intent);

	}

}
