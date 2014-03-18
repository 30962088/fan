package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;
import com.yoka.fan.network.Info.Result;
import com.yoka.fan.network.Follow;
import com.yoka.fan.network.Recommand;
import com.yoka.fan.network.Request;
import com.yoka.fan.network.Request.Status;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.CommonPagerAdapter;
import com.yoka.fan.wiget.LoadingPopup;
import com.yoka.fan.wiget.CommonPagerAdapter.Page;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class RecommandListActivity extends BaseActivity2 implements OnClickListener{
	
	private List<Model> list = new ArrayList<RecommandListActivity.Model>();
	
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.recommand_layout);
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Recommand request = new Recommand(){

					@Override
					public void onSuccess(List<Result> array) {
						for(Result result : array){
							list.add(new Model(result.getId(), result.getHead_url(), result.getNick(), true));
						}
//						for(int i = 0;i<0;i++){
//							list.add(new Model(""+i, "http://tp4.sinaimg.cn/2129028663/180/5684393877/1", "的撒旦撒旦撒旦撒旦撒的撒的撒的撒爱的撒旦", true));
//						}
						initView();
						
					}
				};
				request.request();
				
			}
		}).start();
		
		
	}
	
	private void initView(){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ViewPager viewPager = ((ViewPager)findViewById(R.id.pager));
				viewPager.setAdapter(getAdapter(list));
				((CirclePageIndicator)findViewById(R.id.indicator)).setViewPager(viewPager);
				View saveBtn = findViewById(R.id.save_btn);
				saveBtn.setVisibility(View.VISIBLE);
				saveBtn.setOnClickListener(RecommandListActivity.this);
			}
		});
	}
	
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "推荐关注";
	}
	
	public static class RecommandFragement extends Fragment{
		
		private GridView gridView;
		
		private List<Model> list;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			list = (List<Model>) getArguments().get("list");
			
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			return inflater.inflate(R.layout.recommand_gridview, null);
		}
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			gridView = (GridView) view.findViewById(R.id.gridview);
			gridView.setAdapter(new GridAdapter(getActivity(), list));
		}
	}
	
	
	private CommonPagerAdapter getAdapter(List<Model> list){
		ArrayList<Model> models =null;
		List<Page> pages = new ArrayList<CommonPagerAdapter.Page>();
		for(int i = 0;i<list.size();i++){
			if(i % 6 == 0){
				if(models != null){
					RecommandFragement fragment = new RecommandFragement();
					Bundle bundle = new Bundle();
					bundle.putSerializable("list", models);
					fragment.setArguments(bundle);
					pages.add(new Page(""+i, fragment, false));
				}
				models = new ArrayList<RecommandListActivity.Model>();
			}
			models.add(list.get(i));
		}
		if(models != null){
			RecommandFragement fragment = new RecommandFragement();
			Bundle bundle = new Bundle();
			bundle.putSerializable("list", models);
			fragment.setArguments(bundle);
			pages.add(new Page(""+list.size(), fragment, false));
		}
		
		
		return new CommonPagerAdapter(getSupportFragmentManager(), pages);
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
	
	private static class GridAdapter extends BaseAdapter{

		private List<Model> list;
		
		private Context context;
		
		private ImageLoader imageLoader;

		public GridAdapter(Context context,List<Model> list) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.recommend_item,null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			imageLoader.displayImage(model.photo, holder.imageview);
			holder.nameView.setText(model.name);
			holder.btnView.setSelected(model.selected);
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
			private View btnView;
			public ViewHolder(View view) {
				imageview = (ImageView) view.findViewById(R.id.photo);
				nameView = (TextView) view.findViewById(R.id.name);
				btnView = view.findViewById(R.id.btn);
			}
			
		}
		
		
		
	}

	private void onSave(){
		final List<String> targetlist = new ArrayList<String>();
		for(Model model : list){
			if(model.selected){
				targetlist.add(model.id);
			}
		}
		final User user = User.readUser();
		if(targetlist.size() == 0){
			finish();
		}
		LoadingPopup.show(this);
		new AsyncTask<Void,Void, Status>() {

			@Override
			protected com.yoka.fan.network.Request.Status doInBackground(
					Void... params) {
				Follow follow = new Follow(user.id, targetlist, user.access_token);
				follow.request();
				return follow.getStatus();
			}
			
			@Override
			protected void onPostExecute(
					com.yoka.fan.network.Request.Status result) {
				if(result == Request.Status.SUCCESS){
					finish();
				}
				LoadingPopup.hide(RecommandListActivity.this);
				
				
			}
			
		}.execute();
		
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
//		Intent intent = new Intent(RecommandListActivity.this,MainActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save_btn:
			onSave();
			break;

		default:
			break;
		}
		
	}

}
