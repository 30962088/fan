package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.utils.Utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FansListActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		ListView listView = new ListView(this);
		FansListAdapter adapter = new FansListAdapter(this, new ArrayList<FansListActivity.Model>(){{
			for(int i = 0;i<20;i++){
				add(new Model("1","http://tp4.sinaimg.cn/2129028663/180/5684393877/1","自萌",false));
			}
		}});
		listView.setAdapter(adapter);
		setContentView(listView);
	}
	
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "粉丝";
	}
	
	private static class Model{
		private String id;
		private String photo;
		private String name;
		private boolean selected;
		public Model(String id, String photo, String name, boolean selected) {
			super();
			this.id = id;
			this.photo = photo;
			this.name = name;
			this.selected = selected;
		}
		
	}
	
	private static class FansListAdapter extends BaseAdapter{

		private List<Model> list;
		
		private Context context;
		
		private ImageLoader imageLoader;

		public FansListAdapter(Context context,List<Model> list) {
			this.list = list;
			this.context = context;
			imageLoader = Utils.getImageLoader(context);
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
			final Model model = list.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.fan_item,null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			imageLoader.displayImage(model.photo, holder.imageview);
			holder.nameView.setText(model.name);
			holder.btnView.setSelected(model.selected);
			holder.btnView.setText(model.selected ? "已关注" : "关注");
			holder.btnView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean selected = model.selected;
					selected = !selected;
					model.selected = selected;
					notifyDataSetChanged();
				}
			});
			
			
			return convertView;
		}
		
		private static class ViewHolder{
			private ImageView imageview;
			private TextView nameView;
			private TextView btnView;
			public ViewHolder(View view) {
				imageview = (ImageView) view.findViewById(R.id.photo);
				nameView = (TextView) view.findViewById(R.id.name);
				btnView = (TextView) view.findViewById(R.id.btn);
			}
			
		}
		
		
		
	}

}
