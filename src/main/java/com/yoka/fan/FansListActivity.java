package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.network.Fans;
import com.yoka.fan.network.Info.Result;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FansListActivity extends BaseActivity{

	private FansListAdapter adapter;
	
	private List<Model> list;
	
	private int offset = 0;
	
	private int limit = 20;
	
	private PullToRefreshListView listView;
	
	private String target_id;
	
	private boolean hasMore;
	
	private View footerView;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		target_id = getIntent().getStringExtra("target_id");
		listView = new PullToRefreshListView(this);
		list = new ArrayList<FansListActivity.Model>();
		adapter = new FansListAdapter(this, list);
		footerView = LayoutInflater.from(this).inflate( R.layout.footer_loading, null);
		listView.getRefreshableView().addFooterView(footerView);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(
					PullToRefreshBase<ListView> refreshView) {
				offset = 0;
				load();
				
			}
		});
		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if(hasMore){
					load();
				}
				
			}
		});
		load();
		setContentView(listView);
	}
	
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "粉丝";
	}
	
	private void load(){
		final User user = User.readUser();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Fans request = new Fans(user.id,target_id,offset,limit) {
					
					@Override
					protected void onSuccess(List<Result> results) {
						hasMore = results.size()>=limit?true:false;
						if(offset == 0){
							list.clear();
						}
						for(Result result : results){
							list.add(new Model(result.getId(), result.getHead_url(), result.getNick(),false));
						}
						offset += limit;
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								adapter.notifyDataSetChanged();
								listView.onRefreshComplete();
								if(!hasMore){
									listView.getRefreshableView().removeFooterView(footerView);
								}
							}
						});
						
						
					}
				};
				request.request();
			}
		}).start();
		
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
