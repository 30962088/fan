package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.network.CommentList;
import com.yoka.fan.network.CreateComment;
import com.yoka.fan.network.Request;
import com.yoka.fan.network.CommentList.Result;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.SharePopupWindow.Share;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CommentActivity extends BaseActivity implements OnClickListener{

	public static final String PARAM_COLL_ID = "PARAM_COLL_ID";
	
	private String collId;
	
	private int offset = 0;
	
	private int limit = 20;
	
	private User user;
	
	private boolean hasMore;
	
	private ListViewAdapter adapter;
	
	private List<Comment> list;
	
	private PullToRefreshListView listView;
	
	private View footerView;
	
	private EditText commentView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = User.readUser();
		collId = getIntent().getStringExtra(PARAM_COLL_ID);
		setContentView(R.layout.comment_layout);
		
		
		listView = (PullToRefreshListView) findViewById(R.id.listview);
		list = new ArrayList<CommentActivity.Comment>();
		ListViewAdapter adapter = new ListViewAdapter(list, this);
		commentView = (EditText) findViewById(R.id.comment);
		final View publishView = findViewById(R.id.publish);
		publishView.setOnClickListener(this);
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
        findViewById(R.id.comment_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(commentView.getWindowToken(), 0);
				
			}
		});
        
        commentView.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length()>0){
					publishView.setEnabled(true);
				}else{
					publishView.setEnabled(false);
				}
				
			}
        	
        });
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}
	
	private void load(){
		new AsyncTask<Void, Void, List<CommentList.Result>>() {

			@Override
			protected List<Result> doInBackground(Void... params) {
				CommentList request = new CommentList(user.id, user.access_token, collId, offset, limit);
				request.request();
				return request.getResults();
			}
			
			protected void onPostExecute(java.util.List<CommentList.Result> results) {
				if(results != null){
					hasMore = results.size()>=limit?true:false;
					if(offset == 0){
						list.clear();
					}
					for(Result result : results){
						list.add(new Comment(result.getU_thumb(), result.getU_nick(), result.getDateFormater(), result.getTxt()));
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
			
		}.execute();
		
	}
	
	private static class Comment{
		public String photo;
		public String name;
		public String datetime;
		public String comment;
		public Comment(String photo, String name, String datetime,
				String comment) {
			super();
			this.photo = photo;
			this.name = name;
			this.datetime = datetime;
			this.comment = comment;
		}
		
		
	}
	
	private static class ListViewAdapter extends BaseAdapter{

		private List<Comment> list;
		
		private Context context;
		
		private ImageLoader imageLoader;
		
		public ListViewAdapter(List<Comment> list, Context context) {
			super();
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
			Comment comment = list.get(position);
			
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.comment_item,null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			imageLoader.displayImage(comment.photo, holder.photoView);
			holder.datetimeView.setText(comment.datetime);
			holder.nameView.setText(comment.name);
			holder.commentView.setText(comment.comment);
			return convertView;
		}
		
		private static class ViewHolder{
			
			public ImageView photoView;
			
			public TextView nameView;
			
			public TextView datetimeView;
			
			public TextView commentView;
			
			public ViewHolder(View view) {
				photoView = (ImageView) view.findViewById(R.id.photo);
				nameView = (TextView) view.findViewById(R.id.name);
				datetimeView = (TextView) view.findViewById(R.id.datetime);
				commentView = (TextView) view.findViewById(R.id.comment);
			}	
		}
		
	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "评论";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publish:
			onPublish();
			break;

		default:
			break;
		}
		
	}

	private void onPublish() {
		final String content = commentView.getText().toString();
		new AsyncTask<Void, Void, Request.Status>(){

			@Override
			protected com.yoka.fan.network.Request.Status doInBackground(
					Void... params) {
				CreateComment request = new CreateComment(user.id, user.access_token, collId, content, 1);
				request.request();
				return request.getStatus();
			}
			
			protected void onPostExecute(com.yoka.fan.network.Request.Status result) {
				
			};
			
		}.execute();
		
		
		
	}
	
}
