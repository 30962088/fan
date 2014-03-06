package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.network.Fans;
import com.yoka.fan.network.Follow;
import com.yoka.fan.network.GetFollower;
import com.yoka.fan.network.Request;
import com.yoka.fan.network.Info.Result;
import com.yoka.fan.network.Request.Status;
import com.yoka.fan.network.UnFollow;
import com.yoka.fan.utils.Relation;
import com.yoka.fan.utils.Relation.OperatorListener;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FansListActivity extends BaseActivity{
	
	public static final String PARAM_TYPE = "PARAM_TYPE";
	
	public static final String PARAM_FANS = "PARAM_FANS";
	
	public static final String PARAM_FOLLOWS = "PARAM_FOLLOWS";
	
	public static final String PARAM_TARGET_ID = "PARAM_TARGET_ID";

	private FansListAdapter adapter;
	
	private List<Model> list;
	
	private int offset = 0;
	
	private int limit = 20;
	
	private PullToRefreshListView listView;
	
	private String target_id;
	
	private boolean hasMore;
	
	private View footerView;
	
	private String type;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		type = getIntent().getStringExtra(PARAM_TYPE);
		target_id = getIntent().getStringExtra(PARAM_TARGET_ID);
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
		String title = "粉丝";
		if(PARAM_FOLLOWS.equals(type)){
			title = "关注";
		}
		return title;
	}
	
	private void load(){
		final User user = User.readUser();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Request request = null;
				if(PARAM_FANS.equals(type)){
					request = new Fans(user.id,target_id,offset,limit);
				}else if(PARAM_FOLLOWS.equals(type)){
					request = new GetFollower(user.id,target_id,offset,limit);
				}
				request.request();
				if(request.getStatus() == Status.SUCCESS){
					List<Result> results = null;
					if(request instanceof Fans){
						results = ((Fans)request).getResults();
					}else if(request instanceof GetFollower){
						results = ((GetFollower)request).getList();
					}
					hasMore = results.size()>=limit?true:false;
					if(offset == 0){
						list.clear();
					}
					for(Result result : results){
						list.add(new Model(result.getId(), result.getHeadUrl(), result.getNick(),false));
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
			holder.imageview.setImageResource(R.drawable.photo_default);
			imageLoader.displayImage(model.photo, holder.imageview);
			holder.nameView.setText(model.name);
			
			final TextView _btnView = holder.btnView;
			final User user = User.readUser();
			_btnView.setSelected(model.selected);
			_btnView.setText(model.selected ? "已关注" : "关注");
			Relation.findFollower(user, model.id ,new OperatorListener<Boolean>() {
				
				@Override
				public void success(final Boolean result) {
					new Handler(context.getMainLooper()).post(new Runnable() {
						
						@Override
						public void run() {
							model.selected = result;
							_btnView.setSelected(model.selected);
							_btnView.setText(model.selected ? "已关注" : "关注");
							_btnView.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									final boolean selected = !model.selected;
									_btnView.setSelected(selected);
									_btnView.setText(selected ? "已关注" : "关注");
									new AsyncTask<Void, Void, Status>(){

										@Override
										protected com.yoka.fan.network.Request.Status doInBackground(
												Void... params) {
											Request request = null;
											if(selected){
												request = new Follow(user.id, model.id, user.access_token);
											}else{
												request = new UnFollow(user.id, model.id, user.access_token);
											}
											request.request();
											return request.getStatus();
										}
										
										protected void onPostExecute(Request.Status result) {
											if(result == Request.Status.SUCCESS){
												if(selected){
													Relation.addFollow(user, model.id);
												}else{
													Relation.removeFollow(user, model.id);
												}
												
											}else{
												model.selected = !model.selected;
											}
										};
										
									}.execute();
									
									
									
									
								}
							});
							
						}
					});
					
					
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
